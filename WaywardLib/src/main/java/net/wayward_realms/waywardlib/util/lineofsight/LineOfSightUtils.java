package net.wayward_realms.waywardlib.util.lineofsight;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockIterator;

import java.util.*;

/**
 * Utility class for finding the line of sight of living entities
 */
public class LineOfSightUtils {

    private static List<Block> getLineOfSight(LivingEntity entity, Collection<Material> transparent, int maxDistance, int maxLength) {
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        ArrayList<Block> blocks = new ArrayList<>();
        Iterator<Block> itr = new BlockIterator(entity, maxDistance);
        while (itr.hasNext()) {
            Block block = itr.next();
            blocks.add(block);
            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.remove(0);
            }
            Material material = block.getType();
            if (transparent == null) {
                if (material != Material.AIR) {
                    break;
                }
            } else {
                if (!transparent.contains(material)) {
                    break;
                }
            }
        }
        return blocks;
    }

    /**
     * Gets all blocks along the living entity's line of sight.
     * <p>
     * This list contains all blocks from the living entity's eye position
     * to target inclusive.
     *
     * @param entity the entity to get the line of sight of
     * @param transparent HashSet containing all transparent block materials (set to null for only air)
     * @param maxDistance this is the maximum distance to scan (may be limited by server by at least 100 blocks, no less)
     * @return list containing all blocks along the living entity's line of sight
     */
    public static List<Block> getLineOfSight(LivingEntity entity, Collection<Material> transparent, int maxDistance) {
        return getLineOfSight(entity, transparent, maxDistance, 0);
    }

    /**
     * Gets the block that the living entity has targeted.
     *
     * @param entity the entity to get the target block of
     * @param transparent HashSet containing all transparent block materials (set to null for only air)
     * @param maxDistance this is the maximum distance to scan (may be limited by server by at least 100 blocks, no less)
     * @return block that the living entity has targeted
     */
    public static Block getTargetBlock(LivingEntity entity, HashSet<Material> transparent, int maxDistance) {
        List<Block> blocks = getLineOfSight(entity, transparent, maxDistance, 1);
        return blocks.get(0);
    }

    /**
     * Gets the last two blocks along the living entity's line of sight.
     * <p>
     * The target block will be the last block in the list.
     *
     * @param entity the entity to get the last two target blocks of
     * @param transparent HashSet containing all transparent block materials (set to null for only air)
     * @param maxDistance this is the maximum distance to scan. This may be further limited by the server, but never to less than 100 blocks
     * @return list containing the last 2 blocks along the living entity's line of sight
     */
    public static List<Block> getLastTwoTargetBlocks(LivingEntity entity, HashSet<Material> transparent, int maxDistance) {
        return getLineOfSight(entity, transparent, maxDistance, 2);
    }

}
