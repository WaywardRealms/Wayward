package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Party;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.combat.StatusEffect;
import net.wayward_realms.waywardlib.skills.AttackSpellBase;
import net.wayward_realms.waywardlib.skills.Stat;
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

import java.util.*;

public class BurnSpell extends AttackSpellBase {

    private WaywardSkills plugin;

    private int radius = 8;
    private int fireTicks = 200;

    public BurnSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("Burn");
        setManaCost(15);
        setCoolDown(5);
        setAttackStat(Stat.MAGIC_ATTACK);
        setDefenceStat(Stat.MAGIC_DEFENCE);
        setHitChance(100);
        setPower(10D);
        setCriticalChance(0);
    }

    @Override
    public boolean use(Player player) {
        Set<LivingEntity> invulnerableEntities = new HashSet<>();
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            Party party = characterPlugin.getParty(characterPlugin.getActiveCharacter(player));
            if (party != null) {
                for (Character member : party.getMembers()) {
                    OfflinePlayer memberPlayer = member.getPlayer();
                    if (memberPlayer.isOnline()) invulnerableEntities.add(memberPlayer.getPlayer());
                }
            }
        }
        for (LivingEntity entity : player.getWorld().getLivingEntities()) {
            if (player.getLocation().distanceSquared(entity.getLocation()) <= radius * radius) {
                if (!invulnerableEntities.contains(entity)) {
                    entity.setFireTicks(fireTicks);
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.TORCH);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Burn");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Combatant combatant) {
        return canUse((Character) combatant);
    }

    @Override
    public boolean canUse(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            return canUse(characterPlugin.getActiveCharacter(player));
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Deal 3 burn damage to the opposing party for 5 turns";
    }

    @Override
    public boolean canUse(Character character) {
        return hasScroll(character) && plugin.getSpecialisationValue(character, plugin.getSpecialisation("Fire Magic")) >= 5;
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        defending.getPlayer().getPlayer().setFireTicks(fireTicks);
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getMagicWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " set " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " alight with magic, dealing " + damage + " points of damage.";
    }

    @Override
    public String getFightFailManaMessage(Character attacking, Character defending) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " attempted to set " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " alight with magic, but did not have enough mana.";
    }

    @Override
    public Map<StatusEffect, Integer> getStatusEffects() {
        Map<StatusEffect, Integer> statusEffects = new EnumMap<>(StatusEffect.class);
        statusEffects.put(StatusEffect.BURNED, 5);
        return statusEffects;
    }

    @Override
    public List<String> getSpecialisationInfo() {
        return Arrays.asList(ChatColor.GRAY + "5 Fire Magic points required");
    }

}
