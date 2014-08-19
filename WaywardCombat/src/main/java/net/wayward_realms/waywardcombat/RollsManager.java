package net.wayward_realms.waywardcombat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RollsManager {

    private WaywardCombat plugin;

    public RollsManager(WaywardCombat plugin) {
        this.plugin = plugin;
    }

    public int roll(Player roller, String rollString) {
        try {
            int amount = 1;
            int maxRoll;
            int plus = 0;

            String secondHalf;
            if (rollString.contains("d")) {
                amount = Integer.parseInt(rollString.split("d")[0]);
                secondHalf = rollString.split("d")[1];
            } else {
                secondHalf = rollString;
            }
            if (amount >= 100) {
                roller.sendMessage(plugin.getPrefix() + ChatColor.RED + "You can't roll that many times!");
                return -1;
            }
            if (rollString.contains("+")) {
                plus = Integer.parseInt(secondHalf.split("\\+")[1]);
                maxRoll = Integer.parseInt(secondHalf.split("\\+")[0]);
            } else if (rollString.contains("-")) {
                plus = -Integer.parseInt(secondHalf.split("\\-")[1]);
                maxRoll = Integer.parseInt(secondHalf.split("\\-")[0]);
            } else {
                maxRoll = Integer.parseInt(secondHalf);
            }
            if (maxRoll <= 0) {
                roller.sendMessage(plugin.getPrefix() + ChatColor.RED + "You can't roll a zero or negative number!");
                return -1;
            }
            List<Integer> rolls = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < amount; i++) {
                rolls.add(random.nextInt(maxRoll) + 1);
            }
            String output = ChatColor.GRAY + "(";
            int rollTotal = 0;
            for (int roll : rolls) {
                output += roll;
                output += "+";
                rollTotal += roll;
            }
            rollTotal += plus;
            output += plus + ") = " + rollTotal;
            for (Player player : roller.getWorld().getPlayers()) {
                if (player.getLocation().distanceSquared(roller.getLocation()) <= 256) {
                    if (plus > 0) {
                        player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + roller.getName() + ChatColor.GRAY + "/" + ChatColor.GREEN + roller.getDisplayName() + ChatColor.GRAY + " rolled " + ChatColor.YELLOW + amount + "d" + maxRoll + "+" + plus);
                    } else if (plus < 0) {
                        player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + roller.getName() + ChatColor.GRAY + "/" + ChatColor.GREEN + roller.getDisplayName() + ChatColor.GRAY + " rolled " + ChatColor.YELLOW + amount + "d" + maxRoll + "" + plus);
                    } else if (plus == 0) {
                        player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + roller.getName() + ChatColor.GRAY + "/" + ChatColor.GREEN + roller.getDisplayName() + ChatColor.GRAY + " rolled " + ChatColor.YELLOW + amount + "d" + maxRoll);
                    }
                    player.sendMessage(plugin.getPrefix() + output);
                }
            }
            return rollTotal;
        } catch (NumberFormatException exception) {
            roller.sendMessage(plugin.getPrefix() + ChatColor.RED + "Usage: /roll [roll|[attack|defence] [onhand|offhand] [specialisation]]");
            return -1;
        }
    }

}
