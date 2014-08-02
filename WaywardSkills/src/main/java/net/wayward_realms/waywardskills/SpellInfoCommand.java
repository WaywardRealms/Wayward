package net.wayward_realms.waywardskills;

import net.wayward_realms.waywardlib.skills.Spell;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SpellInfoCommand implements CommandExecutor {

    private WaywardSkills plugin;

    public SpellInfoCommand(WaywardSkills plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            Spell spell = plugin.getSpell(args[0]);
            if (spell != null) {
                sender.sendMessage(new String[] {
                        ChatColor.GRAY + "== " + ChatColor.GREEN + "Spell: " + spell.getName() + ChatColor.GRAY + " ==",
                        ChatColor.GRAY + spell.getDescription(),
                        ChatColor.YELLOW + "Cooldown turns: " + ChatColor.GRAY + spell.getCoolDownTurns(),
                        ChatColor.YELLOW + "Type: " + ChatColor.GRAY + spell.getType()
                });
            }
        }
        return true;
    }

}
