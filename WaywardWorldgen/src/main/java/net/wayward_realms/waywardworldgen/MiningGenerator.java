package net.wayward_realms.waywardworldgen;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MiningGenerator extends ChunkGenerator {

    private WaywardWorldgen plugin;

    public MiningGenerator(WaywardWorldgen plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        List<BlockPopulator> populators = new ArrayList<>();
        populators.add(new OrePopulator(plugin));
        populators.add(new CavePopulator(plugin));
        return populators;
    }

    @SuppressWarnings("deprecation")
    private void setBlock(byte[][] result, int x, int y, int z, Material material) {
        if (result[y >> 4] == null) {
            result[y >> 4] = new byte[4096];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = (byte) material.getId();
    }

    @SuppressWarnings("deprecation")
    @Override
    public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid){
        byte[][] result = new byte[world.getMaxHeight() / 16][];
        int x, y, z;
        for (x = 0; x < 16; x++) {
            for (z = 0; z < 16; z++) {
                setBlock(result, x, 0, z, Material.BEDROCK);
                setBlock(result, x, world.getMaxHeight() - 1, z, Material.BEDROCK);
                for (y = 1; y < world.getMaxHeight() - 1; y++) {
                    setBlock(result, x, y, z, Material.STONE);
                }
            }
        }
        return result;
    }

}
