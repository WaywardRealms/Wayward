package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.Spell;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class EsunaSpell implements Spell {

    private String name = "Esuna";
    private int manaCost = 5;
    private int radius = 8;
    private int coolDown = 5;
    private SkillType type = SkillType.MAGIC_HEALING;

    @Override
    public boolean use(Player player) {
        for (LivingEntity entity : player.getWorld().getEntitiesByClass(LivingEntity.class)) {
            if (player.getLocation().distance(entity.getLocation()) <= radius) {
                for (PotionEffectType potionEffectType : PotionEffectType.values()) {
                    if (potionEffectType != null) {
                        entity.addPotionEffect(new PotionEffect(potionEffectType, 0, 0), true);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon) {
        if (((Character) attacking).getMana() >= manaCost) {
            for (PotionEffectType potionEffectType : PotionEffectType.values()) {
                ((Character) defending).getPlayer().getPlayer().addPotionEffect(new PotionEffect(potionEffectType, 0, 0), true);
            }
            for (Character character : fight.getCharacters()) {
                if (character.getPlayer().isOnline()) {
                    character.getPlayer().getPlayer().sendMessage(ChatColor.YELLOW + attacking.getName() + " cured " + defending.getName() + "'s status effects");
                }
            }
            return true;
        } else {
            for (Character character : fight.getCharacters()) {
                if (character.getPlayer().isOnline()) {
                    character.getPlayer().getPlayer().sendMessage(ChatColor.YELLOW + attacking.getName() + " attempted to cure " + defending.getName() + "'s status effects, but did not have enough mana!");
                }
            }
            return false;
        }
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Esuna");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_HEALING) >= 1;
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
        serialised.put("mana-cost", manaCost);
        serialised.put("radius", radius);
        serialised.put("cooldown", coolDown);
        return serialised;
    }

    public static EsunaSpell deserialize(Map<String, Object> serialised) {
        EsunaSpell deserialised = new EsunaSpell();
        deserialised.name = (String) serialised.get("name");
        deserialised.manaCost = (int) serialised.get("mana-cost");
        deserialised.radius = (int) serialised.get("radius");
        deserialised.coolDown = (int) serialised.get("cooldown");
        return deserialised;
    }

}
