package net.wayward_realms.waywardmonsters;

import org.bukkit.Material;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class CreatureSpawnListener implements Listener {

    private WaywardMonsters plugin;

    public CreatureSpawnListener(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (event.getEntity() != null) {
            if (event.getEntity() instanceof Monster) {
                EntityLevelManager entityLevelManager = plugin.getEntityLevelManager();
                int level = entityLevelManager.getEntityLevel(event.getEntity());
                if (level == 0) {
                    event.setCancelled(true);
                } else {
                    EntityEquipment equipment = event.getEntity().getEquipment();
                    if (level > 5 && level <= 10) {
                        equipment.setHelmet(new ItemStack(Material.LEATHER_HELMET));
                        equipment.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                        equipment.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                        equipment.setBoots(new ItemStack(Material.LEATHER_BOOTS));
                    } else if (level > 10 && level <= 15) {
                        equipment.setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
                        equipment.setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
                        equipment.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
                        equipment.setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
                    } else if (level > 15 && level <= 20) {
                        equipment.setHelmet(new ItemStack(Material.IRON_HELMET));
                        equipment.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                        equipment.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                        equipment.setBoots(new ItemStack(Material.IRON_BOOTS));
                    } else if (level > 20 && level <= 25) {
                        equipment.setHelmet(new ItemStack(Material.GOLD_HELMET));
                        equipment.setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
                        equipment.setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
                        equipment.setBoots(new ItemStack(Material.GOLD_BOOTS));
                    } else if (level > 25) {
                        equipment.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                        equipment.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                        equipment.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                        equipment.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
                    }
                }
            }
        }
    }

}
