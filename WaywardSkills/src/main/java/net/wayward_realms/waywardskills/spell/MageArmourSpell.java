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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MageArmourSpell extends SpellBase {

    public MageArmourSpell() {
        setName("MageArmour");
        setManaCost(10);
        setCoolDown(180);
        setType(SkillType.MAGIC_DEFENCE);
    }

    @Override
    public boolean use(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 2400, 1));
        return true;
    }

    @Override
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon) {
        return use(fight, (Character) attacking, (Character) defending, weapon);
    }

    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        if (attacking.getMana() >= 10) {
            fight.sendMessage(ChatColor.YELLOW + attacking.getName() + " formed a resistant barrier!");
            return use(attacking.getPlayer().getPlayer());
        }
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.IRON_CHESTPLATE);
        icon.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Mage Armour");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_DEFENCE) >= 5;
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
