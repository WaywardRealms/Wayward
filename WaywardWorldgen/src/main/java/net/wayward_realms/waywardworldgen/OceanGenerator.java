package net.wayward_realms.waywardworldgen;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class OceanGenerator extends ChunkGenerator {

    @SuppressWarnings("deprecation") //TODO: Find a way to do this without using deprecated methods
    private void setBlock(byte[][] result, int x, int y, int z, Material material) {
        byte blockId = (byte) material.getId();
        if (result[y >> 4] == null) {
            result[y >> 4] = new byte[4096];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blockId;
    }

    @SuppressWarnings("deprecation")
    @Override
    public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid){
        byte[][] result = new byte[world.getMaxHeight() / 16][];
        int x, y, z;
        for (x = 0; x < 16; x++) {
            for (z = 0; z < 16; z++) {
                for (y = 0; y < 1; y++) {
                    setBlock(result, x, y, z, Material.BEDROCK);
                }
                for (; y < 32; y++) {
                    setBlock(result, x, y, z, Material.SAND);
                }
                for (; y < 64; y++) {
                    setBlock(result, x, y, z, Material.WATER);
                }
            }
        }
        return result;
    }

}
