package net.wayward_realms.waywardworldgen;

import net.wayward_realms.waywardlib.worldgen.WorldgenPlugin;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.bukkit.Material.*;

public class WaywardWorldgen extends JavaPlugin implements WorldgenPlugin {

    @Override
    public void onEnable() {
        saveDefaultMiningConfig();
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        switch (id) {
            case "ocean": return new OceanGenerator();
            case "mining": return new MiningGenerator(this);
            default: return new ChunkGenerator() {
                @SuppressWarnings("deprecation")
                public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
                    return new byte[world.getMaxHeight() / 16][];
                }
            };
        }
    }

    public void saveDefaultMiningConfig() {
        File miningFile = new File(getDataFolder(), "mining.yml");
        YamlConfiguration miningConfig = new YamlConfiguration();
        List<Pocket> pockets = new ArrayList<>();
        pockets.add(new Pocket(AIR, 30, 20));
        pockets.add(new Pocket(COAL_ORE, 10, 60));
        pockets.add(new Pocket(IRON_ORE, 7, 45));
        pockets.add(new Pocket(GOLD_ORE, 4, 24));
        pockets.add(new Pocket(DIAMOND_ORE, 4, 6));
        pockets.add(new Pocket(REDSTONE_ORE, 12, 36));
        pockets.add(new Pocket(EMERALD_ORE, 4, 6));
        pockets.add(new Pocket(LAPIS_ORE, 4, 24));
        pockets.add(new Pocket(GRAVEL, 10, 60));
        pockets.add(new Pocket(LAVA, 10, 2));
        pockets.add(new Pocket(WATER, 10, 2));
        pockets.add(new Pocket(CLAY, 7, 60));
        pockets.add(new Pocket(QUARTZ_ORE, 4, 30));
        pockets.add(new Pocket(GLOWSTONE, 8, 36));
        miningConfig.set("pockets", pockets);
        miningConfig.set("cave-height", 192);
        miningConfig.set("cave-centre", 128);
        try {
            miningConfig.save(miningFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String getPrefix() {
        return "";
    }

    @Override
    public void loadState() {

    }

    @Override
    public void saveState() {

    }
}
