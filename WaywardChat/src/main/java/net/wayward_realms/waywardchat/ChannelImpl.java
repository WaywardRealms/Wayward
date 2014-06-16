package net.wayward_realms.waywardchat;

import net.wayward_realms.waywardlib.chat.Channel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChannelImpl implements Channel {

    private WaywardChat plugin;
    private String name;
    private ChatColor colour;
    private String format;
    private Set<UUID> speakers = Collections.synchronizedSet(new HashSet<UUID>());
    private Set<UUID> listeners = Collections.synchronizedSet(new HashSet<UUID>());
    private int radius;
    private boolean garbleEnabled;
    private boolean ircEnabled;
    private Command command;
    private String ircChannel;

    public ChannelImpl(WaywardChat plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.colour = ChatColor.valueOf(plugin.getConfig().getString("channels." + this.getName() + ".colour").toUpperCase());
        this.format = plugin.getConfig().getString("channels." + getName() + ".format");
        this.speakers = new HashSet<>();
        this.listeners = new HashSet<>();
        this.radius = plugin.getConfig().getInt("channels." + getName() + ".radius");
        this.garbleEnabled = plugin.getConfig().getBoolean("channels." + getName() + ".garble-enabled") && this.radius >= 0;
        this.ircEnabled = plugin.getConfig().getBoolean("irc.enabled") && plugin.getConfig().getBoolean("channels." + getName() + ".irc.enabled");
        if (isIrcEnabled()) {
            this.ircChannel = plugin.getConfig().getString("channels." + getName() + ".irc.channel");
        }
    }

    @Override
    public ChatColor getColour() {
        return colour;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public Set<Player> getListeners() {
        Set<Player> listeners = new HashSet<>();
        for (UUID listener : this.listeners) {
            listeners.add(Bukkit.getOfflinePlayer(listener).getPlayer());
        }
        return listeners;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getRadius() {
        return radius;
    }

    @Override
    public Collection<Player> getSpeakers() {
        Set<Player> speakers = new HashSet<>();
        for (UUID speaker : this.speakers) {
            speakers.add(Bukkit.getOfflinePlayer(speaker).getPlayer());
        }
        return speakers;
    }

    @Override
    public void setColour(ChatColor colour) {
        this.colour = colour;
    }

    @Override
    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public boolean isGarbleEnabled() {
        return garbleEnabled;
    }

    @Override
    public void setGarbleEnabled(boolean garbleEnabled) {
        this.garbleEnabled = garbleEnabled;
    }

    public boolean isIrcEnabled() {
        return ircEnabled;
    }

    public void setIrcEnabled(boolean enabled) {
        ircEnabled = enabled;
    }

    @Override
    public void addListener(Player listener) {
        listeners.add(listener.getUniqueId());
    }

    @Override
    public void addSpeaker(Player speaker) {
        speakers.add(speaker.getUniqueId());
    }

    @Override
    public void removeListener(Player listener) {
        listeners.remove(listener.getUniqueId());
    }

    @Override
    public void removeSpeaker(Player speaker) {
        speakers.remove(speaker.getUniqueId());
    }

    @Override
    public void setCommand(Command command) {
        this.command = command;
    }

    @Override
    public Command getCommand() {
        return command;
    }

    @Override
    public void log(String message) {
        File logDirectory = new File(plugin.getDataFolder(), "logs");
        DateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        File datedLogDirectory = new File(logDirectory, logDateFormat.format(new Date()));
        if (!datedLogDirectory.exists()) {
            datedLogDirectory.mkdirs();
        }
        File log = new File(datedLogDirectory, "chat-" + this.getName() + ".log");
        if (!log.exists()) {
            try {
                log.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(log, true));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            writer.append("[").append(dateFormat.format(new Date())).append("] ").append(message).append("\n");
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public String getIrcChannel() {
        return ircChannel;
    }

    public void setIrcChannel(String ircChannel) {
        this.ircChannel = ircChannel;
    }

}
