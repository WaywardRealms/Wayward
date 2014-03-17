package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpawnerCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public SpawnerCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.essentials.command.spawner")) {
            if (args.length >= 1) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    Block block = getTargetBlock(player);
                    if (block.getType() == Material.MOB_SPAWNER) {
                        CreatureSpawner spawner = (CreatureSpawner) block.getState();
                        try {
                            EntityType entityType = EntityType.valueOf(args[0].toUpperCase());
                            spawner.setSpawnedType(entityType);
                            sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Spawner type set.");
                        } catch (IllegalArgumentException exception) {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That entity does not exist.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "That's not a mob spawner.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to use this command.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify the mob type.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }

    private List<Block> getLineOfSight(LivingEntity entity) {
        ArrayList<Block> blocks = new ArrayList<>();
        Iterator<Block> itr = new BlockIterator(entity, 32);
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
