package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Party;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SpellBase;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;

public class EmpowerSpell extends SpellBase {

    private WaywardSkills plugin;

    public EmpowerSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setName("Empower");
        setCoolDown(300);
        setManaCost(10);
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
                    if (player1.getLocation().distanceSquared(player.getLocation()) <= 64) {
                        players.add(player1);
                    }
                }
            }
        }
        for (Player player1 : players) {
            player1.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 0));
            player1.getWorld().playEffect(player1.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
            player1.sendMessage(ChatColor.GOLD + "You feel empowered!");
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
        return hasScroll(character) && plugin.getSpecialisationValue(character, plugin.getSpecialisation("Buff")) >= 3;
    }

    @Override
    public String getDescription() {
        return "Increase your attack stats by 25% for 3 turns";
    }

    @Override
    public String getSpecialisationInfo() {
        return ChatColor.GRAY + "3 Buff points required";
    }

}
