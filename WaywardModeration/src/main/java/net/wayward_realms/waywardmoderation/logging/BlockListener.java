package net.wayward_realms.waywardmoderation.logging;

import net.wayward_realms.waywardmoderation.WaywardModeration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BlockListener implements Listener {

    private WaywardModeration plugin;

    public BlockListener(WaywardModeration plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlock(final BlockEvent event) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    Method getPlayer = event.getClass().getMethod("getPlayer");
                    plugin.recordBlockChange(event.getBlock(), (Player) getPlayer.invoke(event));
                } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException exception) {
                    plugin.recordBlockChange(event.getBlock());
                }
            }
        }, 5L);
    }

}
