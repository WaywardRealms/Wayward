package net.wayward_realms.waywardskills.skill;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillBase;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DashSkill extends SkillBase {

    private WaywardSkills plugin;

    public DashSkill(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("Dash");
        setCoolDown(120);
    }

    @Override
    public boolean use(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 0));
        return true;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        fight.removeCombatant(attacking);
        fight.sendMessage(ChatColor.GREEN + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.GREEN + " fled the fight!");
        return true;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Dash");
        icon.setItemMeta(meta);
        return icon;
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
        return "Flees a fight without fail";
    }

    @Override
    public boolean canUse(Character character) {
        return plugin.getSpecialisationValue(character, plugin.getSpecialisation("Nimble")) >= 3;
    }

}
