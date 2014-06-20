package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.Party;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PartyImpl implements Party {

    private static int nextId = 0;

    public static int nextAvailableId() {
        nextId++;
        return nextId;
    }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int id) {
        PartyImpl.nextId = id;
    }

    private WaywardCharacters plugin;
    private File file;

    public PartyImpl(WaywardCharacters plugin, Character... characters) {
        this.plugin = plugin;
        File partyDirectory = new File(plugin.getDataFolder(), "parties");
        this.file = new File(partyDirectory, nextAvailableId() + ".yml");
        setCreationDate(new Date());
        for (Character character : characters) addMember(character);
    }

    public PartyImpl(WaywardCharacters plugin, Party toCopy) {
        this.plugin = plugin;
        File partyDirectory = new File(plugin.getDataFolder(), "parties");
        this.file = new File(partyDirectory, nextAvailableId() + ".yml");
        setCreationDate(new Date());
        for (Character character : toCopy.getMembers()) addMember(character);
        for (Character character : toCopy.getInvitees()) invite(character);
    }

    public PartyImpl(WaywardCharacters plugin, File file) {
        this.plugin = plugin;
        this.file = file;
    }

    public int getId() {
        return Integer.parseInt(file.getName().replace(".yml", ""));
    }

    @Override
    public Collection<? extends Character> getMembers() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        Set<Character> characters = new HashSet<>();
        for (int cid : config.getIntegerList("members")) {
            characters.add(plugin.getCharacter(cid));
        }
        return characters;
    }

    @Override
    public void addMember(Character character) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<Integer> cidList = config.getIntegerList("members");
        cidList.add(character.getId());
        config.set("members", cidList);
        List<Integer> inviteeCidList = config.getIntegerList("invitees");
        if (inviteeCidList.contains(character.getId())) inviteeCidList.remove((Integer) character.getId());
        config.set("invitees", inviteeCidList);
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void removeMember(Character character) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<Integer> cidList = config.getIntegerList("members");
        cidList.remove((Integer) character.getId());
        config.set("members", cidList);
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Collection<? extends Character> getInvitees() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        Set<Character> characters = new HashSet<>();
        for (int cid : config.getIntegerList("invitees")) {
            characters.add(plugin.getCharacter(cid));
        }
        return characters;
    }

    @Override
    public void invite(Character character) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<Integer> cidList = config.getIntegerList("invitees");
        cidList.add(character.getId());
        config.set("invitees", cidList);
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void uninvite(Character character) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<Integer> cidList = config.getIntegerList("invitees");
        if (cidList.contains(character.getId())) cidList.remove((Integer) character.getId());
        config.set("invitees", cidList);
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Date getCreationDate() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        return (Date) config.get("creation-date");
    }

    @Override
    public void sendMessage(String message) {
        for (Character member : getMembers()) {
            OfflinePlayer player = member.getPlayer();
            if (player.isOnline()) player.getPlayer().sendMessage(message);
        }
    }

    private void setCreationDate(Date date) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("creation-date", date);
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
