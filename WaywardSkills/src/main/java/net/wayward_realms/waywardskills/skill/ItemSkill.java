package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.Skill;
import net.wayward_realms.waywardlib.skills.SkillType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;

import java.util.HashMap;
import java.util.Map;

public class ItemSkill implements Skill {

    private String name = "Item";
    private int coolDown = 0;
    private SkillType type = SkillType.SUPPORT_PERFORM;

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
    public boolean use(Player player) {
        return false;
    }

    @Override
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon) {
        return use(fight, (Character) attacking, (Character) defending, weapon);
    }

    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        Player attackingPlayer = attacking.getPlayer().getPlayer();
        Player defendingPlayer = defending.getPlayer().getPlayer();
        if (weapon.getType().isEdible() || weapon.getType() == Material.POTION) {
            switch (weapon.getType()) {
                case GOLDEN_APPLE:
                    attackingPlayer.getInventory().removeItem(weapon);
                    defending.setHealth(Math.min(defending.getHealth() + 10, defending.getMaxHealth()));
                    defendingPlayer.setHealth(defending.getHealth());
                    for (Character character : fight.getCharacters()) {
                        if (character.getPlayer().isOnline()) {
                            Player player = character.getPlayer().getPlayer();
                            player.sendMessage(ChatColor.YELLOW + attacking.getName() + " fed " + defending.getName() + " a golden carrot, healing 5 HP.");
                        }
                    }
                    return true;
                case POTION:
                    if (weapon.hasItemMeta()) {
                        if (weapon.getItemMeta().hasDisplayName()) {
                            if (weapon.getItemMeta().getDisplayName().equalsIgnoreCase("Masheek")) {
                                attackingPlayer.getInventory().removeItem(weapon);
                                defending.setMana(Math.min(defending.getMana() + 5, defending.getMaxMana()));
                                for (Character character : fight.getCharacters()) {
                                    if (character.getPlayer().isOnline()) {
                                        Player player = character.getPlayer().getPlayer();
                                        player.sendMessage(ChatColor.YELLOW + attacking.getName() + " used a bottle of Masheek on " + defending.getName() + ", replenishing 5 mana.");
                                    }
                                }
                                return true;
                            }
                        }
                    }
                    attackingPlayer.getInventory().removeItem(weapon);
                    Potion potion = Potion.fromItemStack(weapon);
                    potion.apply(defendingPlayer);
                    for (Character character : fight.getCharacters()) {
                        if (character.getPlayer().isOnline()) {
                            Player player = character.getPlayer().getPlayer();
                            player.sendMessage(ChatColor.YELLOW + attacking.getName() + " used a potion on " + defending.getName());
                        }
                    }
                    return true;
                default:
                    for (Character character : fight.getCharacters()) {
                        if (character.getPlayer().isOnline()) {
                            Player player = character.getPlayer().getPlayer();
                            player.sendMessage(ChatColor.YELLOW + attacking.getName() + " fed something to " + defending.getName() + " but it had no effect.");
                        }
                    }
                    return true;
            }
        } else {
            for (Character character : fight.getCharacters()) {
                if (character.getPlayer().isOnline()) {
                    Player player = character.getPlayer().getPlayer();
                    player.sendMessage(ChatColor.YELLOW + attacking.getName() + " attempted to use a " + weapon.getType().toString().toLowerCase().replace('_', ' ') + " on " + defending.getName() + " but it didn't seem to do anything.");
                }
            }
            return true;
        }
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
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.POTION);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName("Item");
        icon.setItemMeta(iconMeta);
        return icon;
    }

    @Override
    public boolean canUse(net.wayward_realms.waywardlib.character.Character character) {
        return false;
    }

    public boolean canUse(Class clazz, int level) {
        return true;
    }

    @Override
    public boolean canUse(Combatant combatant) {
        return true;
    }

    @Override
    public boolean canUse(OfflinePlayer player) {
        return true;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", name);
        serialised.put("cooldown", coolDown);
        return serialised;
    }

    public static ItemSkill deserialize(Map<String, Object> serialised) {
        ItemSkill deserialised = new ItemSkill();
        deserialised.name = (String) serialised.get("name");
        deserialised.coolDown = (int) serialised.get("cooldown");
        return deserialised;
    }

}
