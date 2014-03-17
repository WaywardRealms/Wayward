package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardlib.skills.Spell;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;

public class FreezeSpell implements Spell {

    private String name = "Freeze";
    private int manaCost = 20;
    private int coolDown = 0;
    private SkillType type = SkillType.MAGIC_NATURE;

    @Override
    public boolean use(Player player) {
        Plugin plugin = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class).getProvider();
        scheduleLaunches(plugin, player, 0L, 5L, 10L, 15L, 20L, 25L);
        return true;
    }

    @Override
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon) {
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.ICE);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Freeze");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_NATURE) >= 4 || character.getSkillPoints(SkillType.MAGIC_DEFENCE) >= 8;
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

    private void scheduleLaunches(final Plugin plugin, final Player player, long... delays) {
        for (long delay : delays) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

                @Override
                public void run() {
                    Snowball snowball = player.launchProjectile(Snowball.class);
                    snowball.setMetadata("isFreeze", new FixedMetadataValue(plugin, true));
                }

            }, delay);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("name", name);
        serialised.put("mana-cost", manaCost);
        serialised.put("cooldown", coolDown);
        return serialised;
    }

    public static FreezeSpell deserialize(Map<String, Object> serialised) {
        FreezeSpell deserialised = new FreezeSpell();
        deserialised.name = (String) serialised.get("name");
        deserialised.manaCost = (int) serialised.get("mana-cost");
        deserialised.coolDown = (int) serialised.get("cooldown");
        return deserialised;
    }

}
