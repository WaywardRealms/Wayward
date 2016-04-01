package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Party;
import net.wayward_realms.waywardlib.classes.Stat;
import net.wayward_realms.waywardlib.combat.Combatant;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.SpellBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashSet;
import java.util.Set;

public class CureSpell extends SpellBase {

    private int radius = 8;

    public CureSpell() {
        setName("Cure");
        setManaCost(5);
        setCoolDown(20);
        setType(SkillType.MAGIC_HEALING);
    }

    @Override
    public boolean use(Player player) {
        Set<Player> players = new HashSet<>();
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            Party party = characterPlugin.getParty(characterPlugin.getActiveCharacter(player));
            if (party != null) {
                for (Character member : party.getMembers()) {
                    OfflinePlayer memberPlayer = member.getPlayer();
                    if (memberPlayer.isOnline()) players.add(memberPlayer.getPlayer());
                }
            } else {
                for (Player player1 : player.getWorld().getPlayers()) {
                    if (player1.getLocation().distanceSquared(player.getLocation()) <= radius * radius) {
                        players.add(player1);
                    }
                }
            }
            for (Player player1 : players) {
                double potency = characterPlugin.getActiveCharacter(player).getStatValue(Stat.MAGIC_DEFENCE) / 4D;
                if (player.getInventory().getItemInMainHand() != null) {
                    switch (player.getInventory().getItemInMainHand().getType()) {
                        case STICK: potency = characterPlugin.getActiveCharacter(player).getStatValue(Stat.MAGIC_DEFENCE) / 2D; break;
                        case BLAZE_ROD: potency = characterPlugin.getActiveCharacter(player).getStatValue(Stat.MAGIC_DEFENCE); break;
                        default: break;
                    }
                }
                double healthRestore = potency / (double) players.size();
                Character target = characterPlugin.getActiveCharacter(player1);
                player1.setHealth(target.getHealth());
                target.setHealth(Math.min(target.getHealth() + healthRestore, target.getMaxHealth()));
                player1.setHealth(target.getHealth());
            }
        }
        return true;
    }

    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        if (attacking.getMana() >= 5) {
            double potency = attacking.getStatValue(Stat.MAGIC_DEFENCE) / 4D;
            if (weapon != null) {
                switch (weapon.getType()) {
                    case STICK: potency = attacking.getStatValue(Stat.MAGIC_DEFENCE) / 2D; break;
                    case BLAZE_ROD: potency = attacking.getStatValue(Stat.MAGIC_DEFENCE); break;
                    default: break;
                }
            }
            defending.setHealth(Math.min(defending.getHealth() + potency, defending.getMaxHealth()));
            defending.getPlayer().getPlayer().setHealth(defending.getHealth());
            fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " used Cure on " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + ", healing " + potency + " health.");
            return true;
        }
        fight.sendMessage(ChatColor.YELLOW + (attacking.isNameHidden() ? ChatColor.MAGIC + attacking.getName() + ChatColor.RESET : attacking.getName()) + ChatColor.YELLOW + " attempted to use Cure on " + (defending.isNameHidden() ? ChatColor.MAGIC + defending.getName() + ChatColor.RESET : defending.getName()) + ChatColor.YELLOW + " but did not have enough mana.");
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.NETHER_STAR);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName("Cure");
        icon.setItemMeta(iconMeta);
        return icon;
    }

    @Override
    public boolean canUse(Combatant combatant) {
        return canUse((Character) combatant);
    }

    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_HEALING) >= 2;
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
        return "Restore HP equal to 25% of your magic defence stat to one target";
    }

}
