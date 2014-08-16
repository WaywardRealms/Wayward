package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillBase;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.util.BlockIterator;

import java.util.Iterator;

public class QuickStepSkill extends SkillBase {

    private WaywardSkills plugin;

    public QuickStepSkill(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("QuickStep");
        setCoolDown(30);
    }

    @Override
    public boolean use(Player player) {
        Iterator<Block> blockIterator = new BlockIterator(player, 6);
        Block block = player.getWorld().getBlockAt(player.getLocation());
        while (blockIterator.hasNext()) {
            Block next = blockIterator.next();
            if (next.getType().isSolid()) break;
            if (next.getLocation().distanceSquared(player.getLocation()) > 36) break;
            block = next;
        }
        float yaw = player.getLocation().getYaw();
        Location location = block.getLocation();
        location.setYaw(yaw);
        player.teleport(location);
        return true;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("QuickStep");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            return canUse(characterPlugin.getActiveCharacter(player));
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Flees a fight without fail";
    }

    @Override
    public boolean canUse(Character character) {
        return plugin.getSpecialisationValue(character, plugin.getSpecialisation("Nimble")) >= 10;
    }

}
