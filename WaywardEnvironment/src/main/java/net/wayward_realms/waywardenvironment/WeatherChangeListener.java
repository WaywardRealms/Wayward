package net.wayward_realms.waywardenvironment;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.Random;

public class WeatherChangeListener implements Listener{

    private WaywardEnvironment plugin;

    WeatherChangeListener(WaywardEnvironment inplugin){
        this.plugin = inplugin;
    }

    @EventHandler
public void onWeatherChange(WeatherChangeEvent event){
    String worldname = event.getWorld().getName();
    if(plugin.getConfig().getBoolean("worlds." + worldname + ".modifyWeather")) {
        Random randomizer = new Random();
        int entryPrecipitation = plugin.getConfig().getInt("worlds." + worldname + ".precipitationPercentile");
        int entryThunder = plugin.getConfig().getInt("worlds." + worldname + ".precipitationPercentile");
            if (((Number) entryPrecipitation).intValue() >= 0 && ((Number) entryPrecipitation).intValue() <= 100) {
                if (randomizer.nextInt(101) > ((Number) entryPrecipitation).intValue()) {
                    event.setCancelled(true);
                    event.getWorld().setStorm(false);
                } else if (randomizer.nextInt(101) <= ((Number) entryPrecipitation).intValue()) {
                    event.setCancelled(true);
                    event.getWorld().setStorm(true);
                        if (((Number) entryThunder).intValue() >= 0 && ((Number) entryThunder).intValue() <= 100) {
                            if (randomizer.nextInt(101) > ((Number) entryThunder).intValue()) {
                                event.getWorld().setThundering(false);
                            } else if (randomizer.nextInt(101) >= ((Number) entryThunder).intValue()) {
                                event.getWorld().setThundering(true);
                            }
                        }
                    }
                }
            }
        }
    }
