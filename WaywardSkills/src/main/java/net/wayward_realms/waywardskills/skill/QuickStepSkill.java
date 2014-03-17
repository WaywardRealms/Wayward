package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.*;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.Skill;
import net.wayward_realms.waywardlib.skills.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.util.BlockIterator;

import java.util.*;

public class QuickStepSkill implements Skill {

    private String name = "QuickStep";
    private int coolDown = 30;
    private SkillType type = SkillType.SPEED_NIMBLE;

    @Override
    public boolean use(Player player) {
        List<Block> blocks = getLineOfSight(player, 6);
        float yaw = player.getLocation().getYaw();
        Block block = blocks.get(blocks.size() - 1);
        player.teleport(block.getLocation());
        player.getLocation().setYaw(yaw);
        return true;
    }

    @Override
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon) {
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

    private List<Block> getLineOfSight(Player player, int maxDistance) {
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        ArrayList<Block> blocks = new ArrayList<>();
        Iterator<Block> iterator = new BlockIterator(player, maxDistance);
        while (iterator.hasNext()) {
            Block block = iterator.next();
            blocks.add(block);
            Material material = block.getType();
            if (material != Material.AIR) {
                break;
            }
        }
        return blocks;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.SPEED_NIMBLE) >= 5;
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
        serialised.put("name", name);
        serialised.put("cooldown", coolDown);
        return serialised;
    }

    public static QuickStepSkill deserialize(Map<String, Object> serialised) {
        QuickStepSkill deserialised = new QuickStepSkill();
        deserialised.name = (String) serialised.get("name");
        deserialised.coolDown = (int) serialised.get("cooldown");
        return deserialised;
    }

}
