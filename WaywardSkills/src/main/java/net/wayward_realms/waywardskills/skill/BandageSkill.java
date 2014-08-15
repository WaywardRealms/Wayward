package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.items.ItemsPlugin;
import net.wayward_realms.waywardlib.skills.SkillBase;
import net.wayward_realms.waywardlib.util.vector.Vector3D;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

public class BandageSkill extends SkillBase {

    private int reach = 16;
    private double healthRestore = 10D;

    public BandageSkill() {
        setName("Bandage");
        setCoolDown(30);
    }

    public int getReach() {
        return reach;
    }

    public void setReach(int reach) {
        this.reach = reach;
    }

    public double getHealthRestore() {
        return healthRestore;
    }

    public void setHealthRestore(double healthRestore) {
        this.healthRestore = healthRestore;
    }

    @Override
    public boolean use(Player player) {
        if (player.getItemInHand() != null) {
            RegisteredServiceProvider<ItemsPlugin> itemsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ItemsPlugin.class);
            if (itemsPluginProvider != null) {
                ItemsPlugin itemsPlugin = itemsPluginProvider.getProvider();
                if (itemsPlugin.getMaterial("Bandage").isMaterial(player.getItemInHand())) {
                    Location observerPos = player.getEyeLocation();
                    Vector3D observerDir = new Vector3D(observerPos.getDirection());
                    Vector3D observerStart = new Vector3D(observerPos);
                    Vector3D observerEnd = observerStart.add(observerDir.multiply(reach));
                    // Get nearby entities
                    for (Player target : player.getWorld().getPlayers()) {
                        // Bounding box of the given player
                        Vector3D targetPos = new Vector3D(target.getLocation());
                        Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
                        Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);
                        if (target != player && hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                            RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
                            if (characterPluginProvider != null) {
                                CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
                                Character character = characterPlugin.getActiveCharacter(target);
                                target.setHealth(character.getHealth());
                                character.setHealth(Math.min(character.getHealth() + healthRestore, character.getMaxHealth()));
                                target.setHealth(character.getHealth());
                                target.sendMessage(ChatColor.GREEN + player.getDisplayName() + " applied a bandage to your wounds.");
                                player.sendMessage(ChatColor.GREEN + "Applied a bandage to " + target.getDisplayName() + "'s wounds.");
                                return true;
                            }
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You must be holding a bandage to apply it.");
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "You must be holding a bandage to apply it.");
        }
        return false;
    }

    private boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
        final double epsilon = 0.0001f;
        Vector3D d = p2.subtract(p1).multiply(0.5);
        Vector3D e = max.subtract(min).multiply(0.5);
        Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
        Vector3D ad = d.abs();
        return !(Math.abs(c.x) > e.x + ad.x || Math.abs(c.y) > e.y + ad.y || Math.abs(c.z) > e.z + ad.z || Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon || Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon || Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + epsilon);
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        RegisteredServiceProvider<ItemsPlugin> itemsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ItemsPlugin.class);
        if (itemsPluginProvider != null) {
            ItemsPlugin itemsPlugin = itemsPluginProvider.getProvider();
            if (itemsPlugin.getMaterial("Bandage").isMaterial(weapon)) {
                defending.setHealth(defending.getHealth() + getHealthRestore());
                attacking.getPlayer().getPlayer().getInventory().removeItem(itemsPlugin.createNewItemStack(itemsPlugin.getMaterial("Bandage"), 1).toMinecraftItemStack());
                fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " applied a bandage to " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()));
                return true;
            } else {
                fight.sendMessage(ChatColor.RED + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.RED + " attempted to use " + weapon.getType().toString().toLowerCase().replace('_', ' ') + " as a bandage. It didn't work very well.");
            }
        }
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.PAPER);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName("Bandage");
        icon.setItemMeta(iconMeta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return true;
    }

    @Override
    public String getDescription() {
        return "Restores " + (int) Math.round(getHealthRestore()) + " HP to one target, and uses one bandage";
    }

}
