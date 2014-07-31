package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.combat.StatusEffect;
import net.wayward_realms.waywardlib.skills.AttackSpellBase;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.util.lineofsight.LineOfSightUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.EnumMap;
import java.util.Map;

import static net.wayward_realms.waywardlib.classes.Stat.MAGIC_ATTACK;
import static net.wayward_realms.waywardlib.classes.Stat.MAGIC_DEFENCE;

public class LightningSpell extends AttackSpellBase {

    public LightningSpell() {
        setName("Lightning");
        setManaCost(100);
        setCoolDown(90);
        setType(SkillType.MAGIC_OFFENCE);
        setCriticalChance(20);
        setCriticalMultiplier(4D);
        setPower(90);
        setAttackStat(MAGIC_ATTACK);
        setDefenceStat(MAGIC_DEFENCE);
        setHitChance(100);
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName("Lightning");
        icon.setItemMeta(iconMeta);
        return icon;
    }

    @Override
    public boolean use(Player player) {
        player.getWorld().strikeLightning(LineOfSightUtils.getTargetBlock(player, null, 64).getLocation());
        return true;
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        Player defendingPlayer = defending.getPlayer().getPlayer();
        defendingPlayer.getWorld().strikeLightningEffect(defendingPlayer.getLocation());
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getMagicWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " struck a lightning bolt through " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " dealing " + damage + " points of damage.";
    }

    @Override
    public String getFightFailManaMessage(Character attacking, Character defending) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " tried to form a bolt of lightning, but did not have enough mana.";
    }

    @Override
    public boolean canUse(Combatant combatant) {
        return canUse((Character) combatant);
    }

    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_OFFENCE) >= 50;
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
        return "Deals damage equal to five times the difference between your magic attack roll and your opponent's magic defence roll";
    }

    @Override
    public int getStatusEffectChance(StatusEffect statusEffect) {
        return 10;
    }

    @Override
    public Map<StatusEffect, Integer> getStatusEffects() {
        Map<StatusEffect, Integer> statusEffects = new EnumMap<>(StatusEffect.class);
        statusEffects.put(StatusEffect.BURNED, 3);
        return statusEffects;
    }
}
