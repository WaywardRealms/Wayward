package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class CalculateDamageCommand implements CommandExecutor {

    private WaywardCombat plugin;

    public CalculateDamageCommand(WaywardCombat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            if (args.length > 0) {
                int armourRating;
                try {
                    armourRating = Integer.parseInt(args[0]);
                } catch (NumberFormatException exception) {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Event character armour rating must be an integer.");
                    return true;
                }
                plugin.setDamageCalculationContext((Player) sender, new DamageCalculationContext(characterPlugin.getActiveCharacter((Player) sender), armourRating));
                ((Player) sender).openInventory(plugin.getHandSelectionInventory("Hand [dc]"));
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify the armour rating of the event character you are attacking.");
            }
        }
        return true;
    }

}
