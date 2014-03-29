package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.combat.Fight;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class FleeCommand implements CommandExecutor {
	
	private WaywardCombat plugin;
	
	public FleeCommand(WaywardCombat plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			RegisteredServiceProvider<CharacterPlugin> characterProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
			if (characterProvider != null) {
				CharacterPlugin characterPlugin = characterProvider.getProvider();
				Character character = characterPlugin.getActiveCharacter(player);
				Fight fight = plugin.getActiveFight(character);
                fight.removeCombatant(character);
                fight.sendMessage(ChatColor.YELLOW + character.getName() + " fled.");
				sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Fled.");
			}
		}
		return true;
	}
	
}
