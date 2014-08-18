package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.CombatPlugin;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WaywardCombat extends JavaPlugin implements CombatPlugin {

    private RollsManager rollsManager;

    private Set<Fight> fights = new HashSet<>();

    @Override
    public void onEnable() {
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
}
