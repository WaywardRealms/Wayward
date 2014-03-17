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

	private Set<Fight> fights = new HashSet<>();

	@Override
	public void onEnable() {
		registerListeners(new InventoryClickListener(this));
		getCommand("fight").setExecutor(new FightCommand(this));
		getCommand("turn").setExecutor(new TurnCommand(this));
		getCommand("flee").setExecutor(new FleeCommand(this));
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
					if (fight.getCombatants().contains(character)) {
						return fight;
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

}
