package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillType;
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

import java.util.HashMap;
import java.util.Map;

public class DrawSwordSpell extends SpellBase {

    private String name = "DrawSword";
    private int manaCost = 50;
    private int coolDown = 300;
    private SkillType type = SkillType.MAGIC_SWORD;

    @Override
    public int getManaCost() {
        return manaCost;
    }

    @Override
    public void setManaCost(int cost) {
        this.manaCost = cost;
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
            player.getWorld().dropItem(player.getLocation(), player.getItemInHand());
            player.setItemInHand(sword);
            return true;
        }
        return false;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        fight.sendMessage(ChatColor.YELLOW + attacking.getName() + " drew a sword from the ground!");
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
        return character.getSkillPoints(SkillType.MAGIC_SWORD) >= 20;
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public SkillType getType() {
        return type;
    }

    @Override
    public void setType(SkillType type) {
        this.type = type;
    }

    @Override
    public int getCoolDown() {
        return coolDown;
    }

    @Override
    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", getName());
        serialised.put("mana-cost", getManaCost());
        serialised.put("cooldown", getCoolDown());
        return serialised;
    }

    public static DrawSwordSpell deserialize(Map<String, Object> serialised) {
        DrawSwordSpell deserialised = new DrawSwordSpell();
        deserialised.setName((String) serialised.get("name"));
        deserialised.setManaCost((int) serialised.get("mana-cost"));
        deserialised.setCoolDown((int) serialised.get("cooldown"));
        return deserialised;
    }

}
