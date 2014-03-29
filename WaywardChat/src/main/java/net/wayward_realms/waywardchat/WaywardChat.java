package net.wayward_realms.waywardchat;

import net.wayward_realms.waywardchat.irc.*;
import net.wayward_realms.waywardlib.chat.Channel;
import net.wayward_realms.waywardlib.chat.ChatPlugin;
import net.wayward_realms.waywardlib.essentials.EssentialsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.exception.IrcException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WaywardChat extends JavaPlugin implements ChatPlugin {

    private Map<String, Channel> channels = new HashMap<>();

    private PircBotX ircBot;

    @Override
    public void onEnable() {
        registerListeners(new AsyncPlayerChatListener(this), new PlayerJoinListener(this), new PlayerQuitListener(this), new PlayerCommandPreprocessListener(this));
        getCommand("broadcast").setExecutor(new BroadcastCommand(this));
        getCommand("ch").setExecutor(new ChCommand(this));
        getCommand("chathelp").setExecutor(new ChatHelpCommand(this));
        saveDefaultConfig();
        saveDefaultPrefixes();
        for (String section : getConfig().getConfigurationSection("channels").getKeys(false)) {
            ChannelImpl channel = new ChannelImpl(this, section.toLowerCase());
            channels.put(channel.getName(), channel);
            channel.setCommand(new QuickChannelSwitchCommand(channel.getName(), "Allows you to talk in " + channel.getName() + " on-the-fly", Arrays.asList(channel.getName()), this, channel));
        }
        setupIrc();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            for (Channel channel : getChannels()) {
                if (player.hasPermission("wayward.chat.ch.listen." + channel.getName().toLowerCase())) {
                    channel.addListener(player);
                }
            }
            setPlayerChannel(player, getChannel(getConfig().getString("default-channel").toLowerCase()));
        }
        setupBroadcasts();
    }



    @Override
    public void onDisable() {
        if (ircBot != null) {
            ircBot.stopBotReconnect();
        }
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public String getPrefix() {
        //return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + getDescription().getName() + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
        return "";
    }

    @Override
    public void loadState() {

    }

    @Override
    public void saveState() {

    }

    @Override
    public Channel getPlayerChannel(Player player) {
        for (Channel channel : getChannels()) {
            if (channel.getSpeakers().contains(player)) {
                return channel;
            }
        }
        return getChannel(getConfig().getString("default-channel"));
    }

    public Channel getChannelFromIrcChannel(String ircChannel) {
        for (Channel channel : getChannels()) {
            if (channel.isIrcEnabled()) {
                if (channel.getIrcChannel().equals(ircChannel)) {
                    return channel;
                }
            }
        }
        return null;
    }

    public void handleChat(Player talking, String message) {
        if (!message.equals("")) {
            String format;
            if (message.startsWith("*") && message.endsWith("*")) {
                getChannel(getConfig().getString("default-channel")).log(talking.getName() + "/" + talking.getDisplayName() + ": " + message);
                for (Player player : new ArrayList<>(talking.getWorld().getPlayers())) {
                    if (getConfig().getInt("emotes.radius") >= 0) {
                        if (talking.getLocation().distance(player.getLocation()) <= getConfig().getInt("emotes.radius")) {
                            format = getConfig().getString("emotes.format").replace("%channel%", "emote").replace("%prefix%", getPlayerPrefix(talking)).replace("%player%", talking.getDisplayName()).replace("%ign%", talking.getName()).replace("&", ChatColor.COLOR_CHAR + "").replace("%message%", message.replace("*", ""));
                            player.sendMessage(format);
                        }
                    } else {
                        format = getConfig().getString("emotes.format").replace("%channel%", "emote").replace("%prefix%", getPlayerPrefix(talking)).replace("%player%", talking.getDisplayName()).replace("%ign%", talking.getName()).replace("&", ChatColor.COLOR_CHAR + "").replace("%message%", message.replace("*", ""));
                        player.sendMessage(format);
                    }
                }
            } else {
                if (getPlayerChannel(talking) != null) {
                    getPlayerChannel(talking).log(talking.getName() + "/" + talking.getDisplayName() + ": " + message);
                    for (Player player : new HashSet<>(getPlayerChannel(talking).getListeners())) {
                        if (player != null) {
                            if (getPlayerChannel(talking).getRadius() >= 0) {
                                if (talking.getWorld().equals(player.getWorld())) {
                                    if (talking.getLocation().distance(player.getLocation()) <= (double) getPlayerChannel(talking).getRadius()) {
                                        if (getPlayerChannel(talking).isGarbleEnabled()) {
                                            double distance = talking.getLocation().distance(player.getLocation());
                                            double clearRange = 0.75D * (double) getPlayerChannel(talking).getRadius();
                                            double hearingRange = (double) getPlayerChannel(talking).getRadius();
                                            double clarity = 1.0D - ((distance - clearRange) / hearingRange);
                                            format = getPlayerChannel(talking).getFormat().replace("%channel%", getPlayerChannel(talking).getName()).replace("%prefix%", getPlayerPrefix(talking)).replace("%player%", talking.getDisplayName()).replace("%ign%", talking.getName()).replace("&", ChatColor.COLOR_CHAR + "").replace("%message%", garbleMessage(drunkify(player, message), clarity));
                                        } else {
                                            format = getPlayerChannel(talking).getFormat().replace("%channel%", getPlayerChannel(talking).getName()).replace("%prefix%", getPlayerPrefix(talking)).replace("%player%", talking.getDisplayName()).replace("%ign%", talking.getName()).replace("&", ChatColor.COLOR_CHAR + "").replace("%message%", message);
                                        }
                                        player.sendMessage(format);
                                    }
                                }
                            } else {
                                format = getPlayerChannel(talking).getFormat().replace("%channel%", getPlayerChannel(talking).getName()).replace("%prefix%", getPlayerPrefix(talking)).replace("%player%", talking.getDisplayName()).replace("%ign%", talking.getName()).replace("&", ChatColor.COLOR_CHAR + "").replace("%message%", message);
                                player.sendMessage(format);
                            }
                        }
                    }
                    if (getPlayerChannel(talking).isIrcEnabled()) {
                        format = getPlayerChannel(talking).getFormat().replace("%channel%", getPlayerChannel(talking).getName()).replace("%prefix%", getPlayerPrefix(talking)).replace("%player%", talking.getDisplayName()).replace("%ign%", talking.getName()).replace("&", ChatColor.COLOR_CHAR + "").replace("%message%", message);
                        getIrcBot().sendIRC().message(getPlayerChannel(talking).getIrcChannel(), ChatColor.stripColor(format));
                    }
                } else {
                    talking.sendMessage(getPrefix() + ChatColor.RED + "You must talk in a channel! Use /chathelp for help.");
                }
            }
        }
    }

    public void handleChat(User talking, org.pircbotx.Channel ircChannel, String message) {
        String format;
        Channel channel = getChannelFromIrcChannel(ircChannel.getName());
        channel.log("(irc)" + talking.getRealName() + "/" + talking.getNick() + ": " + message);
        for (Player player : new HashSet<>(channel.getListeners())) {
            if (player != null) {
                format = channel.getFormat().replace("%channel%", channel.getName()).replace("%player%", talking.getNick()).replace("%ign%", talking.getNick()).replace("&", ChatColor.COLOR_CHAR + "").replace("%message%", message);
                player.sendMessage(format);
            }
        }
    }

    private String drunkify(Player player, String message) {
        RegisteredServiceProvider<EssentialsPlugin> essentialsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(EssentialsPlugin.class);
        if (essentialsPluginProvider != null) {
            EssentialsPlugin essentialsPlugin = essentialsPluginProvider.getProvider();
            if (essentialsPlugin.getDrunkenness(player) >= 5) {
                return message.replaceAll("s([^h])", "sh$1");
            }
        }
        return message;
    }

    private String garbleMessage(String message, double clarity) {
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

    public String getPlayerPrefix(Permissible player) {
        YamlConfiguration prefixConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "prefixes.yml"));
        for (String key : prefixConfig.getKeys(false)) {
            if (player.hasPermission("wayward.chat.prefix." + key)) {
                return ChatColor.translateAlternateColorCodes('&', prefixConfig.getString(key));
            }
        }
        return "";
    }

    public void saveDefaultPrefixes() {
        YamlConfiguration prefixConfig = new YamlConfiguration();
        prefixConfig.set("admin", " &e[admin] ");
        try {
            prefixConfig.save(new File(getDataFolder(), "prefixes.yml"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Collection<Channel> getChannels() {
        return channels.values();
    }

    @Override
    public void addChannel(Channel channel) {
        channels.put(channel.getName(), channel);
    }

    @Override
    public void removeChannel(Channel channel) {
        channels.remove(channel.getName());
    }

    @Override
    public void setPlayerChannel(Player player, Channel channel) {
        for (Channel channel1 : getChannels()) {
            if (channel1.getSpeakers().contains(player)) {
                channel1.removeSpeaker(player);
            }
        }
        channel.addListener(player);
        channel.addSpeaker(player);
    }

    @Override
    public Channel getChannel(String name) {
        return channels.get(name);
    }

    public void setupIrc() {
        if (getConfig().getBoolean("irc.enabled")) {
            Configuration.Builder<PircBotX> configurationBuilder = new Configuration.Builder<>()
                .setName(getConfig().getString("irc.bot-name"))
                .setLogin(getConfig().getString("irc.bot-name"))
                .setAutoNickChange(true)
                .setCapEnabled(true)
                .addListener(new IrcBroadcastCommand(this))
                .addListener(new IrcChCommand(this))
                .addListener(new IrcChatHelpCommand(this))
                .addListener(new IrcListCommand(this))
                .addListener(new IrcMessageListener(this))
                .setServerHostname(getConfig().getString("irc.server").contains(":") ? getConfig().getString("irc.server").split(":")[0] : getConfig().getString("irc.server"))
                .setAutoReconnect(true);
            for (Channel channel : getChannels()) {
                if (channel.isIrcEnabled()) {
                    configurationBuilder.addAutoJoinChannel(channel.getIrcChannel());
                }
            }
            Configuration<PircBotX> configuration = configurationBuilder.buildConfiguration();
            ircBot = new PircBotX(configuration);
            getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
                @Override
                public void run() {
                    try {
                        ircBot.startBot();
                    } catch (IOException | IrcException exception) {
                        exception.printStackTrace();
                    }
                }
            });
        }
    }

    public PircBotX getIrcBot() {
        return ircBot;
    }

    private void setupBroadcasts() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                String format;
                Random random = new Random();
                for (Channel channel : getChannels()) {
                    if (getConfig().getBoolean("channels." + channel.getName() + ".broadcasts.enabled")){
                        List<String> possibleBroadcasts = getConfig().getStringList("channels." + channel.getName() + ".broadcasts.possible");
                        String message = possibleBroadcasts.get(random.nextInt(possibleBroadcasts.size()));
                        for (Player player : new HashSet<>(channel.getListeners())) {
                            if (player != null) {
                                format = channel.getFormat().replace("%channel%", channel.getName()).replace("%prefix%", "").replace("%player%", "").replace("%ign%", "").replace("&", ChatColor.COLOR_CHAR + "").replace("%message%", message);
                                player.sendMessage(format);
                            }
                        }
                        if (channel.isIrcEnabled()) {
                            format = channel.getFormat().replace("%channel%", channel.getName()).replace("%prefix%", "").replace("%player%", "").replace("%ign%", "").replace("&", ChatColor.COLOR_CHAR + "").replace("%message%", message);
                            getIrcBot().sendIRC().message(channel.getIrcChannel(), ChatColor.stripColor(format));
                        }
                    }
                }
            }
        }, getConfig().getInt("broadcasts.delay") * 20, getConfig().getInt("broadcasts.delay") * 20);
    }

}
