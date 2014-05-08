package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.AttackSpellBase;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;

public class SummonSkeletonSpell extends AttackSpellBase {

    private WaywardSkills plugin;

    public SummonSkeletonSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("SummonSkeleton");
        setManaCost(60);
        setCoolDown(60);
        setAttackStat(Stat.MAGIC_ATTACK);
        setDefenceStat(Stat.MAGIC_DEFENCE);
        setType(SkillType.MAGIC_SUMMONING);
        setPower(90);
        setCriticalChance(5);
        setHitChance(40);
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        Player attackingPlayer = attacking.getPlayer().getPlayer();
        Entity skeleton = attackingPlayer.getWorld().spawnEntity(attackingPlayer.getLocation(), EntityType.SKELETON);
        if (skeleton != null) {
            skeleton.setMetadata("summoner", new FixedMetadataValue(plugin, attacking.getId()));
        }
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getMagicWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return attacking.getName() + " summoned a skeleton and demanded it attack " + defending.getName() + "! It dealt " + damage + " damage.";
    }

    @Override
    public String getFightFailManaMessage(Character attacking, Character defending) {
        return attacking.getName() + " attempted to summon a skeleton, but did not have enough mana.";
    }

    @Override
    public boolean use(Player player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            Entity skeleton = player.getWorld().spawnEntity(player.getLocation(), EntityType.SKELETON);
            if (skeleton != null) {
                skeleton.setMetadata("summoner", new FixedMetadataValue(plugin, characterPlugin.getActiveCharacter(player).getId()));
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "You cannot summon a skeleton here! (Try a bit further into the wilderness)");
            }
        }
        return false;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.SKULL_ITEM, 1, (byte) 0);
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_SUMMONING) >= 10;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", getName());
        serialised.put("mana-cost", getManaCost());
        serialised.put("cooldown", getCoolDown());
        return serialised;
    }

    public static SummonSkeletonSpell deserialize(Map<String, Object> serialised) {
        RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
        if (skillsPluginProvider != null) {
            SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
            if (skillsPlugin instanceof WaywardSkills) {
                SummonSkeletonSpell deserialised = new SummonSkeletonSpell((WaywardSkills) skillsPlugin);
                deserialised.setName((String) serialised.get("name"));
                deserialised.setManaCost((int) serialised.get("mana-cost"));
                deserialised.setCoolDown((int) serialised.get("cooldown"));
                return deserialised;
            }
        }
        return null;
    }

}
