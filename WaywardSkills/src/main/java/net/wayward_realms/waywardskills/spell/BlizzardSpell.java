package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Party;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.combat.StatusEffect;
import net.wayward_realms.waywardlib.skills.AttackSpellBase;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.util.BlockIterator;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlizzardSpell extends AttackSpellBase {

    private WaywardSkills plugin;

    public BlizzardSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("Blizzard");
        setManaCost(50);
        setCoolDown(120);
        setType(SkillType.MAGIC_OFFENCE);
        setAttackStat(Stat.MAGIC_ATTACK);
        setDefenceStat(Stat.MAGIC_DEFENCE);
        setHitChance(50);
        setPower(100D);
        setCriticalChance(5);
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        if (attacking.getPlayer().isOnline()) {
            Player player = attacking.getPlayer().getPlayer();
            BlockIterator iterator = new BlockIterator(player, 8);
            long delay = 0L;
            int i = 0;
            while (iterator.hasNext()) {
                Block block = iterator.next();
                setShard(block.getRelative(BlockFace.UP, 2).getRelative(BlockFace.DOWN, i), delay);
                float yaw = player.getLocation().getYaw();
                yaw = (yaw + 90) % 360;
                int left = (int) Math.floor((yaw - 90) / 90);
                int right = (int) Math.floor((yaw + 90) / 90);
                BlockFace leftBlockFace = BlockFace.UP;
                switch (left) {
                    case 0: leftBlockFace = BlockFace.NORTH; break;
                    case 1: leftBlockFace = BlockFace.WEST; break;
                    case 2: leftBlockFace = BlockFace.SOUTH; break;
                    case 3: leftBlockFace = BlockFace.EAST; break;
                }
                BlockFace rightBlockFace = BlockFace.UP;
                switch (right) {
                    case 0: rightBlockFace = BlockFace.NORTH; break;
                    case 1: rightBlockFace = BlockFace.WEST; break;
                    case 2: rightBlockFace = BlockFace.SOUTH; break;
                    case 3: rightBlockFace = BlockFace.EAST; break;
                }
                setShard(block.getRelative(leftBlockFace, 2).getRelative(BlockFace.DOWN, i), delay);
                setShard(block.getRelative(rightBlockFace, 2).getRelative(BlockFace.DOWN, i), delay);
                delay += 5L;
                i++;
            }
        }
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getMagicWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " summoned a blizzard at " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + ", dealing " + damage + " damage.";
    }

    @Override
    public String getFightFailManaMessage(Character attacking, Character defending) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " began summoning strong winds, but didn't have enough mana to summon a blizzard.";
    }

    @Override
    public boolean use(Player player) {
        BlockIterator iterator = new BlockIterator(player, 8);
        long delay = 0L;
        int i = 0;
        while (iterator.hasNext()) {
            Block block = iterator.next();
            setShard(block.getRelative(BlockFace.UP, 2).getRelative(BlockFace.DOWN, i), delay);
            float yaw = player.getLocation().getYaw();
            yaw = (yaw + 90) % 360;
            int left = (int) Math.floor((yaw - 90) / 90);
            int right = (int) Math.floor((yaw + 90) / 90);
            BlockFace leftBlockFace = BlockFace.UP;
            switch (left) {
                case 0: leftBlockFace = BlockFace.NORTH; break;
                case 1: leftBlockFace = BlockFace.WEST; break;
                case 2: leftBlockFace = BlockFace.SOUTH; break;
                case 3: leftBlockFace = BlockFace.EAST; break;
            }
            BlockFace rightBlockFace = BlockFace.UP;
            switch (right) {
                case 0: rightBlockFace = BlockFace.NORTH; break;
                case 1: rightBlockFace = BlockFace.WEST; break;
                case 2: rightBlockFace = BlockFace.SOUTH; break;
                case 3: rightBlockFace = BlockFace.EAST; break;
            }
            setShard(block.getRelative(leftBlockFace, 2).getRelative(BlockFace.DOWN, i), delay);
            setShard(block.getRelative(rightBlockFace, 2).getRelative(BlockFace.DOWN, i), delay);
            delay += 5L;
            i++;
        }
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
            if (entity.getLocation().distanceSquared(player.getLocation()) <= 64) {
                if (!invulnerableEntities.contains(entity)) entity.damage(entity.getHealth() / 2, player);
            }
        }
        return true;
    }

    public void setShard(final Block block, long delay) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (block.getType() == Material.AIR) {
                    block.setType(Material.PACKED_ICE);
                }
            }
        }, delay);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (block.getType() == Material.PACKED_ICE) {
                    block.setType(Material.AIR);
                }
            }
        }, delay * 2);
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.PACKED_ICE);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Blizzard");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_OFFENCE) >= 40;
    }

    @Override
    public String getDescription() {
        return "Deals damage equal to the difference between your magic attack roll and your opponent's magic defence roll for every member of the opposing party";
    }

    @Override
    public Map<StatusEffect, Integer> getStatusEffects() {
        Map<StatusEffect, Integer> statusEffects = new EnumMap<>(StatusEffect.class);
        statusEffects.put(StatusEffect.FROZEN, 3);
        return statusEffects;
    }

    @Override
    public int getStatusEffectChance(StatusEffect statusEffect) {
        return 20;
    }

}
