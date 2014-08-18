package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Party;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.combat.StatusEffect;
import net.wayward_realms.waywardlib.skills.SpellBase;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class EsunaSpell extends SpellBase {

    private WaywardSkills plugin;

    private int radius = 8;

    public EsunaSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("Esuna");
        setManaCost(5);
        setCoolDown(5);
    }

    @Override
    public boolean use(Player player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            Party party = characterPlugin.getParty(characterPlugin.getActiveCharacter(player));
            if (party != null) {
                for (Character member : party.getMembers()) {
                    OfflinePlayer memberPlayer = member.getPlayer();
                    if (memberPlayer.isOnline()) {
                        for (PotionEffectType potionEffectType : PotionEffectType.values()) {
                            if (potionEffectType != null) {
                                memberPlayer.getPlayer().addPotionEffect(new PotionEffect(potionEffectType, 0, 0), true);
                            }
                        }
                    }
                }
            } else {
                for (LivingEntity entity : player.getWorld().getEntitiesByClass(LivingEntity.class)) {
                    if (player.getLocation().distanceSquared(entity.getLocation()) <= radius * radius) {
                        for (PotionEffectType potionEffectType : PotionEffectType.values()) {
                            if (potionEffectType != null) {
                                entity.addPotionEffect(new PotionEffect(potionEffectType, 0, 0), true);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        if (attacking.getMana() >= getManaCost()) {
            for (StatusEffect statusEffect : StatusEffect.values()) {
                fight.setStatusTurns(defending, statusEffect, 0);
            }
            fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " cured " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + "'s status effects");
            return true;
        } else {
            fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " attempted to cure " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + "'s status effects, but did not have enough mana!");
            return false;
        }
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Esuna");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return hasScroll(character) && plugin.getSpecialisationValue(character, plugin.getSpecialisation("Regenerative Magic")) >= 3;
    }

    @Override
    public String getDescription() {
        return "Cure all status ailments (burn, bleed, paralyse, etc) for one target";
    }

    @Override
    public List<String> getSpecialisationInfo() {
        return Arrays.asList(ChatColor.GRAY + "3 Regenerative Magic points required");
    }

}
