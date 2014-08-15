package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Party;
import net.wayward_realms.waywardlib.character.TemporaryStatModification;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillBase;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ArmourUpSkill extends SkillBase {

    private WaywardSkills plugin;

    public ArmourUpSkill(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("ArmourUp");
        setCoolDown(180);
        setCoolDownTurns(5);
    }

    @Override
    public boolean use(Player player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            final Character character = characterPlugin.getActiveCharacter(player);
            Party party = characterPlugin.getParty(character);
            if (party != null) {
                for (final Character partyMember : party.getMembers()) {
                    partyMember.addTemporaryStatModification(TemporaryStatModification.MELEE_DEFENCE_UP_25);
                    partyMember.addTemporaryStatModification(TemporaryStatModification.RANGED_DEFENCE_UP_25);
                    partyMember.addTemporaryStatModification(TemporaryStatModification.MAGIC_DEFENCE_UP_25);
                    OfflinePlayer partyMemberPlayer = partyMember.getPlayer();
                    if (partyMemberPlayer.isOnline()) {
                        partyMemberPlayer.getPlayer().sendMessage(ChatColor.GREEN + "Defence stats +25% for 15 seconds");
                    }
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            partyMember.removeTemporaryStatModification(TemporaryStatModification.MELEE_DEFENCE_UP_25);
                            partyMember.removeTemporaryStatModification(TemporaryStatModification.RANGED_DEFENCE_UP_25);
                            partyMember.removeTemporaryStatModification(TemporaryStatModification.MAGIC_DEFENCE_UP_25);
                        }
                    }, 300L);
                }
            } else {
                character.addTemporaryStatModification(TemporaryStatModification.MELEE_DEFENCE_UP_25);
                character.addTemporaryStatModification(TemporaryStatModification.RANGED_DEFENCE_UP_25);
                character.addTemporaryStatModification(TemporaryStatModification.MAGIC_DEFENCE_UP_25);
                player.sendMessage(ChatColor.GREEN + "Defence stats +25% for 15 seconds");
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        character.removeTemporaryStatModification(TemporaryStatModification.MELEE_DEFENCE_UP_25);
                        character.removeTemporaryStatModification(TemporaryStatModification.RANGED_DEFENCE_UP_25);
                        character.removeTemporaryStatModification(TemporaryStatModification.MAGIC_DEFENCE_UP_25);
                    }
                }, 300L);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            final Character character = defending;
            Party party = characterPlugin.getParty(character);
            if (party != null) {
                for (final Character partyMember : party.getMembers()) {
                    partyMember.addTemporaryStatModification(TemporaryStatModification.MELEE_DEFENCE_UP_25);
                    partyMember.addTemporaryStatModification(TemporaryStatModification.RANGED_DEFENCE_UP_25);
                    partyMember.addTemporaryStatModification(TemporaryStatModification.MAGIC_DEFENCE_UP_25);
                    fight.scheduleTask(new Runnable() {
                        @Override
                        public void run() {
                            partyMember.removeTemporaryStatModification(TemporaryStatModification.MELEE_DEFENCE_UP_25);
                            partyMember.removeTemporaryStatModification(TemporaryStatModification.RANGED_DEFENCE_UP_25);
                            partyMember.removeTemporaryStatModification(TemporaryStatModification.MAGIC_DEFENCE_UP_25);
                        }
                    }, 3);
                }
                fight.sendMessage(ChatColor.YELLOW + defending.getName() + "'s party's defence stats were buffed for three turns.");
            } else {
                character.addTemporaryStatModification(TemporaryStatModification.MELEE_DEFENCE_UP_25);
                character.addTemporaryStatModification(TemporaryStatModification.RANGED_DEFENCE_UP_25);
                character.addTemporaryStatModification(TemporaryStatModification.MAGIC_DEFENCE_UP_25);
                fight.sendMessage(ChatColor.YELLOW + defending.getName() + "'s defence stats were buffed for three turns.");
                fight.scheduleTask(new Runnable() {
                    @Override
                    public void run() {
                        character.removeTemporaryStatModification(TemporaryStatModification.MELEE_DEFENCE_UP_25);
                        character.removeTemporaryStatModification(TemporaryStatModification.RANGED_DEFENCE_UP_25);
                        character.removeTemporaryStatModification(TemporaryStatModification.MAGIC_DEFENCE_UP_25);
                    }
                }, 3);
            }
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.IRON_CHESTPLATE);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Armour Up");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return true;
    }

    @Override
    public String getDescription() {
        return "Add 25% to each of your defence rolls";
    }

}
