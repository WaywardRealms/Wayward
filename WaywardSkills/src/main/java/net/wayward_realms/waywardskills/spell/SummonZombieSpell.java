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

public class SummonZombieSpell extends AttackSpellBase {

    private WaywardSkills plugin;

    public SummonZombieSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("SummonZombie");
        setManaCost(50);
        setCoolDown(60);
        setAttackStat(Stat.MAGIC_ATTACK);
        setDefenceStat(Stat.MAGIC_DEFENCE);
        setType(SkillType.MAGIC_SUMMONING);
        setPower(90);
        setCriticalChance(5);
        setHitChance(30);
    }
    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        Player attackingPlayer = attacking.getPlayer().getPlayer();
        Entity zombie = attackingPlayer.getWorld().spawnEntity(attackingPlayer.getLocation(), EntityType.ZOMBIE);
        if (zombie != null) {
            zombie.setMetadata("summoner", new FixedMetadataValue(plugin, attacking.getId()));
        }
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getMagicWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return attacking.getName() + " summoned a zombie and demanded it attack " + defending.getName() + "! It dealt " + damage + " damage.";
    }

    @Override
    public String getFightFailManaMessage(Character attacking, Character defending) {
        return attacking.getName() + " attempted to summon a zombie, but did not have enough mana.";
    }

    @Override
    public boolean use(Player player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            Entity zombie = player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
            if (zombie != null) {
                zombie.setMetadata("summoner", new FixedMetadataValue(plugin, characterPlugin.getActiveCharacter(player).getId()));
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "You cannot summon a zombie here! (Try a bit further into the wilderness)");
            }
        }
        return false;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.SKULL_ITEM, 2);
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_SUMMONING) >= 5;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", getName());
        serialised.put("mana-cost", getManaCost());
        serialised.put("cooldown", getCoolDown());
        return serialised;
    }

    public static SummonZombieSpell deserialize(Map<String, Object> serialised) {
        RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
        if (skillsPluginProvider != null) {
            SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
            if (skillsPlugin instanceof WaywardSkills) {
                SummonZombieSpell deserialised = new SummonZombieSpell((WaywardSkills) skillsPlugin);
                deserialised.setName((String) serialised.get("name"));
                deserialised.setManaCost((int) serialised.get("mana-cost"));
                deserialised.setCoolDown((int) serialised.get("cooldown"));
                return deserialised;
            }
        }
        return null;
    }

}
