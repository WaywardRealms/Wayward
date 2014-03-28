package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.Spell;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class EmpowerSpell implements Spell {

    private String name = "Empower";
    private int coolDown = 600;
    private int manaCost = 10;
    private SkillType type;

    @Override
    public boolean use(Player player) {
        for (Player player1 : player.getWorld().getPlayers()) {
            if (player1.getLocation().distanceSquared(player.getLocation()) <= 64) {
                player1.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2400, 2));
                player1.getWorld().playEffect(player1.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
                player1.sendMessage(ChatColor.GOLD + "You feel empowered!");
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
        ItemStack icon = new ItemStack(Material.EXP_BOTTLE);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Empower");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.SUPPORT_PERFORM) >= 7;
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
        serialised.put("cooldown", coolDown);
        serialised.put("mana-cost", manaCost);
        return serialised;
    }

    public static EmpowerSpell deserialize(Map<String, Object> serialised) {
        EmpowerSpell deserialised = new EmpowerSpell();
        deserialised.name = (String) serialised.get("name");
        deserialised.coolDown = (int) serialised.get("cooldown");
        deserialised.manaCost = (int) serialised.get("mana-cost");
        return deserialised;
    }

}
