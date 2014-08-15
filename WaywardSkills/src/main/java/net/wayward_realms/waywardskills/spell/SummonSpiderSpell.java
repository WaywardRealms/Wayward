package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.AttackSpellBase;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.RegisteredServiceProvider;

public class SummonSpiderSpell extends AttackSpellBase {

    private WaywardSkills plugin;

    public SummonSpiderSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("SummonSpider");
        setManaCost(60);
        setCoolDown(60);
        setAttackStat(Stat.MAGIC_ATTACK);
        setDefenceStat(Stat.MAGIC_DEFENCE);
        setPower(90);
        setCriticalChance(5);
        setHitChance(40);
    }

    @Override
    public void animate(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        Player attackingPlayer = attacking.getPlayer().getPlayer();
        Entity spider = attackingPlayer.getWorld().spawnEntity(attackingPlayer.getLocation(), EntityType.SPIDER);
        if (spider != null) {
            spider.setMetadata("summoner", new FixedMetadataValue(plugin, attacking.getId()));
        }
    }

    @Override
    public double getWeaponModifier(ItemStack weapon) {
        return getMagicWeaponModifier(weapon);
    }

    @Override
    public String getFightUseMessage(Character attacking, Character defending, double damage) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " summoned a spider and demanded it attack " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + "! It dealt " + damage + " damage.";
    }

    @Override
    public String getFightFailManaMessage(Character attacking, Character defending) {
        return (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " attempted to summon a spider, but did not have enough mana.";
    }

    @Override
    public boolean use(Player player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            Entity spider = player.getWorld().spawnEntity(player.getLocation(), EntityType.SPIDER);
            if (spider != null) {
                spider.setMetadata("summoner", new FixedMetadataValue(plugin, characterPlugin.getActiveCharacter(player).getId()));
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "You cannot summon a spider here! (Try a bit further into the wilderness)");
            }
        }
        return false;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.SKULL_ITEM, 1, (byte) 0);
    }

    @Override
    public boolean canUse(Character character) {
        return true;
    }

    @Override
    public String getDescription() {
        return "Allows you to deal the difference between your magic attack roll and your opponent's melee defence roll for 3 turns in addition to taking another action";
    }

}
