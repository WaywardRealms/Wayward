package net.wayward_realms.waywardlib.character;

import java.util.Collection;
import java.util.Date;

/**
 * Represents a party of characters
 */
public interface Party {

    /**
     * Gets the party's unique ID
     *
     * @return the party's ID
     */
    public int getId();

    /**
     * Gets characters in the party
     *
     * @return the characters
     */
    public Collection<? extends Character> getMembers();

    /**
     * Adds a character to the party
     *
     * @param character the character to add
     */
    public void addMember(Character character);

    /**
     * Removes a character from the party
     *
     * @param character the character to remove
     */
    public void removeMember(Character character);

    /**
     * Gets characters invited to the party
     *
     * @return the characters who have recieved an invitation to this party
     */
    public Collection<? extends Character> getInvitees();

    /**
     * Invites a character to the party
     *
     * @param character the character to invite
     */
    public void invite(Character character);

    /**
     * Uninvites a character from the party
     *
     * @param character the character to uninvite
     */
    public void uninvite(Character character);

    /**
     * Gets the creation date of the party
     *
     * @return the date the party was created
     */
    public Date getCreationDate();

    /**
     * Sends a message to the party
     *
     * @param message the message to send
     */
    public void sendMessage(String message);

}
