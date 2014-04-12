package net.wayward_realms.waywardlocks;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LockpickEfficiencyCommand implements CommandExecutor {
    
    private WaywardLocks plugin;

    public LockpickEfficiencyCommand(WaywardLocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (characterPluginProvider != null) {
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Lockpicking efficiency: " + plugin.getLockpickEfficiency(characterPlugin.getActiveCharacter((Player) sender)));
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to use this command.");
        }
        return true;
    }
}
