package net.wayward_realms.waywardlib.skills;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * A base class for skills to extend
 */
public abstract class SkillBase implements Skill {

    private String name;
    private SkillType type;
    private int coolDown;
    private int coolDownTurns = 0;

    @Override
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon) {
        return attacking instanceof Character && defending instanceof Character && use(fight, (Character) attacking, (Character) defending, weapon);
    }

    @Override
    public boolean canUse(Combatant combatant) {
        return combatant instanceof Character && canUse((Character) combatant);
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public SkillType getType() {
        return type;
    }

    @Override
    public void setType(SkillType type) {
        this.type = type;
    }

    @Override
    public int getCoolDown() {
        return coolDown;
    }

    @Override
    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    public double getMeleeWeaponModifier(ItemStack weapon) {
        if (weapon != null) {
            switch (weapon.getType()) {
                case WOOD_SWORD: return 1.1D;
                case STONE_SWORD: return 1.2D;
                case IRON_SWORD: return 1.3D;
                case DIAMOND_SWORD: return 1.5D;
                default: return 1D;
            }
        }
        return 1D;
    }

    public double getRangedWeaponModifier(ItemStack weapon) {
        if (weapon != null) {
            switch (weapon.getType()) {
                case BOW: return 1.5D;
                default: return 1D;
            }
        }
        return 1D;
    }

    public double getMagicWeaponModifier(ItemStack weapon) {
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
    public int getCoolDownTurns() {
        return coolDownTurns;
    }

    @Override
    public void setCoolDownTurns(int coolDownTurns) {
        this.coolDownTurns = coolDownTurns;
    }
}
