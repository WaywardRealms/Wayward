package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JumpCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public JumpCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.jump")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Block block = getTargetBlock(player);
                if (block != null) {
                    player.teleport(block.getLocation());
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Teleported to first block in line of sight.");
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Could not find a block within 64 blocks in your line of sight.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You may not use this command from console.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

    private List<Block> getLineOfSight(LivingEntity entity) {
        ArrayList<Block> blocks = new ArrayList<>();
        Iterator<Block> itr = new BlockIterator(entity, 64);
        while (itr.hasNext()) {
            Block block = itr.next();
            blocks.add(block);
            if (blocks.size() > 1) {
                blocks.remove(0);
            }
            Material material = block.getType();
            if (material != Material.AIR) {
                break;
            }
        }
        return blocks;
    }

    private Block getTargetBlock(LivingEntity entity) {
        List<Block> blocks = getLineOfSight(entity);
        return blocks.get(0);
    }

}
