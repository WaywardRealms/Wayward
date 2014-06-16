package net.wayward_realms.waywardchat;

import mkremins.fanciful.FancyMessage;
import net.wayward_realms.waywardlib.chat.Channel;
import net.wayward_realms.waywardlib.essentials.EssentialsPlugin;
import net.wayward_realms.waywardlib.util.math.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncPlayerChatListener implements Listener {

    private WaywardChat plugin;
    private YamlConfiguration pluginConfig;
    private YamlConfiguration emoteModeConfig;
    private YamlConfiguration prefixConfig;
    private ConcurrentHashMap<UUID, Location> UUIDLocations = new ConcurrentHashMap<>();

    public AsyncPlayerChatListener(final WaywardChat plugin) {
        this.plugin = plugin;
        // GET PLUGIN CONFIG
        Future<FileConfiguration> futureConfig = Bukkit.getScheduler().callSyncMethod(plugin, new Callable<FileConfiguration>() {
                    @Override
                    public FileConfiguration call() {
                        return plugin.getConfig();
                    }
                }
        );
        try {
            pluginConfig = (YamlConfiguration) futureConfig.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // END
        // GET emote-mode.yml FILE.
        Future<File> futureEmoteModeFolder = Bukkit.getScheduler().callSyncMethod(plugin, new Callable<File>() {
                    @Override
                    public File call() {
                        return new File(plugin.getDataFolder(), "emote-mode.yml");
                    }
                }
        );
        try {
            emoteModeConfig = YamlConfiguration.loadConfiguration(futureEmoteModeFolder.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // END
        // GET prefixes.yml FILE
        Future<File> futurePrefixConfig = Bukkit.getScheduler().callSyncMethod(plugin, new Callable<File>() {
                    @Override
                    public File call() {
                        return new File(plugin.getDataFolder(), "prefix.yml");
                    }
                }
        );
        try {
            prefixConfig = YamlConfiguration.loadConfiguration(futurePrefixConfig.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // END
        // SET UP FUCKING PLAYER LOCATION GETTER SHIT
        Bukkit.getScheduler().runTaskTimer(
                plugin,
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        for(Player player: Bukkit.getServer().getOnlinePlayers()) {
                    UUIDLocations.put(player.getUniqueId(), player.getLocation());
                        }
                    }
                },
                100L,
                100L
        );
        // END THAT SHIT
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final String message = event.getMessage();
        handleAsyncChat(player, message);
        event.setCancelled(true);
    }

    public void handleAsyncChat(final Player talking, String message) {
        if (!message.equals("")) {
            final String format;
            if (getEmoteMode(talking).isEmote(message)) {
                plugin.getChannel(pluginConfig.getString("default-channel")).log(talking.getName() + "/" + talking.getDisplayName() + ": " + message);
                for (Player player : new ArrayList<>(talking.getWorld().getPlayers())) {
                    formatEmote(talking, message).send(player);
                }
            } else {
                if (plugin.getPlayerChannel(talking) != null) {
                    plugin.getPlayerChannel(talking).log(talking.getName() + "/" + talking.getDisplayName() + ": " + message);
                    synchronized (plugin.getPlayerChannel(talking).getListeners()) {
                        for (Player player : new HashSet<>(plugin.getPlayerChannel(talking).getListeners())) {
                            if (player != null) {
                                if (plugin.getPlayerChannel(talking).getRadius() >= 0) {
                                    if (talking.getWorld().equals(player.getWorld())) {
                                        if (MathUtils.fastsqrt(corelateUUIDtoLocation(talking.getUniqueId()).distanceSquared(corelateUUIDtoLocation(player.getUniqueId()))) <= (double) plugin.getPlayerChannel(talking).getRadius()) {
                                            FancyMessage fancy = formatChannel(plugin.getPlayerChannel(talking), talking, player, message);
                                            fancy.send(player);
                                        }
                                    }
                                } else {
                                    formatChannel(plugin.getPlayerChannel(talking), talking, player, message).send(player);
                                }
                            }
                        }
                    }
                    if (plugin.getPlayerChannel(talking).isIrcEnabled()) {
                        format = plugin.getPlayerChannel(talking).getFormat()
                                .replace("%channel%", plugin.getPlayerChannel(talking).getName())
                                .replace("%prefix%", getPlayerPrefix(talking))
                                .replace("%player%", talking.getDisplayName())
                                .replace("%ign%", talking.getName())
                                .replace("&", ChatColor.COLOR_CHAR + "")
                                .replace("%message%", message);
                        Bukkit.getScheduler().runTask(plugin, new Runnable() {
                            public void run() {
                                plugin.getIrcBot().sendIRC().message(plugin.getPlayerChannel(talking).getIrcChannel(), ChatColor.stripColor(format));
                            }
                        });
                    }
                } else {
                    talking.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must talk in a channel! Use /chathelp for help.");
                }
            }
        }
    }

    public EmoteMode getEmoteMode(OfflinePlayer player) {
        if (emoteModeConfig.get(player.getName()) != null)
            return EmoteMode.valueOf(emoteModeConfig.getString(player.getName()));
        else
            return EmoteMode.TWO_ASTERISKS;
    }

    public String getPlayerPrefix(Permissible player) {
        for (String key : prefixConfig.getKeys(false)) {
            if (player.hasPermission("wayward.chat.prefix." + key)) {
                return ChatColor.translateAlternateColorCodes('&', prefixConfig.getString(key));
            }
        }
        return "";
    }

    public FancyMessage formatChannel(Channel channel, Player talking, Player recipient, String message) {
        FancyMessage fancy = new FancyMessage("");
        String format = channel.getFormat();
        ChatColor chatColour = null;
        ChatColor chatFormat = null;
        for (int i = 0; i < format.length(); i++) {
            if (format.charAt(i) == '&') {
                ChatColor colourOrFormat = ChatColor.getByChar(format.charAt(i + 1));
                if (colourOrFormat.isColor()) chatColour = colourOrFormat;
                if (colourOrFormat.isFormat()) chatFormat = colourOrFormat;
                i += 1;
            } else if (format.substring(i, i + ("%channel%").length()).equalsIgnoreCase("%channel%")) {
                fancy.then(channel.getName());
                if (chatColour != null) fancy.color(chatColour);
                if (chatFormat != null) fancy.style(chatFormat);
                i += ("%channel%").length() - 1;
            } else if (format.substring(i, i + ("%player%").length()).equalsIgnoreCase("%player%")) {
                fancy.then(talking.getDisplayName());
                fancy.tooltip(talking.getName());
                if (chatColour != null) fancy.color(chatColour);
                if (chatFormat != null) fancy.style(chatFormat);
                i += ("%player%").length() - 1;
            } else if (format.substring(i, i + ("%prefix%").length()).equalsIgnoreCase("%prefix%")) {
                fancy.then(getPlayerPrefix(talking));
                if (chatColour != null) fancy.color(chatColour);
                if (chatFormat != null) fancy.style(chatFormat);
                i += ("%prefix%").length() - 1;
            } else if (format.substring(i, i + ("%ign%").length()).equalsIgnoreCase("%ign%")) {
                fancy.then(talking.getName());
                fancy.tooltip(talking.getDisplayName());
                if (chatColour != null) fancy.color(chatColour);
                if (chatFormat != null) fancy.style(chatFormat);
                i += ("%ign%").length() - 1;
            } else if (format.substring(i, i + ("%message%").length()).equalsIgnoreCase("%message%")) {
                if (channel.isGarbleEnabled()) {
                    if (recipient != null) {
                        double distance = MathUtils.fastsqrt(talking.getLocation().distanceSquared(recipient.getLocation()));
                        double clearRange = 0.75D * (double) channel.getRadius();
                        double hearingRange = (double) channel.getRadius();
                        double clarity = 1.0D - ((distance - clearRange) / hearingRange);
                        String garbledMessage = plugin.garbleMessage(drunkify(talking, message), clarity);
                        fancy.then(garbledMessage);
                    }
                } else {
                    fancy.then(message);
                }
                if (chatColour != null) fancy.color(chatColour);
                if (chatFormat != null) fancy.style(chatFormat);
                i += ("%message%").length() - 1;
            } else {
                fancy.then(format.charAt(i));
                if (chatColour != null) fancy.color(chatColour);
                if (chatFormat != null) fancy.style(chatFormat);
            }
        }
        return fancy;
    }

    public FancyMessage formatEmote(Player talking, String message) {
        FancyMessage fancy = new FancyMessage("");
        String format = pluginConfig.getString("emotes.format");
        ChatColor chatColour = null;
        ChatColor chatFormat = null;
        for (int i = 0; i < format.length(); i++) {
            if (format.charAt(i) == '&') {
                ChatColor colourOrFormat = ChatColor.getByChar(format.charAt(i + 1));
                if (colourOrFormat.isColor()) chatColour = colourOrFormat;
                if (colourOrFormat.isFormat()) chatFormat = colourOrFormat;
                i += 1;
            } else if (format.substring(i, i + ("%channel%").length()).equalsIgnoreCase("%channel%")) {
                fancy.then("emote");
                if (chatColour != null) fancy.color(chatColour);
                if (chatFormat != null) fancy.style(chatFormat);
                i += ("%channel%").length() - 1;
            } else if (format.substring(i, i + ("%player%").length()).equalsIgnoreCase("%player%")) {
                fancy.then(talking.getDisplayName());
                fancy.tooltip(talking.getName());
                if (chatColour != null) fancy.color(chatColour);
                if (chatFormat != null) fancy.style(chatFormat);
                i += ("%player%").length() - 1;
            } else if (format.substring(i, i + ("%prefix%").length()).equalsIgnoreCase("%prefix%")) {
                fancy.then(getPlayerPrefix(talking));
                if (chatColour != null) fancy.color(chatColour);
                if (chatFormat != null) fancy.style(chatFormat);
                i += ("%prefix%").length() - 1;
            } else if (format.substring(i, i + ("%ign%").length()).equalsIgnoreCase("%ign%")) {
                fancy.then(talking.getName());
                fancy.tooltip(talking.getDisplayName());
                if (chatColour != null) fancy.color(chatColour);
                if (chatFormat != null) fancy.style(chatFormat);
                i += ("%ign%").length() - 1;
            } else if (format.substring(i, i + ("%message%").length()).equalsIgnoreCase("%message%")) {
                fancy.then(message.replace("*", ""));
                if (chatColour != null) fancy.color(chatColour);
                if (chatFormat != null) fancy.style(chatFormat);
                i += ("%message%").length() - 1;
            } else {
                fancy.then(format.charAt(i));
                if (chatColour != null) fancy.color(chatColour);
                if (chatFormat != null) fancy.style(chatFormat);
            }
        }
        return fancy;
    }

    private String drunkify(final Player player, String message) {
        Future<RegisteredServiceProvider<EssentialsPlugin>> futureEssentialsPluginProvider = Bukkit.getScheduler().callSyncMethod(plugin, new Callable<RegisteredServiceProvider<EssentialsPlugin>>(){
                    @Override
                    public RegisteredServiceProvider<EssentialsPlugin> call() {
                        return Bukkit.getServer().getServicesManager().getRegistration(EssentialsPlugin.class);
                    }
                }
        );
        RegisteredServiceProvider<EssentialsPlugin> essentialsPluginProvider = null;
        try {
            essentialsPluginProvider = futureEssentialsPluginProvider.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (essentialsPluginProvider != null) {
            final EssentialsPlugin essentialsPlugin = essentialsPluginProvider.getProvider();
            Future<Number> futureDrunkenness = Bukkit.getScheduler().callSyncMethod(plugin, new Callable<Number>(){
                        @Override
                        public Number call() {
                            return essentialsPlugin.getDrunkenness(player);
                        }
                    }
            );
            try {
                if (futureDrunkenness.get().intValue() >= 5) {
                    return message.replaceAll("s([^h])", "sh$1");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    private Location corelateUUIDtoLocation(UUID in){
        if(UUIDLocations.containsKey(in)){
            return UUIDLocations.get(in);
        }
        return null;
    }
}
