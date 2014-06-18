package net.wayward_realms.waywardchat;

import net.wayward_realms.waywardchat.irc.*;
import net.wayward_realms.waywardlib.chat.Channel;
import net.wayward_realms.waywardlib.chat.ChatPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.java.JavaPlugin;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.exception.IrcException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WaywardChat extends JavaPlugin implements ChatPlugin {

    private Map<String, Channel> channels = new ConcurrentHashMap<>();
    private Map<String, ChatGroup> chatGroups = new ConcurrentHashMap<>();
    private Map<UUID, ChatGroup> lastPrivateMessage = new ConcurrentHashMap<>();
    private Set<UUID> snooping = Collections.synchronizedSet(new HashSet<UUID>());

    private PircBotX ircBot;

    @Override
    public void onEnable() {
        registerListeners(new AsyncPlayerChatListener(this), new PlayerJoinListener(this), new PlayerQuitListener(this), new PlayerCommandPreprocessListener(this));
        getCommand("broadcast").setExecutor(new BroadcastCommand(this));
        getCommand("ch").setExecutor(new ChCommand(this));
        getCommand("chathelp").setExecutor(new ChatHelpCommand(this));
        getCommand("chatgroup").setExecutor(new ChatGroupCommand(this));
        getCommand("emotemode").setExecutor(new EmoteModeCommand(this));
        getCommand("message").setExecutor(new MessageCommand(this));
        getCommand("reply").setExecutor(new ReplyCommand(this));
        getCommand("snoop").setExecutor(new SnoopCommand(this));
        saveDefaultConfig();
        saveDefaultPrefixes();
        for (String section : getConfig().getConfigurationSection("channels").getKeys(false)) {
            ChannelImpl channel = new ChannelImpl(this, section.toLowerCase());
            channels.put(channel.getName(), channel);
            getCommand(section).setExecutor(new QuickChannelSwitchCommand(this));
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
        setupChatGroupDisposal();
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
        saveDefaultIrcConfig();
        File ircFile = new File(getDataFolder(), "irc.yml");
        YamlConfiguration ircConfig = new YamlConfiguration();
        try {
            ircConfig.load(ircFile);
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }
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
                .setAutoReconnect(true);
            getLogger().info("Setting up IRC bot:");
            if (ircConfig.get("name") != null) {
                String name = (String) ircConfig.get("name");
                configurationBuilder.setName(name);
                getLogger().info("Name set to " + name);
            }
            if (ircConfig.get("real-name") != null) {
                String realName = (String) ircConfig.get("real-name");
                configurationBuilder.setRealName(realName);
                getLogger().info("Real name set to " + realName);
            }
            if (ircConfig.get("login") != null) {
                String login = (String) ircConfig.get("login");
                configurationBuilder.setLogin(login);
                getLogger().info("Login set to " + login);
            }
            if (ircConfig.get("cap-enabled") != null) {
                boolean capEnabled = (boolean) ircConfig.get("cap-enabled");
                configurationBuilder.setCapEnabled(capEnabled);
                getLogger().info("CAP " + (capEnabled ? "enabled" : "disabled"));
            }
            if (ircConfig.get("auto-nick-change-enabled") != null) {
                boolean autoNickChange = (boolean) ircConfig.get("auto-nick-change-enabled");
                configurationBuilder.setAutoNickChange(autoNickChange);
                getLogger().info("Auto nick change " + (autoNickChange ? "enabled" : "disabled"));
            }
            if (ircConfig.get("auto-split-message-enabled") != null) {
                boolean autoSplitMessage = (boolean) ircConfig.get("auto-split-message-enabled");
                configurationBuilder.setAutoSplitMessage(autoSplitMessage);
                getLogger().info("Auto-split message " + (autoSplitMessage ? "enabled" : "disabled"));
            }
            configurationBuilder.setServerHostname(ircConfig.getString("server").contains(":") ? ircConfig.getString("server").split(":")[0] : ircConfig.getString("server"));
            getLogger().info("Hostname set to " + (ircConfig.getString("server").contains(":") ? ircConfig.getString("server").split(":")[0] : ircConfig.getString("server")));
            if (ircConfig.getString("server").contains(":")) {
                try {
                    configurationBuilder.setServerPort(Integer.parseInt(ircConfig.getString("server").split(":")[1]));
                    getLogger().info("Port set to " + ircConfig.getString("server").split(":")[1]);
                } catch (NumberFormatException ignore) {
                }
            }
            if (ircConfig.get("max-line-length") != null) {
                int maxLineLength = (int) ircConfig.get("max-line-length");
                configurationBuilder.setMaxLineLength(maxLineLength);
                getLogger().info("Max line length set to " + maxLineLength);
            }
            if (ircConfig.get("message-delay") != null) {
                long messageDelay = Long.parseLong("" + (int) ircConfig.get("message-delay"));
                configurationBuilder.setMessageDelay(messageDelay);
                getLogger().info("Message delay set to " + messageDelay);
            }
            if (ircConfig.get("password") != null) {
                String password = (String) ircConfig.get("password");
                configurationBuilder.setNickservPassword(password);
                getLogger().info("Password set. (" + password.length() + " characters, starting with " + password.charAt(0) + ", ending with " + password.charAt(password.length() - 1) + ")");
            }
            for (Channel channel : getChannels()) {
                if (channel.isIrcEnabled()) {
                    configurationBuilder.addAutoJoinChannel(channel.getIrcChannel());
                }
            }
            getLogger().info("Building configuration...");
            Configuration<PircBotX> configuration = configurationBuilder.buildConfiguration();
            ircBot = new PircBotX(configuration);
            getLogger().info("Done! The bot will now be started in a separate thread.");
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

    public void saveDefaultIrcConfig() {
        YamlConfiguration ircConfig = new YamlConfiguration();
        File ircFile = new File(getDataFolder(), "irc.yml");
        if (!ircFile.exists()) {
            ircConfig.set("server", "irc.esper.net:5555");
            ircConfig.set("name", "Wayward");
            ircConfig.set("real-name", "Wayward");
            ircConfig.set("login", "Wayward");
            ircConfig.set("cap-enabled", true);
            ircConfig.set("auto-nick-change-enabled", true);
            ircConfig.set("auto-split-message-enabled", false);
            ircConfig.set("max-line-length", null);
            ircConfig.set("message-delay", null);
            ircConfig.set("password", null);
            try {
                ircConfig.save(ircFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
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

    public ChatGroup getChatGroup(String name) {
        return chatGroups.get(name.toLowerCase());
    }

    public void removeChatGroup(String name) {
        chatGroups.remove(name.toLowerCase());
        for (Iterator<Map.Entry<String, ChatGroup>> iterator = chatGroups.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, ChatGroup> entry = iterator.next();
            if (entry.getValue().getName().equalsIgnoreCase(name)) iterator.remove();
        }
    }

    public void addChatGroup(ChatGroup chatGroup) {
        chatGroups.put(chatGroup.getName(), chatGroup);
    }

    public void sendPrivateMessage(Player sender, ChatGroup recipients, String message) {
        recipients.sendMessage(sender, message);
        for (UUID recipient : recipients.getPlayers()) {
            lastPrivateMessage.put(recipient, recipients);
        }
    }

    public Set<UUID> getSnooping() {
        return snooping;
    }

    public boolean isSnooping(OfflinePlayer player) {
        return snooping.contains(player.getUniqueId());
    }

    public void setSnooping(OfflinePlayer player, boolean snoop) {
        if (snoop) {
            snooping.add(player.getUniqueId());
        } else {
            snooping.remove(player.getUniqueId());
        }
    }

    public ChatGroup getLastPrivateMessage(Player player) {
        return lastPrivateMessage.get(player.getUniqueId());
    }

    public Collection<ChatGroup> getChatGroups() {
        return chatGroups.values();
    }

    public void setupChatGroupDisposal() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (ChatGroup chatGroup : chatGroups.values()) {
                    chatGroup.disposeIfUnused();
                }
            }
        }, 0L, 900000L);
    }

    public EmoteMode getEmoteMode(OfflinePlayer player) {
        File emoteModeFile = new File(getDataFolder(), "emote-mode.yml");
        YamlConfiguration emoteModeConfig = YamlConfiguration.loadConfiguration(emoteModeFile);
        if (emoteModeConfig.get(player.getName()) != null) return EmoteMode.valueOf(emoteModeConfig.getString(player.getName())); else return EmoteMode.TWO_ASTERISKS;
    }

    public void setEmoteMode(OfflinePlayer player, EmoteMode emoteMode) {
        File emoteModeFile = new File(getDataFolder(), "emote-mode.yml");
        YamlConfiguration emoteModeConfig = YamlConfiguration.loadConfiguration(emoteModeFile);
        emoteModeConfig.set(player.getName(), emoteMode.toString());
        try {
            emoteModeConfig.save(emoteModeFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
