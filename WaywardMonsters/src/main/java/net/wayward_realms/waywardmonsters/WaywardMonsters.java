package net.wayward_realms.waywardmonsters;

import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.monsters.MonstersPlugin;
import net.wayward_realms.waywardmonsters.bleed.BleedBlockBreakListener;
import net.wayward_realms.waywardmonsters.bleed.BleedCommand;
import net.wayward_realms.waywardmonsters.bleed.BleedEntityDamageListener;
import net.wayward_realms.waywardmonsters.bleed.BleedTask;
import net.wayward_realms.waywardmonsters.drops.MobDrop;
import net.wayward_realms.waywardmonsters.drops.MobDropManager;
import net.wayward_realms.waywardmonsters.target.TargetProjectileHitListener;
import net.wayward_realms.waywardmonsters.target.TargetSignChangeListener;
import net.wayward_realms.waywardmonsters.trainingdummy.TrainingDummyPlayerInteractListener;
import net.wayward_realms.waywardmonsters.trainingdummy.TrainingDummySignChangeListener;
import org.bukkit.Chunk;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import static net.wayward_realms.waywardlib.util.plugin.ListenerUtils.registerListeners;

public class WaywardMonsters extends JavaPlugin implements MonstersPlugin {

    private EntityLevelManager entityLevelManager;
    private MobDropManager mobDropManager;
    private BleedTask bleedTask;

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(MobDrop.class);
        saveDefaultConfig();
        entityLevelManager = new EntityLevelManager(this);
        mobDropManager = new MobDropManager(this);
        registerListeners(
                this,
                new BleedEntityDamageListener(this), new BleedBlockBreakListener(),
                new CreatureSpawnListener(this), new EntityDamageByEntityListener(this), new EntityDeathListener(this), new PlayerInteractEntityListener(), new PlayerFishListener(this),
                new TargetProjectileHitListener(this), new TargetSignChangeListener(this),
                new TrainingDummyPlayerInteractListener(this), new TrainingDummySignChangeListener(this)
                );
        getCommand("bleed").setExecutor(new BleedCommand(this));
        getCommand("entitylevel").setExecutor(new EntityLevelCommand(this));
        bleedTask = new BleedTask(this);
        getServer().getScheduler().runTaskTimer(this, bleedTask, 10L, 10L);
    }

    @Override
    public void onDisable() {
        saveState();
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

    public void bleed(LivingEntity entity) {
        bleedTask.add(entity);
    }

    public int getChunkEntityLevel(Chunk chunk) {
        return getEntityLevelManager().getChunkEntityLevel(chunk);
    }

    public void setChunkEntityLevel(Chunk chunk, int level, int radius) {
        getEntityLevelManager().setChunkEntityLevel(chunk, level, radius);
    }

}
