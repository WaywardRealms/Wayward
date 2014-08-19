package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class DamageCommand implements CommandExecutor {

    private WaywardCombat plugin;

    public DamageCommand(WaywardCombat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            if (args.length > 0) {
                Player defendingPlayer = plugin.getServer().getPlayer(args[0]);
                plugin.setDamageContext((Player) sender, new DamageContext(characterPlugin.getActiveCharacter((Player) sender), characterPlugin.getActiveCharacter(defendingPlayer)));
                ((Player) sender).openInventory(plugin.getHandSelectionInventory("Hand [d]"));
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify the player to damage.");
            }
        }
        return true;
    }

}
