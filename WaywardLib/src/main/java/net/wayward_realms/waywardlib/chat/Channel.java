package net.wayward_realms.waywardlib.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Represents a chat channel
 *
 */
public interface Channel {

    /**
     * Gets the name of the channel
     *
     * @return the name of the channel
     */
    public String getName();

    /**
     * Sets the name of the channel
     *
     * @param name the name to set
     */
    public void setName(String name);

    /**
     * Gets the colour of the channel
     *
     * @return the colour associated with the channel
     */
    public ChatColor getColour();

    /**
     * Sets the colour of the channel
     *
     * @param colour the colour to set
     */
    public void setColour(ChatColor colour);

    /**
     * Gets the format messages in this channel appear in
     *
     * @return a format string, containing the placeholders %channel% (channel name), %player% (the display name of the player talking), and %ign% (the in-game name of the player talking)
     */
    public String getFormat();

    /**
     * Sets the format messages in this channel appear in
     *
     * @param format the format to set, containing the placeholders %channel% (channel name), %player% (the display name of the player talking), and %ign% (the in-game name of the player talking)
     */
    public void setFormat(String format);

    /**
     * Gets the radius the messages in this channel are sent to
     *
     * @return the radius messages are sent to in blocks, or -1 if the messages are sent globally
     */
    public int getRadius();

    /**
     * Sets the radius messages in this channel are sent to
     *
     * @param radius the radius to set in blocks, or -1 if messages should be sent globally
     */
    public void setRadius(int radius);

    /**
     * Gets a collection of the players speaking in the channel
     *
     * @return a collection of the players speaking in the channel
     */
    public Collection<Player> getSpeakers();

    /**
     * Adds a speaker to the channel
     *
     * @param speaker the speaker to add
     */
    public void addSpeaker(Player speaker);

    /**
     * Removes a speaker from the channel
     *
     * @param speaker the speaker to remove
     */
    public void removeSpeaker(Player speaker);

    /**
     * Gets a collection of the players listening to the channel
     *
     * @return a collection of the players listening to the channel
     */
    public Collection<Player> getListeners();

    /**
     * Adds a listener to the channel
     *
     * @param listener the listener to add
     */
    public void addListener(Player listener);

    /**
     * Removes a listener from the channel
     *
     * @param listener the listener to remove
     */
    public void removeListener(Player listener);

    /**
     * Gets whether garble is enabled for this channel
     *
     * @return true if garble is enabled, otherwise false
     */
    public boolean isGarbleEnabled();

    /**
     * Sets whether garble is enabled in this channel
     *
     * @param enable whether to enable garble in this channel
     */
    public void setGarbleEnabled(boolean enable);

    /**
     * Gets whether IRC is enabled in this channel
     *
     * @return true if IRC is enabled, otherwise false
     */
    public boolean isIrcEnabled();

    /**
     * Sets whether IRC is enabled in this channel
     *
     * @param enable whether to enable IRC in this channel
     */
    public void setIrcEnabled(boolean enable);

    /**
     * Gets the IRC channel this channel links with
     *
     * @return the IRC channel
     */
    public String getIrcChannel();

    /**
     * Sets the IRC channel this channel links with
     *
     * @param channel the channel to link with
     */
    public void setIrcChannel(String channel);

    /**
     * Gets the channel's command
     *
     * @return the channel's command
     */
    public Command getCommand();

    /**
     * Sets the channel's command
     *
     * @param command the command to set
     */
    public void setCommand(Command command);

    /**
     * Writes a message to the channel's log file
     *
     * @param message the message to add
     */
    public void log(String message);

}
