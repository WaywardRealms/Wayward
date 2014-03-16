package net.wayward_realms.waywardclasses;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.Class;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ListClassesCommand implements CommandExecutor {

    private WaywardClasses plugin;

    public ListClassesCommand(WaywardClasses plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "The use of this command is discouraged, it will be removed in future. Use '/class list' instead");
        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Available classes:");
        if (sender instanceof Player) {
            Player player = (Player) sender;
            RegisteredServiceProvider<CharacterPlugin> characterProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (characterProvider.getProvider() != null) {
                CharacterPlugin characterPlugin = characterProvider.getProvider();
                Character character = characterPlugin.getActiveCharacter(player);
                for (Class clazz : plugin.getClasses()) {
                    sender.sendMessage((clazz.hasPrerequisites(character) ? ChatColor.GREEN : ChatColor.RED) + clazz.getName());
                }
            }
        } else {
            for (Class clazz : plugin.getClasses()) {
                sender.sendMessage(ChatColor.GREEN + clazz.getName());
            }
        }
        return true;
    }

}
