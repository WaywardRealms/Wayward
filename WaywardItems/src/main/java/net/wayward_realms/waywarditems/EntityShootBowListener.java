package net.wayward_realms.waywarditems;

import com.sk89q.jinglenote.MidiJingleSequencer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.File;
import java.io.IOException;

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
                        try {
                            File musicDirectory = new File(plugin.getDataFolder(), "music");
                            plugin.getJingleNoteManager().play(plugin, player.getName(), new MidiJingleSequencer(new File(musicDirectory, "canon.mid"), false));
                        } catch (MidiUnavailableException | InvalidMidiDataException | IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
