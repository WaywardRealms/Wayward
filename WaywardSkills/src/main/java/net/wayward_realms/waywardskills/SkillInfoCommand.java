package net.wayward_realms.waywardskills;

import net.wayward_realms.waywardlib.skills.Skill;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SkillInfoCommand implements CommandExecutor {

    private WaywardSkills plugin;

    public SkillInfoCommand(WaywardSkills plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            Skill skill = plugin.getSkill(args[0]);
            if (skill != null) {
                sender.sendMessage(new String[] {
                    ChatColor.GRAY + "== " + ChatColor.GREEN + "Skill: " + skill.getName() + ChatColor.GRAY + " ==",
                    skill.getSpecialisationInfo(),
                    ChatColor.YELLOW + "Cooldown turns: " + ChatColor.GRAY + skill.getCoolDownTurns()
                });
            }
        }
        return true;
    }

}
