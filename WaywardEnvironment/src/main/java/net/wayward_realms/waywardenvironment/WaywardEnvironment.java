package net.wayward_realms.waywardenvironment;

import net.wayward_realms.waywardlib.environment.EnvironmentPlugin;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class WaywardEnvironment extends JavaPlugin implements EnvironmentPlugin {

    @Override
    public void onEnable(){
        getServer().getScheduler().runTaskTimerAsynchronously(this, new AdjustTimeRunnable(this), 3000L, 3000L);
    }

    @Override
    public void onDisable(){

    }

    public long getTime(World world){
        return world.getTime();
    }

    public void setTime(World world, int time){

    }

    public String getWeather(World world){
        return "";
    }

    public void setWeather(World world, String weather){

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