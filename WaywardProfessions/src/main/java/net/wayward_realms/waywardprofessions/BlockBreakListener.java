package net.wayward_realms.waywardprofessions;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockBreakListener implements Listener {

    private final WaywardProfessions plugin;

    public BlockBreakListener(WaywardProfessions plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            net.wayward_realms.waywardlib.character.Character character = characterPlugin.getActiveCharacter(event.getPlayer());
            Material material = event.getBlock().getType();
            List<ItemStack> drops = new ArrayList<>();
            Random random = new Random();
            for (ItemStack drop : event.getBlock().getDrops()) {
                int amount = (int) ((double) random.nextInt(plugin.getMiningEfficiency(character, event.getBlock().getType())) * (4D / 100D) * (double) drop.getAmount());
                drops.add(new ItemStack(drop.getType(), amount));
            }
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
            for (ItemStack drop : drops) {
                event.getBlock().getWorld().dropItem(event.getBlock().getLocation().add(0.5D, 0.5D, 0.5D), drop);
            }
            plugin.setMiningEfficiency(character, material, plugin.getMiningEfficiency(character, material) + (random.nextInt(100) <= 30 ? random.nextInt(3) : 0));
        }
    }
}
