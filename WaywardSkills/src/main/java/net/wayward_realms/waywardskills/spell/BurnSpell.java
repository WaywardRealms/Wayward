package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.AttackSpellBase;
import net.wayward_realms.waywardlib.skills.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;

public class BurnSpell extends AttackSpellBase {

    private int radius = 8;
    private int fireTicks = 200;

    public BurnSpell() {
        setName("Burn");
        setManaCost(15);
        setCoolDown(5);
        setType(SkillType.MAGIC_OFFENCE);
        setAttackStat(Stat.MAGIC_ATTACK);
        setDefenceStat(Stat.MAGIC_DEFENCE);
        setHitChance(100);
        setPower(10D);
        setCriticalChance(0);
    }

    @Override
    public boolean use(Player player) {
        for (Entity entity : player.getWorld().getEntities()) {
            if (player.getLocation().distance(entity.getLocation()) <= radius) {
                if (entity != player) {
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
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_OFFENCE) >= 20;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", getName());
        serialised.put("mana-cost", getManaCost());
        serialised.put("radius", radius);
        serialised.put("fire-ticks", fireTicks);
        serialised.put("cooldown", getCoolDown());
        return serialised;
    }

    public BurnSpell deserialize(Map<String, Object> serialised) {
        BurnSpell deserialised = new BurnSpell();
        deserialised.setName((String) serialised.get("name"));
        deserialised.setManaCost((int) serialised.get("mana-cost"));
        deserialised.radius = (int) serialised.get("radius");
        deserialised.fireTicks = (int) serialised.get("fire-ticks");
        deserialised.setCoolDown((int) serialised.get("cooldown"));
        return deserialised;
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        defending.getPlayer().getPlayer().setFireTicks(fireTicks);
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        if (weapon != null) {
            switch (weapon.getType()) {
                case STICK: return 1.1D;
                case BLAZE_ROD: return 1.5D;
                default: return 1D;
            }
        }
        return 1D;
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return attacking.getName() + " set " + defending.getName() + " alight with magic, dealing " + damage + " points of damage.";
    }

    @Override
    public String getFightFailManaMessage(Character attacking, Character defending) {
        return attacking.getName() + " attempted to set " + defending.getName() + " alight with magic, but did not have enough mana.";
    }

}
