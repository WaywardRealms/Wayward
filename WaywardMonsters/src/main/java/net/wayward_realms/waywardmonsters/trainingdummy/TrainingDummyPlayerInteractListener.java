package net.wayward_realms.waywardmonsters.trainingdummy;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.professions.ToolType;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardmonsters.WaywardMonsters;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

public class TrainingDummyPlayerInteractListener implements Listener {

    private WaywardMonsters plugin;

    public TrainingDummyPlayerInteractListener(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            for (int i = 0; i < 6; i++) {
                Block block = event.getClickedBlock().getRelative(BlockFace.DOWN, i);
                if (block.getState() instanceof Sign) {
                    Sign sign = (Sign) block.getState();
                    if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_RED + "[dummy]")) {
                        int uses = 0;
                        try {
                            uses = Integer.parseInt(sign.getLine(1));
                        } catch (NumberFormatException exception) {
                            event.getPlayer().sendMessage(ChatColor.RED + "Could not parse dummy uses");
                        }
                        if (uses >= 19) {
                            ItemStack item = event.getPlayer().getItemInHand();
                            ToolType toolType = ToolType.getToolType(item.getType());
                            if (toolType != null && toolType == ToolType.SWORD) {
                                item.setDurability((short) (item.getDurability() + 5));
                                if (item.getDurability() >= item.getType().getMaxDurability()) {
                                    event.getPlayer().setItemInHand(null);
                                }
                            } else {
                                event.getPlayer().damage(2D);
                            }
                            RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = plugin.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
                            if (skillsPluginProvider != null) {
                                SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
                                RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = plugin.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                                if (characterPluginProvider != null) {
                                    CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                                    skillsPlugin.giveExperience(characterPlugin.getActiveCharacter(event.getPlayer()), 5);
                                }
                            }
                            uses = 0;
                        } else {
                            uses++;
                        }
                        event.getPlayer().sendMessage(plugin.getPrefix() + ChatColor.GREEN + (20 - uses) + " hits remaining!");
                        sign.setLine(1, "" + uses);
                        sign.update();
                    }
                }
            }
        }
    }

}
