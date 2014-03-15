package net.wayward_realms.waywardcharacters;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RaceKitCommand implements CommandExecutor {

    private WaywardCharacters plugin;

    public RaceKitCommand(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (plugin.getActiveCharacter(player).getRace() instanceof RaceImpl) {
                if (plugin.canClaimRaceKit(player)) {
                    RaceImpl race = (RaceImpl) plugin.getActiveCharacter(player).getRace();
                    if (race.getKit() != null) {
                        race.getKit().give(player);
                        plugin.setRaceKitClaim(player);
                        sender.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Claimed your race kit. You may reclaim your next one tomorrow.");
                    } else {
                        sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Your race does not have a kit.");
                    }
                } else {
                    StringBuilder timeTillNextClaim = new StringBuilder();
                    long timeMillis = 86400000L - (System.currentTimeMillis() - plugin.getRaceKitClaim(player));
                    long time = timeMillis / 1000;
                    int seconds = (int) (time % 60);
                    int minutes = (int) ((time % 3600) / 60);
                    int hours = (int) (time / 3600);
                    timeTillNextClaim.append(hours).append(" hours, ").append(minutes).append(" minutes and ").append(seconds).append(" seconds.");
                    sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You can claim your next race kit in " + timeTillNextClaim.toString());
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Your race does not have a kit.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You may only execute this command as a player.");
        }
        return true;
    }

}
