package net.wayward_realms.waywardchat;

import mkremins.fanciful.FancyMessage;
import net.wayward_realms.waywardlib.chat.Channel;
import net.wayward_realms.waywardlib.essentials.EssentialsPlugin;
import net.wayward_realms.waywardlib.util.math.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
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
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AsyncPlayerChatListener implements Listener {

    private WaywardChat plugin;
    private RegisteredServiceProvider<EssentialsPlugin> essentialsPluginProvider;
    private YamlConfiguration pluginConfig;
    private YamlConfiguration emoteModeConfig;
    private YamlConfiguration prefixConfig;
    private ConcurrentHashMap<UUID, Location> uuidLocations = new ConcurrentHashMap<>();

    public AsyncPlayerChatListener(WaywardChat plugin) {
        this.plugin = plugin;
        this.pluginConfig = (YamlConfiguration)plugin.getConfig();
        emoteModeConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "emote-mode.yml"));
        prefixConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "prefixes.yml"));
        prefixConfig.set("admin", " &e[admin] ");
        essentialsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(EssentialsPlugin.class);
        // SET UP FUCKING PLAYER LOCATION GETTER SHIT COLLECTION
        Bukkit.getScheduler().runTaskTimer(
                plugin,
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        for(Player player: Bukkit.getServer().getOnlinePlayers()) {
                            uuidLocations.put(player.getUniqueId(), player.getLocation());
                        }
                    }
                },100L,100L);
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
                int emoteRadius = pluginConfig.getInt("emotes.radius");
                plugin.getChannel(pluginConfig.getString("default-channel")).log(talking.getName() + "/" + talking.getDisplayName() + ": " + message);
                for (UUID uuid : uuidLocations.keySet()) {
                    Player player = plugin.getServer().getPlayer(uuid); // I hope this is threadsafe...
                    if (player != null) {
                        Location talkingLocation = correlateUUIDtoLocation(talking.getUniqueId());
                        Location playerLocation = correlateUUIDtoLocation(uuid);
                        if (emoteRadius < 0
                                || (talkingLocation != null
                                && playerLocation != null
                                && talkingLocation.getWorld() == playerLocation.getWorld()
                                && talkingLocation.distanceSquared(playerLocation) <= (double) (emoteRadius * emoteRadius))) {
                            formatEmote(talking, message).send(player);
                        }
                    }
                }
            } else {
                final Channel channel = plugin.getPlayerChannel(talking);
                if (channel != null) {
                    channel.log(talking.getName() + "/" + talking.getDisplayName() + ": " + message);
                    synchronized (channel.getListeners()) {
                        for (Player player : new HashSet<>(channel.getListeners())) {
                            if (player != null) {
                                int radius = channel.getRadius();
                                if (radius >= 0) {
                                    Location talkingLocation = correlateUUIDtoLocation(talking.getUniqueId());
                                    Location playerLocation = correlateUUIDtoLocation(player.getUniqueId());
                                    if (talkingLocation != null && playerLocation != null) {
                                        if (talkingLocation.getWorld() == playerLocation.getWorld()) {
                                            if (correlateUUIDtoLocation(talking.getUniqueId()).distanceSquared(correlateUUIDtoLocation(player.getUniqueId())) <= (double) (radius * radius)) {
                                                FancyMessage fancy = formatChannel(channel, talking, player, message);
                                                fancy.send(player);
                                            }
                                        }
                                    }
                                } else {
                                    formatChannel(channel, talking, player, message).send(player);
                                }
                            }
                        }
                    }
                    if (channel.isIrcEnabled()) {
                        format = channel.getFormat()
                                .replace("%channel%", channel.getName())
                                .replace("%prefix%", getPlayerPrefix(talking))
                                .replace("%player%", talking.getDisplayName())
                                .replace("%ign%", talking.getName())
                                .replace("&", ChatColor.COLOR_CHAR + "")
                                .replace("%message%", message);
                        //SCHEDULE TASK TO PASS TO IRCBOT
                        Bukkit.getScheduler().runTask(plugin, new Runnable() {
                            public void run() {
                                plugin.getIrcBot().sendIRC().message(channel.getIrcChannel(), ChatColor.stripColor(format));
                            }
                        });
                        //TASK FUCKING PASSED
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

    public String getPlayerPrefix(final Permissible player) {
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
                        double distance = MathUtils.fastsqrt(correlateUUIDtoLocation(talking.getUniqueId()).distanceSquared(correlateUUIDtoLocation(recipient.getUniqueId())));
                        double clearRange = 0.75D * (double) channel.getRadius();
                        double hearingRange = (double) channel.getRadius();
                        double clarity = 1.0D - ((distance - clearRange) / hearingRange);
                        String garbledMessage = garbleMessage(drunkify(talking, message), clarity);
                        fancy.then(garbledMessage);
                    }
                } else {
                    String urlRegex = "(\\w+://)?\\w+(\\.\\w+)+(/\\S*)?/?";
                    Matcher matcher = Pattern.compile(urlRegex).matcher(message);
                    int index = 0;
                    int startIndex;
                    int endIndex = 0;
                    while (matcher.find()) {
                        startIndex = matcher.start();
                        endIndex = matcher.end();
                        if (startIndex > index) {
                            fancy.then(message.substring(index, startIndex));
                        }
                        String link = message.substring(startIndex, endIndex);
                        if (!link.contains("://")) link = "http://" + link;
                        fancy.then("[link]")
                                .color(ChatColor.BLUE)
                                .style(ChatColor.UNDERLINE)
                                .link(link)
                                .tooltip(link);
                    }
                    if (endIndex < message.length() - 1) {
                        fancy.then(message.substring(endIndex, message.length()));
                        if (chatColour != null) fancy.color(chatColour);
                        if (chatFormat != null) fancy.style(chatFormat);
                    }
                }
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
        if (essentialsPluginProvider != null) {
            EssentialsPlugin essentialsPlugin = essentialsPluginProvider.getProvider();
            if (essentialsPlugin.getDrunkenness(player) >= 5) {
                return message.replaceAll("s([^h])", "sh$1");
            }
        }
        return message;
    }

    public String garbleMessage(String message, double clarity) {
        StringBuilder newMessage = new StringBuilder();
        Random random = new Random();
        int i = 0;
        int drops = 0;
        while (i < message.length()) {
            int c = message.codePointAt(i);
            i += Character.charCount(c);
            if (random.nextDouble() < clarity) {
                newMessage.appendCodePoint(c);
            } else if (random.nextDouble() < 0.1D) {
                newMessage.append(ChatColor.DARK_GRAY);
                newMessage.appendCodePoint(c);
                newMessage.append(ChatColor.WHITE);
            } else {
                newMessage.append(' ');
                drops++;
            }
        }
        if (drops == message.length()) {
            return "~~~";
        }
        return newMessage.toString();
    }

    private Location correlateUUIDtoLocation(UUID in){
        if(uuidLocations.containsKey(in)){
            return uuidLocations.get(in);
        }
        return null;
    }
}
