package net.wayward_realms.waywardmonsters.target;

import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardmonsters.WaywardMonsters;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.util.BlockIterator;

public class TargetProjectileHitListener implements Listener {

    private WaywardMonsters plugin;

    public TargetProjectileHitListener(WaywardMonsters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            BlockIterator iterator = new BlockIterator(event.getEntity().getWorld(), event.getEntity().getLocation().toVector(), event.getEntity().getVelocity().normalize(), 0, 4);
            Block hit = null;
            while (iterator.hasNext()) {
                hit = iterator.next();
                if (hit.getType().isSolid()) {
                    break;
                }
            }
            if (hit != null) {
                for (int i = 0; i < 6; i++) {
                    Block block = hit.getRelative(BlockFace.DOWN, i);
                    if (block.getState() instanceof Sign) {
                        Sign sign = (Sign) block.getState();
                        if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_RED + "[target]")) {
                            if (hit.getLocation().distanceSquared(player.getLocation()) >= 256) {
                                int uses = 0;
                                try {
                                    uses = Integer.parseInt(sign.getLine(1));
                                } catch (NumberFormatException exception) {
                                    player.sendMessage(ChatColor.RED + "Could not parse target hits");
                                }
                                if (uses >= 4) {
                                    RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = plugin.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
                                    if (skillsPluginProvider != null) {
                                        SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
                                        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                                        if (characterPluginProvider != null) {
                                            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                                            skillsPlugin.giveExperience(characterPlugin.getActiveCharacter(player), 5);
                                        }
                                    }
                                    uses = 0;
                                } else {
                                    uses++;
                                }
                                player.getWorld().playEffect(player.getLocation(), Effect.ZOMBIE_CHEW_WOODEN_DOOR, 0);
                                player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + "Hit!");
                                player.sendMessage(plugin.getPrefix() + ChatColor.GREEN + (5 - uses) + " hits remaining!");
                                sign.setLine(1, "" + uses);
                                sign.update();
                            } else {
                                player.sendMessage(plugin.getPrefix() + ChatColor.RED + "Please move further away.");
                            }
                        }
                    }
                }
            }
        }
    }

}
