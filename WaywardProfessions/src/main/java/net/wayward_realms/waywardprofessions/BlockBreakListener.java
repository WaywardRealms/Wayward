package net.wayward_realms.waywardprofessions;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.professions.ToolType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
        
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
            if (characterPluginProvider != null) {
                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                net.wayward_realms.waywardlib.character.Character character = characterPlugin.getActiveCharacter(event.getPlayer());
                Material material = event.getBlock().getType();
                List<ItemStack> drops = new ArrayList<>();
                Random random = new Random();
                
                if (material == null || !plugin.canGainCraftEfficency(material))
                    return;
                
                for (ItemStack drop : event.getBlock().getDrops(event.getPlayer().getItemInHand())) {
                    int miningEfficiency = plugin.getMiningEfficiency(character, event.getBlock().getType());
                    int amount = (int) ((double) (random.nextInt(100) <= 75 ? (miningEfficiency > 10 ? random.nextInt(miningEfficiency) + 1 : random.nextInt(10) + 1) : 25) * (4D / 100D) * (double) drop.getAmount());
                    if (amount > 0) {
                        ItemStack newDrop = new ItemStack(drop);
                        newDrop.setAmount(amount);
                        drops.add(newDrop);
                    }
                }
                event.setCancelled(true);
                if (event.getPlayer().getItemInHand() != null) {
                    if (ToolType.getToolType(event.getPlayer().getItemInHand().getType()) != null) {
                        ToolType toolType = ToolType.getToolType(event.getPlayer().getItemInHand().getType());
                        switch (toolType) {
                            case SWORD:
                                if (event.getPlayer().getItemInHand().getDurability() + 2 < event.getPlayer().getItemInHand().getType().getMaxDurability()) {
                                    event.getPlayer().getItemInHand().setDurability((short) (event.getPlayer().getItemInHand().getDurability() + 2));
                                } else {
                                    event.getPlayer().setItemInHand(null);
                                }
                                break;
                            case PICKAXE:
                            case AXE:
                            case SPADE:
                                if (event.getPlayer().getItemInHand().getDurability() + 1 < event.getPlayer().getItemInHand().getType().getMaxDurability()) {
                                    event.getPlayer().getItemInHand().setDurability((short) (event.getPlayer().getItemInHand().getDurability() + 1));
                                } else {
                                    event.getPlayer().setItemInHand(null);
                                }
                                break;
                        }
                    }
                }
                event.getBlock().setType(Material.AIR);
                for (ItemStack drop : drops) {
                    event.getBlock().getWorld().dropItem(event.getBlock().getLocation().add(0.5D, 0.5D, 0.5D), drop);
                }
                plugin.setMiningEfficiency(character, material, plugin.getMiningEfficiency(character, material) + (random.nextInt(100) <= 30 ? random.nextInt(3) : 0));
            }
        }
    }
}
