package net.wayward_realms.waywardmonsters.bleed;

import net.wayward_realms.waywardmonsters.WaywardMonsters;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BleedTask implements Runnable {

    private WaywardMonsters plugin;

    private final Map<UUID, BleedingEntity> bleedingEntities = new ConcurrentHashMap<>();
    private final Map<Block, Integer> bloodBlocks = new ConcurrentHashMap<>();

    public BleedTask(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        synchronized (bleedingEntities) {
            final int interval = 10;
            final Iterator<BleedingEntity> iterator = bleedingEntities.values().iterator();
            while (iterator.hasNext()) {
                final BleedingEntity entry = iterator.next();
                final int timeleft = entry.reduceTimeleft(interval);
                final LivingEntity entity = entry.getEntity();
                if (timeleft > 0 && entity != null && !entity.isDead() && entity.getLocation() != null) {
                    Block block = entity.getWorld().getBlockAt(entity.getLocation());
                    if (block.getType() == Material.AIR && block.getRelative(BlockFace.DOWN).getType().isSolid()) {
                        block.setType(Material.REDSTONE_WIRE);
                        block.setMetadata("isBlood", new FixedMetadataValue(plugin, true));
                        synchronized (bloodBlocks) {
                            bloodBlocks.put(block, 200);
                        }
                    }
                } else {
                    iterator.remove();
                }
            }
        }
        synchronized (bloodBlocks) {
            final int interval = 10;
            final Iterator<Map.Entry<Block, Integer>> iterator = bloodBlocks.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<Block, Integer> entry = iterator.next();
                entry.setValue(entry.getValue() - interval);
                int timeLeft = entry.getValue();
                if (timeLeft > 0 && entry.getKey() != null && entry.getKey().getLocation() != null) {
                    Block block = entry.getKey();
                    if (block.getType() == Material.REDSTONE_WIRE && block.getMetadata("isBlood") != null && block.getMetadata("isBlood").size() > 0) {
                        block.setType(Material.AIR);
                        block.removeMetadata("isBlood", plugin);
                    }
                } else {
                    iterator.remove();
                }
            }
        }
    }

    public void add(final LivingEntity entity) {
        synchronized (bleedingEntities) {
            bleedingEntities.put(entity.getUniqueId(), new BleedingEntity(entity, 200));
        }
    }

    public void remove(final LivingEntity entity) {
        synchronized (bleedingEntities) {
            bleedingEntities.remove(entity.getUniqueId());
        }
    }

}
