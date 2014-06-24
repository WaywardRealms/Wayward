package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Turn;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Random;

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
                FightImpl fight = (FightImpl) plugin.getActiveFight(character);
                Turn turn = fight.getActiveTurn();
                if (((Character) turn.getAttacker()).getId() == character.getId()) {
                    boolean stopped = false;
                    Random random = new Random();
                    for (Character combatant : fight.getCharacters()) {
                        if (characterPlugin.getParty(combatant) == null || characterPlugin.getParty(character) == null || characterPlugin.getParty(combatant).getId() != characterPlugin.getParty(character).getId()) {
                            if (random.nextInt(combatant.getStatValue(Stat.SPEED)) > random.nextInt(character.getStatValue(Stat.SPEED))) {
                                stopped = true;
                                fight.sendMessage(ChatColor.RED + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + " attempted to flee, but was stopped by " + (combatant.isNameHidden() ? ChatColor.MAGIC + combatant.getName() + ChatColor.RESET : combatant.getName()));
                                break;
                            }
                        }
                    }
                    if (!stopped) {
                        fight.removeCombatant(character);
                        fight.sendMessage(ChatColor.YELLOW + (character.isNameHidden() ? ChatColor.MAGIC + character.getName() + ChatColor.RESET : character.getName()) + ChatColor.YELLOW + " fled.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "It is not your turn.");
                }
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
        }
        return true;
    }

}
