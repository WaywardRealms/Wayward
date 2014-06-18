package net.wayward_realms.waywardevents;

import net.wayward_realms.waywardlib.util.lineofsight.LineOfSightUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BlockDescriptionCommand implements CommandExecutor {

    private final WaywardEvents plugin;

    public BlockDescriptionCommand(WaywardEvents plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("wayward.events.command.blockdescription")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("info")) {
                    Block targetBlock = LineOfSightUtils.getTargetBlock((Player) sender, null, 8);
                    BlockDescriptor descriptor;
                    if (plugin.hasBlockDescription(targetBlock)) {
                        descriptor = plugin.getBlockDescription(targetBlock);
                    } else {
                        descriptor = new BlockDescriptor();
                    }
                    StringBuilder infoBuilder = new StringBuilder();
                    if (args.length > 1) {
                        for (int i = 1; i < args.length; i++) {
                            infoBuilder.append(args[i]).append(" ");
                        }
                        descriptor.setDescription(infoBuilder.toString());
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Block description set to '" + infoBuilder.toString() + "'");
                    } else {
                        descriptor.setDescription(null);
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Block description cleared.");
                    }
                    plugin.setBlockDescription(targetBlock, descriptor);
                } else if (args[0].equalsIgnoreCase("item")) {
                    Block targetBlock = LineOfSightUtils.getTargetBlock((Player) sender, null, 8);
                    BlockDescriptor descriptor;
                    if (plugin.hasBlockDescription(targetBlock)) {
                        descriptor = plugin.getBlockDescription(targetBlock);
                    } else {
                        descriptor = new BlockDescriptor();
                    }
                    List<ItemStack> nonNullItems = new ArrayList<>();
                    for (ItemStack item : ((Player) sender).getInventory().getContents()) {
                        if (item != null) nonNullItems.add(item);
                    }
                    descriptor.setItems(nonNullItems.toArray(new ItemStack[nonNullItems.size()]));
                    plugin.setBlockDescription(targetBlock, descriptor);
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Copy of inventory contents concealed in block.");
                } else if (args[0].equalsIgnoreCase("remove")) {
                    Block targetBlock = LineOfSightUtils.getTargetBlock((Player) sender, null, 8);
                    plugin.removeBlockDescription(targetBlock);
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Removed block description from the block.");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /" + label + " [info|item|remove]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission.");
        }
        return true;
    }
}
