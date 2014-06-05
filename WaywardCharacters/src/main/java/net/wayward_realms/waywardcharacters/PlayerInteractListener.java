package net.wayward_realms.waywardcharacters;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.bukkit.block.Biome.*;

public class PlayerInteractListener implements Listener {

    private final WaywardCharacters plugin;

    public PlayerInteractListener(WaywardCharacters plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand() != null) {
            if (event.getPlayer().getItemInHand().getType() == Material.GLASS_BOTTLE) {
                Block targetBlock = getTargetBlock(event.getPlayer());
                if (targetBlock != null) {
                    if (targetBlock.getType() == Material.WATER || targetBlock.getType() == Material.STATIONARY_WATER) {
                        if (targetBlock.getRelative(BlockFace.DOWN, 2).getState() instanceof Sign) {
                            Sign sign = (Sign) targetBlock.getRelative(BlockFace.DOWN, 2).getState();
                            if (sign.getLine(0).equalsIgnoreCase(ChatColor.YELLOW + "[masheekwell]")) {
                                try {
                                    if (Integer.parseInt(sign.getLine(1)) > 0) {
                                        event.setCancelled(true);
                                        if (event.getPlayer().getItemInHand().getAmount() > 1) {
                                            event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
                                        } else {
                                            event.getPlayer().setItemInHand(null);
                                        }
                                        ItemStack masheek = new ItemStack(Material.POTION);
                                        ItemMeta meta = masheek.getItemMeta();
                                        meta.setDisplayName("Masheek");
                                        List<String> lore = new ArrayList<>();
                                        lore.add("+5 mana");
                                        meta.setLore(lore);
                                        masheek.setItemMeta(meta);
                                        event.getPlayer().getInventory().addItem(masheek);
                                        event.getPlayer().updateInventory();
                                        int usesRemaining = Integer.parseInt(sign.getLine(1)) - 1;
                                        sign.setLine(1, "" + usesRemaining);
                                        sign.update();
                                        if (usesRemaining <= 0) {
                                            targetBlock.setType(Material.AIR);
                                            event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.RED + "The Masheek well dried out!");
                                        }
                                    }
                                } catch (NumberFormatException ignored) {}
                            }
                        }
                    }
                }
            }
            if (event.getPlayer().getItemInHand().getType() == Material.AIR) {
                Block targetBlock = getTargetBlock(event.getPlayer());
                if (targetBlock != null) {
                    if (targetBlock.getType() == Material.WATER || targetBlock.getType() == Material.STATIONARY_WATER) {
                        net.wayward_realms.waywardlib.character.Character character = plugin.getActiveCharacter(event.getPlayer());
                        if (plugin.isSafeWater(targetBlock.getBiome())) {
                            character.setThirst(character.getThirst() + 1);
                            event.getPlayer().sendMessage(ChatColor.GREEN + "Thirst: +1" + ChatColor.GRAY + " (Total: " + character.getThirst() + ")");
                        }else{
                            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 1500, 2), true);
                            event.getPlayer().sendMessage(ChatColor.RED + "You feel sick. Perhaps it was the water.");
                            if (targetBlock.getBiome() == OCEAN || targetBlock.getBiome() == DEEP_OCEAN || targetBlock.getBiome() == FROZEN_OCEAN || targetBlock.getBiome() == BEACH || targetBlock.getBiome() == COLD_BEACH || targetBlock.getBiome() == STONE_BEACH) {
                                character.setThirst(character.getThirst() - 2);
                                event.getPlayer().sendMessage(ChatColor.RED + "You suddenly feel dehydrated from drinking salt water.");
                                event.getPlayer().sendMessage(ChatColor.RED + "Thirst: -2" + ChatColor.GRAY + " (Total: " + character.getThirst() + ")");
                            }
                        }
                    }
                }
            } else if (event.getPlayer().getItemInHand().getType() == Material.GLASS_BOTTLE) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    //TODO: Give player waterbottle with biome in lore if not a safe water biome.
                }
            }
        }
    }

    private List<Block> getLineOfSight(Player player, int maxDistance) {
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        ArrayList<Block> blocks = new ArrayList<>();
        Iterator<Block> iterator = new BlockIterator(player, maxDistance);
        while (iterator.hasNext()) {
            Block block = iterator.next();
            Material material = block.getType();
            if (material != Material.AIR) {
                blocks.add(block);
                break;
            }
        }
        return blocks;
    }

    private Block getTargetBlock(Player player) {
        List<Block> lineOfSight = getLineOfSight(player, 5);
        if (lineOfSight.size() > 0) {
            return lineOfSight.get(0);
        }
        return null;
    }
}
