package net.wayward_realms.waywardenvironment;

import net.wayward_realms.waywardlib.environment.EnvironmentPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WaywardEnvironment extends JavaPlugin implements EnvironmentPlugin {

    @Override
    public void onEnable(){

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