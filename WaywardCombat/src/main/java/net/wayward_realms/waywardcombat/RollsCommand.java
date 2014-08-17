package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Equipment;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

public class RollsCommand implements CommandExecutor {

    private WaywardCombat plugin;

    public RollsCommand(WaywardCombat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (sender.hasPermission("wayward.combat.command.rolls.other")) {
            if (args.length > 0) {
                if (plugin.getServer().getPlayer(args[0]) != null) {
                    player = plugin.getServer().getPlayer(args[0]);
                }
            }
        }
        if (player != null) {
            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta meta = (BookMeta) book.getItemMeta();
            meta.setTitle("Rolls");
            meta.setAuthor("Wayward");
            RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = plugin.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (skillsPluginProvider != null && characterPluginProvider != null) {
                SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                Character character = characterPlugin.getActiveCharacter(player);
                Equipment equipment = character.getEquipment();
                int i = 0;
                for (Specialisation specialisation : skillsPlugin.getSpecialisations()) {
                    StringBuilder pageBuilder = new StringBuilder();
                    pageBuilder.append(ChatColor.BOLD).append(specialisation.getName()).append(ChatColor.RESET).append("\n");
                    if (specialisation.meetsAttackRequirement(equipment.getOnHandItem())) {
                        pageBuilder.append("Attack roll with onhand item: ").append(skillsPlugin.getAttackRoll(character, specialisation, true)).append("\n");
                    }
                    if (specialisation.meetsAttackRequirement(equipment.getOffHandItem())) {
                        pageBuilder.append("Attack roll with offhand item: ").append(skillsPlugin.getAttackRoll(character, specialisation, false)).append("\n");
                    }
                    if (specialisation.meetsDefenceRequirement(equipment.getOnHandItem())) {
                        pageBuilder.append("Defence roll with onhand item: ").append(skillsPlugin.getDefenceRoll(character, specialisation, true)).append("\n");
                    }
                    if (specialisation.meetsDefenceRequirement(equipment.getOffHandItem())) {
                        pageBuilder.append("Defence roll with offhand item: ").append(skillsPlugin.getDefenceRoll(character, specialisation, false)).append("\n");
                    }
                    meta.setPage(i, pageBuilder.toString());
                    i++;
                }
                book.setItemMeta(meta);
                player.getInventory().addItem(book);
            }
        }
        return true;
    }

}
