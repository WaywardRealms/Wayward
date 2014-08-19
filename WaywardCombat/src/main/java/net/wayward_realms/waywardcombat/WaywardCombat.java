package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.CombatPlugin;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WaywardCombat extends JavaPlugin implements CombatPlugin {

    private RollsManager rollsManager;

    private Set<Fight> fights = new HashSet<>();

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(RollContext.class);
        saveDefaultConfig();
        rollsManager = new RollsManager(this);
        registerListeners(new InventoryClickListener(this));
        //getCommand("fight").setExecutor(new FightCommand(this));
        //getCommand("turn").setExecutor(new TurnCommand(this));
        //getCommand("flee").setExecutor(new FleeCommand(this));
        getCommand("rolls").setExecutor(new RollsCommand(this));
        getCommand("roll").setExecutor(new RollCommand(this));
        getCommand("damage").setExecutor(new DamageCommand(this));
        getCommand("calculatedamage").setExecutor(new CalculateDamageCommand(this));
    }

    @Override
    public String getPrefix() {
        //return "" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "|" + ChatColor.RESET + ChatColor.BLUE + "WaywardCombat" + ChatColor.DARK_GRAY + ChatColor.MAGIC + "| " + ChatColor.RESET;
        return "";
    }

    @Override
    public void loadState() {

    }

    @Override
    public void saveState() {

    }

    @Override
    public Collection<Fight> getActiveFights() {
        return fights;
    }

    public Fight getActiveFight(Character character) {
        for (Fight fight : fights) {
            if (fight != null) {
                if (fight.getCombatants() != null) {
                    for (Character character1 : fight.getCharacters()) {
                        if (character1.getId() == character.getId()) return fight;
                    }
                }
            }
        }
        return null;
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public void addFight(Fight fight) {
        fights.add(fight);
    }

    @Override
    public void removeFight(Fight fight) {
        fights.remove(fight);
    }

    @Override
    public Fight getActiveFight(Combatant combatant) {
        return combatant instanceof Character ? getActiveFight((Character) combatant) : null;
    }

    public RollsManager getRollsManager() {
        return rollsManager;
    }

    public Inventory getHandSelectionInventory() {
        Inventory handInventory = getServer().createInventory(null, 9, "Hand");
        ItemStack onHandItem = new ItemStack(Material.WOOL);
        ItemMeta onHandMeta = onHandItem.getItemMeta();
        onHandMeta.setDisplayName("Onhand");
        onHandItem.setItemMeta(onHandMeta);
        handInventory.setItem(0, onHandItem);
        ItemStack offHandItem = new ItemStack(Material.WOOL);
        ItemMeta offHandMeta = offHandItem.getItemMeta();
        offHandMeta.setDisplayName("Offhand");
        offHandItem.setItemMeta(offHandMeta);
        handInventory.setItem(8, offHandItem);
        return handInventory;
    }

    public Inventory getSpecialisationInventory(Specialisation root, Character character) {
        RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
        if (skillsPluginProvider != null) {
            SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
            Inventory inventory = getServer().createInventory(null, 45, "Specialisation");
            ItemMeta meta;
            List<String> lore;
            int i = 0;
            for (Specialisation parent : root.getParentSpecialisations()) {
                ItemStack parentItem = new ItemStack(Material.WOOL);
                meta = parentItem.getItemMeta();
                meta.setDisplayName(parent.getName());
                lore = new ArrayList<>();
                lore.add("Points: " + skillsPlugin.getSpecialisationValue(character, parent));
                meta.setLore(lore);
                parentItem.setItemMeta(meta);
                inventory.setItem(i, parentItem);
                i++;
            }
            ItemStack rootItem = new ItemStack(Material.WOOL, 1, (short) 5);
            meta = rootItem.getItemMeta();
            meta.setDisplayName(root.getName());
            lore = new ArrayList<>();
            lore.add("Points: " + skillsPlugin.getSpecialisationValue(character, root));
            meta.setLore(lore);
            rootItem.setItemMeta(meta);
            inventory.setItem(22, rootItem);
            i = 27;
            for (Specialisation child : root.getChildSpecialisations()) {
                ItemStack childItem = new ItemStack(Material.WOOL);
                meta = childItem.getItemMeta();
                meta.setDisplayName(child.getName());
                lore = new ArrayList<>();
                lore.add("Points: " + skillsPlugin.getSpecialisationValue(character, child));
                meta.setLore(lore);
                childItem.setItemMeta(meta);
                inventory.setItem(i, childItem);
                i++;
            }
            return inventory;
        }
        return null;
    }

    @Override
    public int roll(Player player, String rollString) {
        return getRollsManager().roll(player, rollString);
    }

    public RollContext getRollContext(OfflinePlayer player) {
        return (RollContext) YamlConfiguration.loadConfiguration(new File(new File(getDataFolder(), "roll-contexts"), player.getUniqueId().toString() + ".yml")).get("context");
    }

    public void setRollContext(OfflinePlayer player, RollContext context) {
        File contextDirectory = new File(getDataFolder(), "roll-contexts");
        File contextFile = new File(contextDirectory, player.getUniqueId().toString() + ".yml");
        if (context != null) {
            YamlConfiguration contextConfiguration = YamlConfiguration.loadConfiguration(contextFile);
            contextConfiguration.set("context", context);
            try {
                contextConfiguration.save(contextFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            contextFile.delete();
        }
    }

}
