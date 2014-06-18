package net.wayward_realms.waywardmonsters.bleed;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BleedTask implements Runnable {

    private final Map<UUID, BleedingEntity> bleedingEntities = new ConcurrentHashMap<>();

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
