package net.wayward_realms.waywardmonsters;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.ClassesPlugin;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EntityLevelManager {

    private WaywardMonsters plugin;

    private File levelMapFile;
    private BufferedImage levelMap;

    public EntityLevelManager(WaywardMonsters plugin) {
        this.plugin = plugin;
        levelMapFile = new File(plugin.getDataFolder(), "level-map");
        if (levelMapFile.exists()) {
            try {
                levelMap = ImageIO.read(levelMapFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            levelMap = new BufferedImage(256, 256, BufferedImage.TYPE_BYTE_GRAY);
            try {
                ImageIO.write(levelMap, "png", levelMapFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public int getChunkEntityLevel(Chunk chunk) {
        Chunk spawnChunk = chunk.getWorld().getSpawnLocation().getChunk();
        int xOrigin = spawnChunk.getX() - 128;
        int yOrigin = spawnChunk.getZ() - 128;
        int x = chunk.getX() - xOrigin;
        int y = chunk.getZ() - yOrigin;
        if (x >= 0 && x < levelMap.getWidth() && y >= 0 && y < levelMap.getHeight()) {
            return (levelMap.getRGB(x, y) & 0xff);
        } else {
            return 0;
        }
    }

    public void setChunkEntityLevel(Chunk chunk, int level, int radius) {
        Chunk spawnChunk = chunk.getWorld().getSpawnLocation().getChunk();
        int xOrigin = spawnChunk.getX() - 128;
        int yOrigin = spawnChunk.getZ() - 128;
        int x = chunk.getX() - xOrigin;
        int y = chunk.getZ() - yOrigin;
        if (x >= 0 && x < levelMap.getWidth() && y >= 0 && y < levelMap.getHeight()) {
            Graphics2D graphics = levelMap.createGraphics();
            for (int i = radius; i > 0; i--) {
                int c = level - (int) Math.round(((double) i / (double) radius) * (double) level);
                graphics.setColor(new Color(c, c, c));
                graphics.fillRect(x - i, y - i, i * 2, i * 2);
            }
            graphics.dispose();
            try {
                ImageIO.write(levelMap, "png", levelMapFile);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public int getEntityLevel(Entity entity) {
        if (entity instanceof Player) {
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (characterPluginProvider != null) {
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                RegisteredServiceProvider<ClassesPlugin> classesPluginProvider = plugin.getServer().getServicesManager().getRegistration(ClassesPlugin.class);
                if (classesPluginProvider != null) {
                    ClassesPlugin classesPlugin = classesPluginProvider.getProvider();
                    return classesPlugin.getLevel(characterPlugin.getActiveCharacter((Player) entity));
                }
            }
        }
        Chunk entityChunk = entity.getLocation().getChunk();
        return getChunkEntityLevel(entityChunk);
    }

    public int getEntityStatValue(Entity entity) {
        return (int) Math.round(((200D * (double) getEntityLevel(entity)) / 100D) + 5D);
    }

}
