package net.wayward_realms.waywardmonsters;

import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.monsters.MonstersPlugin;
import net.wayward_realms.waywardmonsters.drops.MobDrop;
import net.wayward_realms.waywardmonsters.drops.MobDropManager;
import net.wayward_realms.waywardmonsters.trainingdummy.TrainingDummyPlayerInteractListener;
import net.wayward_realms.waywardmonsters.trainingdummy.TrainingDummySignChangeListener;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

import static net.wayward_realms.waywardlib.util.plugin.ListenerUtils.registerListeners;

public class WaywardMonsters extends JavaPlugin implements MonstersPlugin {

    private EntityLevelManager entityLevelManager;
    private MobDropManager mobDropManager;

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(MobDrop.class);
        saveDefaultConfig();
        entityLevelManager = new EntityLevelManager(this);
        mobDropManager = new MobDropManager(this);
        registerListeners(this, new CreatureSpawnListener(this), new EntityDamageByEntityListener(this), new EntityDeathListener(this), new PlayerInteractEntityListener(), new PlayerFishListener(this),
                new TrainingDummyPlayerInteractListener(this), new TrainingDummySignChangeListener(this));
        getCommand("zeropoint").setExecutor(new ZeroPointCommand(this));
        getCommand("viewzeropoints").setExecutor(new ViewZeroPointsCommand(this));
    }

    @Override
    public void onDisable() {
        saveState();
    }

    @Override
    public Collection<Location> getZeroPoints() {
        return entityLevelManager.getZeroPoints();
    }

    @Override
    public void addZeroPoint(Location zeroPoint) {
        entityLevelManager.addZeroPoint(zeroPoint);
    }

    @Override
    public void removeZeroPoint(Location zeroPoint) {
        entityLevelManager.removeZeroPoint(zeroPoint);
    }

    @Override
    public int getEntityLevel(Entity entity) {
        return entityLevelManager.getEntityLevel(entity);
    }

    @Override
    public int getEntityStatValue(Entity entity, Stat stat) {
        return entityLevelManager.getEntityStatValue(entity);
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

    public EntityLevelManager getEntityLevelManager() {
        return entityLevelManager;
    }

    public MobDropManager getMobDropManager() {
        return mobDropManager;
    }
}
