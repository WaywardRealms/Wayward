package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SpellBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

public class DrawSwordSpell extends SpellBase {

    public DrawSwordSpell() {
        setName("DrawSword");
        setManaCost(50);
        setCoolDown(300);
    }

    @Override
    public boolean use(Player player) {
        Block block = player.getWorld().getBlockAt(player.getLocation()).getRelative(BlockFace.DOWN);
        ItemStack sword = null;
        switch (block.getType()) {
            case WOOD: sword = new ItemStack(Material.WOOD_SWORD); sword.setDurability((short) 45); break;
            case LOG:case LOG_2: sword = new ItemStack(Material.WOOD_SWORD); sword.setDurability((short) 30); break;
            case STONE:case COBBLESTONE:case SMOOTH_BRICK:case MOSSY_COBBLESTONE: sword = new ItemStack(Material.STONE_SWORD); sword.setDurability((short) 99); break;
            case IRON_ORE: sword = new ItemStack(Material.IRON_SWORD); sword.setDurability((short) 186); break;
            case IRON_BLOCK: sword = new ItemStack(Material.IRON_SWORD); sword.setDurability((short) 125); break;
            case GOLD_ORE: sword = new ItemStack(Material.GOLD_SWORD); sword.setDurability((short) 24); break;
            case GOLD_BLOCK: sword = new ItemStack(Material.GOLD_SWORD); sword.setDurability((short) 16); break;
            case DIAMOND_ORE: sword = new ItemStack(Material.DIAMOND_SWORD); sword.setDurability((short) 1171); break;
            case DIAMOND_BLOCK: sword = new ItemStack(Material.DIAMOND_SWORD); sword.setDurability((short) 781); break;
        }
        if (sword != null) {
            block.setType(Material.AIR);
            if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
                player.getWorld().dropItem(player.getLocation(), player.getItemInHand());
            }
            player.setItemInHand(sword);
            return true;
        }
        return false;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + " drew a sword from the ground!");
        return use((attacking).getPlayer().getPlayer());
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.STONE_SWORD);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Draw Sword");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return true;
    }

    @Override
    public boolean canUse(Combatant combatant) {
        return canUse((Character) combatant);
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
        return "Draw a sword from the material you are standing on";
    }

}
