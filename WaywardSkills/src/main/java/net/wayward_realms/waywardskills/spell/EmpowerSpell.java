package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.SpellBase;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EmpowerSpell extends SpellBase {

    public EmpowerSpell() {
        setName("Empower");
        setCoolDown(300);
        setManaCost(10);
        setType(SkillType.SUPPORT_PERFORM);
    }

    @Override
    public boolean use(Player player) {
        for (Player player1 : player.getWorld().getPlayers()) {
            if (player1.getLocation().distanceSquared(player.getLocation()) <= 64) {
                player1.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 0));
                player1.getWorld().playEffect(player1.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
                player1.sendMessage(ChatColor.GOLD + "You feel empowered!");
            }
        }
        return true;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
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

}
