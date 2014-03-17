package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.*;
import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.Spell;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;

public class BurnSpell implements Spell {

    private String name = "Burn";
    private int manaCost = 15;
    private int radius = 8;
    private int fireTicks = 200;
    private int coolDown = 5;
    private SkillType type = SkillType.MAGIC_OFFENCE;

    @Override
    public boolean use(Player player) {
        for (Entity entity : player.getWorld().getEntities()) {
            if (player.getLocation().distance(entity.getLocation()) <= radius) {
                if (entity != player) {
                    entity.setFireTicks(fireTicks);
                }
            }
        }
        return true;
    }

    @Override
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon) {
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.TORCH);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Burn");
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

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_OFFENCE) >= 20;
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
    public int getManaCost() {
        return manaCost;
    }

    @Override
    public void setManaCost(int cost) {
        this.manaCost = cost;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", name);
        serialised.put("mana-cost", manaCost);
        serialised.put("radius", radius);
        serialised.put("fire-ticks", fireTicks);
        serialised.put("cooldown", coolDown);
        return serialised;
    }

    public BurnSpell deserialize(Map<String, Object> serialised) {
        BurnSpell deserialised = new BurnSpell();
        deserialised.name = (String) serialised.get("name");
        deserialised.manaCost = (int) serialised.get("mana-cost");
        deserialised.radius = (int) serialised.get("radius");
        deserialised.fireTicks = (int) serialised.get("fire-ticks");
        deserialised.coolDown = (int) serialised.get("cooldown");
        return deserialised;
    }

}
