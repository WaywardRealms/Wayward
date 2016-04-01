package net.wayward_realms.waywardessentials.command;

import net.wayward_realms.waywardessentials.WaywardEssentials;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;

import java.io.IOException;

public class IssueCommand implements CommandExecutor {

    private WaywardEssentials plugin;

    public IssueCommand(WaywardEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("create")) {
                    for (ItemStack remainder : player.getInventory().addItem(new ItemStack(Material.BOOK_AND_QUILL, 1)).values()) {
                        player.getWorld().dropItem(player.getLocation(), remainder);
                    }
                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Please write your issue in the book, titling it with the title of the issue, then use /issue submit [plugin] with the book in hand when done.");
                } else if (args[0].equalsIgnoreCase("submit")) {
                    if (player.getInventory().getItemInMainHand() != null) {
                        if (player.getInventory().getItemInMainHand().hasItemMeta()) {
                            if (player.getInventory().getItemInMainHand().getItemMeta() instanceof BookMeta) {
                                BookMeta meta = (BookMeta) player.getInventory().getItemInMainHand().getItemMeta();
                                StringBuilder issueBody = new StringBuilder();
                                for (String pageText : meta.getPages()) {
                                    issueBody.append("> ").append(pageText.replace("\n", "\n> ")).append('\n');
                                }
                                issueBody.append("> &mdash; ").append(sender.getName()).append("\n");
                                try {
                                    GHRepository repo = plugin.getGitHub().getRepository("WaywardRealms/Wayward");
                                    GHIssue issue = repo.createIssue(meta.hasTitle() ? meta.getTitle() : "Issue").body(issueBody.toString()).create();
                                    player.getInventory().setItemInMainHand(null);
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Issue submitted. Thank you!");
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "You can view it at " + issue.getUrl().toString());
                                } catch (IOException exception) {
                                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Failed to create issue.");
                                    exception.printStackTrace();
                                }
                            } else {
                                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be holding a book in order to create an issue.");
                            }
                        } else {
                            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be holding a book in order to create an issue.");
                        }
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be holding a book in order to create an issue.");
                    }
                } else {
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /issue [submit|create]");
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /issue [submit|create]");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to report issues.");
        }
        return true;
    }

}
