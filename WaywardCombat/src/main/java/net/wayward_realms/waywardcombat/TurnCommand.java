package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.combat.Turn;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TurnCommand implements CommandExecutor {
	
	private WaywardCombat plugin;
	
	public TurnCommand(WaywardCombat plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			CharacterPlugin characterPlugin = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class).getProvider();
			Character character = characterPlugin.getActiveCharacter(player);
			if (plugin.getActiveFight(character) != null) {
				FightImpl fight = (FightImpl) plugin.getActiveFight(character);
                if (fight.isActive()) {
                    Turn turn = fight.getActiveTurn();
                    if (args.length > 0) {
                        if (turn.getAttacker() == character) {
                            if (args[0].equalsIgnoreCase("skill")) {
                                fight.showTurnOptions(player);
                            } else if (args[0].equalsIgnoreCase("target")) {
                                fight.showCharacterOptions(player);
                            } else if (args[0].equalsIgnoreCase("weapon")) {
                                fight.showWeaponOptions(player);
                            } else if (args[0].equalsIgnoreCase("complete")) {
                                if (turn.getSkill() != null && turn.getDefender() != null && turn.getWeapon() != null) {
                                    fight.doTurn(turn);
                                } else {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "There are still some options you must set before completing your turn.");
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /turn [skill|target|weapon|complete]");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "It's not your turn!");
                        }
                    } else {
                        sender.sendMessage(new String[] {plugin.getPrefix() + ChatColor.GREEN + "Current turn:",
                                (turn.getSkill() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Skill type: " + (turn.getSkill() != null ? ChatColor.GREEN + turn.getSkill().getType().toString() : ChatColor.RED + "NOT CHOSEN - use /turn skill to choose"),
                                (turn.getSkill() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Skill: " + (turn.getSkill() != null ? ChatColor.GREEN + turn.getSkill().getName() : ChatColor.RED + "NOT CHOSEN - use /turn skill to choose"),
                                (turn.getDefender() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Target: " + (turn.getDefender() != null ? ChatColor.GREEN + turn.getDefender().getName() + " (" + ((Character) turn.getDefender()).getPlayer().getName() + "'s character)" : ChatColor.RED + "NOT CHOSEN - use /turn target to choose"),
                                (turn.getWeapon() != null ? ChatColor.GREEN + "\u2611" : ChatColor.RED + "\u2612") + ChatColor.GRAY + "Weapon: " + (turn.getWeapon() != null ? ChatColor.GREEN + turn.getWeapon().getType().toString() : ChatColor.RED + "NOT CHOSEN - use /turn weapon to choose"),
                                (turn.getSkill() != null && turn.getDefender() != null && turn.getWeapon() != null ? ChatColor.GREEN + "Ready to make a move! Use /turn complete to complete your turn." : ChatColor.RED + "There are still some options you must set before completing your turn.")});
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be in a fight to view the current turn.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be in a fight to view the current turn.");
            }
		} else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
        }
		return true;
	}
	
}
