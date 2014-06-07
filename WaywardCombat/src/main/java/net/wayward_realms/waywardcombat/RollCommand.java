package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RollCommand implements CommandExecutor {

    private WaywardCombat plugin;

    public RollCommand(WaywardCombat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                if (characterPluginProvider != null) {
                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                    try {
                        Stat stat = Stat.valueOf(args[0].toUpperCase());
                        roll((Player) sender, plugin.getRollsManager().getRoll(characterPlugin.getActiveCharacter((Player) sender), stat));
                    } catch (IllegalArgumentException exception) {
                        roll((Player) sender, args[0]);
                    }
                } else {
                    roll((Player) sender, args[0]);
                }
            } else {
                sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must specify a roll string.");
            }
        } else {
            sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You must be a player to perform this command.");
        }
        return true;
    }

    private int roll(Player roller, String rollString) {
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
    }

}
