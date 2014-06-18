package net.wayward_realms.waywardworldgen;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.io.File;
import java.util.Random;

public class CavePopulator extends BlockPopulator {

    private int maxCaveHeight;
    private int caveCentre;

    public CavePopulator(WaywardWorldgen plugin) {
        File miningFile = new File(plugin.getDataFolder(), "mining.yml");
        YamlConfiguration miningConfig = YamlConfiguration.loadConfiguration(miningFile);
        this.maxCaveHeight = miningConfig.getInt("cave-height", 192);
        this.caveCentre = miningConfig.getInt("cave-centre", 128);
    }

    @Override
    public void populate(World world, Random random, Chunk source) {
        int worldChunkX = source.getX() * 16;
        int worldChunkZ = source.getZ() * 16;
        int x, y, z;
        SimplexOctaveGenerator noiseGenerator = new SimplexOctaveGenerator(world, 8);
        noiseGenerator.setScale(1 / 32.0);
        for (x = worldChunkX; x < worldChunkX + 16; x++) {
            for (z = worldChunkZ; z < worldChunkZ + 16; z++) {
                for (y = world.getMaxHeight() - caveCentre; y > world.getMaxHeight() - (caveCentre + (noiseGenerator.noise(x, z, 0.5, 0.5) * (maxCaveHeight / 2))); y--) {
                    if (world.getBlockAt(x, y, z).getType() != Material.AIR) {
                        world.getBlockAt(x, y, z).setType(source.getX() % 4 == 0 && source.getZ() % 4 == 0 && (x - worldChunkX == 12 || x - worldChunkX == 15) && (z - worldChunkZ == 12 || z - worldChunkZ == 15) ? Material.LOG : Material.AIR);
                    }
                }
                for (y = world.getMaxHeight() - caveCentre; y < world.getMaxHeight() - (caveCentre - (noiseGenerator.noise(x, z, 0.5, 0.5) * (maxCaveHeight / 2))); y++) {
                    if (world.getBlockAt(x, y, z).getType() != Material.AIR) {
                        world.getBlockAt(x, y, z).setType(source.getX() % 4 == 0 && source.getZ() % 4 == 0 && (x - worldChunkX == 12 || x - worldChunkX == 15) && (z - worldChunkZ == 12 || z - worldChunkZ == 15) && y < 144 ? Material.LOG : Material.AIR);
                    }
                }
            }
        }
        if (source.getX() % 4 == 0) {
            y = 144;
            for (x = worldChunkX + 12; x < worldChunkX + 16; x++) {
                for (z = worldChunkZ; z < worldChunkZ + 16; z++) {
                    if (world.getBlockAt(x, y, z).getType() == Material.AIR) {
                        world.getBlockAt(x, y, z).setType(Material.WOOD);
                    }
                }
            }
        }
        if (source.getZ() % 4 == 0) {
            y = 112;
            for (x = worldChunkX; x < worldChunkX + 16; x++) {
                for (z = worldChunkZ + 12; z < worldChunkZ + 16; z++) {
                    if (world.getBlockAt(x, y, z).getType() == Material.AIR) {
                        world.getBlockAt(x, y, z).setType(Material.WOOD);
                    }
                }
            }
        }
    }

}
