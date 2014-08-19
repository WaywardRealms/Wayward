package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class RollCommand implements CommandExecutor {

    private WaywardCombat plugin;

    public RollCommand(WaywardCombat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    Character character = characterPlugin.getActiveCharacter((Player) sender);
                    RollContext context = new RollContext(character, args[0].equalsIgnoreCase("attack") || args[0].equalsIgnoreCase("a"));
                    plugin.setRollContext((Player) sender, context);
                    ((Player) sender).openInventory(plugin.getHandSelectionInventory());
                }
            } else if (args.length > 0) {
                plugin.getRollsManager().roll((Player) sender, args[0]);
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a roll string.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
        }
        return true;
    }

}
