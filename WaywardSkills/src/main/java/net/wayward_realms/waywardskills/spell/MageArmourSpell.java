package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SpellBase;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class MageArmourSpell extends SpellBase {

    private WaywardSkills plugin;

    public MageArmourSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("MageArmour");
        setManaCost(25);
        setCoolDown(180);
    }

    @Override
    public boolean use(final Player player) {
        List<String> lore = new ArrayList<>();
        lore.add("" + System.currentTimeMillis());
        if (player.getInventory().getHelmet() == null) {
            ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
            ItemMeta helmetMeta = helmet.getItemMeta();
            helmetMeta.setDisplayName("Mage armour helmet");
            helmetMeta.setLore(lore);
            helmetMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
            helmet.setItemMeta(helmetMeta);
            player.getInventory().setHelmet(helmet);
        }
        if (player.getInventory().getChestplate() == null) {
            ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
            ItemMeta chestplateMeta = chestplate.getItemMeta();
            chestplateMeta.setDisplayName("Mage armour chestplate");
            chestplateMeta.setLore(lore);
            chestplateMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
            chestplate.setItemMeta(chestplateMeta);
            player.getInventory().setChestplate(chestplate);
        }
        if (player.getInventory().getLeggings() == null) {
            ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
            ItemMeta leggingsMeta = leggings.getItemMeta();
            leggingsMeta.setDisplayName("Mage armour leggings");
            leggingsMeta.setLore(lore);
            leggingsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
            leggings.setItemMeta(leggingsMeta);
            player.getInventory().setLeggings(leggings);
        }
        if (player.getInventory().getBoots() == null) {
            ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
            ItemMeta bootsMeta = boots.getItemMeta();
            bootsMeta.setDisplayName("Mage armour boots");
            bootsMeta.setLore(lore);
            bootsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
            boots.setItemMeta(bootsMeta);
            player.getInventory().setBoots(boots);
        }
        return true;
    }

    @Override
    public boolean use(Fight fight, Combatant attacking, Combatant defending, ItemStack weapon) {
        return use(fight, (Character) attacking, (Character) defending, weapon);
    }

    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        if (attacking.getMana() >= 10) {
            fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " formed a resistant barrier!");
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
        return hasScroll(character) && plugin.getSpecialisationValue(character, plugin.getSpecialisation("Shielding Magic")) >= 5;
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
        return "Increases defensive rolls by 25% for 5 turns";
    }

}
