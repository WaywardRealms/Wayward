package net.wayward_realms.waywarditems;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class EntityShootBowListener implements Listener {

    private WaywardItems plugin;

    public EntityShootBowListener(WaywardItems plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            if (plugin.getMaterial("Harp").isMaterial(event.getBow())) {
                event.setCancelled(true);
                for (Player player : event.getEntity().getWorld().getPlayers()) {
                    if (player.getLocation().distanceSquared(event.getEntity().getLocation()) <= 64) {
                        player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Note.Tone.C)); //TODO: Add some actual midi files we can play here!
                    }
                }
            }
        }
    }

}
