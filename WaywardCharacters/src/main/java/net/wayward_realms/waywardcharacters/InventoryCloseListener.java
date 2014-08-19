package net.wayward_realms.waywardcharacters;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.Equipment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryCloseListener implements Listener {

    private WaywardCharacters plugin;

    public InventoryCloseListener(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase("Equipment")) {
            if (event.getPlayer() instanceof Player) {
                Player player = (Player) event.getPlayer();
                Character character = plugin.getActiveCharacter(player);
                Equipment equipment = new EquipmentImpl(
                        event.getInventory().getItem(10),
                        event.getInventory().getItem(11),
                        null,
                        new ItemStack[] {
                                event.getInventory().getItem(6),
                                event.getInventory().getItem(7),
                                event.getInventory().getItem(8),
                                event.getInventory().getItem(15),
                                event.getInventory().getItem(16),
                                event.getInventory().getItem(17),
                                event.getInventory().getItem(24),
                                event.getInventory().getItem(25),
                                event.getInventory().getItem(26)
                        }
                );
                character.setEquipment(equipment);
            }
        }
    }

}
