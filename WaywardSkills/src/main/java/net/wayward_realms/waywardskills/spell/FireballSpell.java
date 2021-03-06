package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.combat.StatusEffect;
import net.wayward_realms.waywardlib.skills.AttackSpellBase;
import net.wayward_realms.waywardlib.skills.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.EnumMap;
import java.util.Map;

import static net.wayward_realms.waywardlib.classes.Stat.MAGIC_ATTACK;
import static net.wayward_realms.waywardlib.classes.Stat.MAGIC_DEFENCE;

public class FireballSpell extends AttackSpellBase {

    public FireballSpell() {
        setName("Fireball");
        setManaCost(5);
        setCoolDown(15);
        setType(SkillType.MAGIC_OFFENCE);
        setCriticalChance(2);
        setPower(50);
        setAttackStat(MAGIC_ATTACK);
        setDefenceStat(MAGIC_DEFENCE);
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.BLAZE_POWDER, 1);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName("Fireball");
        icon.setItemMeta(iconMeta);
        return icon;
    }

    @Override
    public boolean use(Player player) {
        player.launchProjectile(Fireball.class);
        return true;
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        attacking.getPlayer().getPlayer().launchProjectile(Fireball.class);
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getMagicWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " launched a fireball at " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " dealing " + (Math.round(damage * 100D) / 100D) + " points of damage.";
    }

    @Override
    public String getFightFailManaMessage(Character attacking, Character defending) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " tried to form a fireball, but did not have enough mana.";
    }

    @Override
    public boolean canUse(Combatant combatant) {
        return canUse((Character) combatant);
    }

    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_OFFENCE) >= 30;
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
        return "Deals damage equal to double the difference between your magic attack roll and your target's magic defence roll";
    }

    @Override
    public Map<StatusEffect, Integer> getStatusEffects() {
        Map<StatusEffect, Integer> statusEffects = new EnumMap<>(StatusEffect.class);
        statusEffects.put(StatusEffect.BURNED, 3);
        return statusEffects;
    }

    @Override
    public int getStatusEffectChance(StatusEffect statusEffect) {
        return 30;
    }

}
