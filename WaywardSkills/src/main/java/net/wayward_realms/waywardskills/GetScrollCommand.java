package net.wayward_realms.waywardskills;

import net.wayward_realms.waywardlib.skills.Spell;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GetScrollCommand implements CommandExecutor {

    private WaywardSkills plugin;

    public GetScrollCommand(WaywardSkills plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.skills.command.getscroll")) {
            if (sender instanceof Player) {
                if (args.length > 0) {
                    Spell spell = plugin.getSpell(args[0]);
                    ItemStack scroll = new ItemStack(Material.PAPER);
                    ItemMeta meta = scroll.getItemMeta();
                    meta.setDisplayName(ChatColor.YELLOW + "Scroll");
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.GRAY + spell.getName());
                    lore.add(ChatColor.GRAY + "Mana cost: " + spell.getManaCost());
                    lore.add(ChatColor.GRAY + "Cooldown (seconds): " + spell.getCoolDown());
                    lore.add(ChatColor.GRAY + "Cooldown (turns): " + spell.getCoolDownTurns());
                    meta.setLore(lore);
                    scroll.setItemMeta(meta);
                    ((Player) sender).getInventory().addItem(scroll);
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Here is the scroll for " + spell.getName());
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify the spell.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

}
