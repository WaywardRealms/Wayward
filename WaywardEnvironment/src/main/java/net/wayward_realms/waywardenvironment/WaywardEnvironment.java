package net.wayward_realms.waywardenvironment;

import net.wayward_realms.waywardlib.environment.EnvironmentPlugin;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class WaywardEnvironment extends JavaPlugin implements EnvironmentPlugin {

    @Override
    public void onEnable(){
        saveDefaultConfig();
        for(World world: getServer().getWorlds()){
            if(getConfig().getBoolean("worlds." + world.getName() + ".modifiedDayNightCycle")){
                world.setGameRuleValue("doDaylightCycle", "false");
                getServer().getScheduler().runTaskTimerAsynchronously(this, new AdjustTimeRunnable(this, world, this.getConfig()), 3000L, 3000L);
            }
        }
        registerListeners(new WeatherChangeListener(this));
    }

    @Override
    public void onDisable(){
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public void loadState() {

    }

    @Override
    public void saveState() {

    }
}