package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.character.Equipment;
import net.wayward_realms.waywardlib.combat.CombatPlugin;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;

public class RollContext implements ConfigurationSerializable {

    private int rolling;
    private boolean attacking;
    private boolean onHand;
    private Specialisation specialisation;

    public RollContext(Character rolling, boolean attacking) {
        this.rolling = rolling.getId();
        this.attacking = attacking;
    }

    private RollContext() {}

    public void setOnHand(boolean onHand) {
        this.onHand = onHand;
    }

    public void setSpecialisation(Specialisation specialisation) {
        this.specialisation = specialisation;
    }

    public String getRoll() {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        RegisteredServiceProvider<CombatPlugin> combatPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CombatPlugin.class);
        RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
        if (characterPluginProvider != null && combatPluginProvider != null && skillsPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            CombatPlugin combatPlugin = combatPluginProvider.getProvider();
            SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
            Character character = characterPlugin.getCharacter(rolling);
            OfflinePlayer offlinePlayer = character.getPlayer();
            if (offlinePlayer.isOnline()) {
                Player player = offlinePlayer.getPlayer();
                Equipment equipment = character.getEquipment();
                if (specialisation != null) {
                    if (onHand) {
                        if (attacking) {
                            if (specialisation.meetsAttackRequirement(equipment.getOnHandItem())) {
                                return skillsPlugin.getAttackRoll(character, specialisation, true);
                            } else {
                                player.sendMessage(combatPlugin.getPrefix() + ChatColor.RED + "Your onhand item does not meet the attack requirement for that specialisation.");
                            }
                        } else {
                            if (specialisation.meetsDefenceRequirement(equipment.getOnHandItem())) {
                                return skillsPlugin.getDefenceRoll(character, specialisation, true);
                            } else {
                                player.sendMessage(combatPlugin.getPrefix() + ChatColor.RED + "Your onhand item does not meet the defence requirement for that specialisation.");
                            }
                        }
                    } else {
                        if (attacking) {
                            if (specialisation.meetsAttackRequirement(equipment.getOffHandItem())) {
                                return skillsPlugin.getAttackRoll(character, specialisation, false);
                            } else {
                                player.sendMessage(combatPlugin.getPrefix() + ChatColor.RED + "Your offhand item does not meet the attack requirement for that specialisation.");
                            }
                        } else {
                            if (specialisation.meetsDefenceRequirement(equipment.getOffHandItem())) {
                                return skillsPlugin.getDefenceRoll(character, specialisation, false);
                            } else {
                                player.sendMessage(combatPlugin.getPrefix() + ChatColor.RED + "Your offhand item does not meet the defence requirement for that specialisation.");
                            }
                        }
                    }
                } else {
                    player.sendMessage(combatPlugin.getPrefix() + ChatColor.RED + "That specialisation does not exist.");
                }
            }
        }
        return "0d100";
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("rolling", rolling);
        serialised.put("attacking", attacking);
        serialised.put("onhand", onHand);
        if (specialisation != null) serialised.put("specialisation", specialisation.getName());
        return serialised;
    }

    public static RollContext deserialize(Map<String, Object> serialised) {
        RollContext deserialised = new RollContext();
        deserialised.rolling = (int) serialised.get("rolling");
        deserialised.attacking = (boolean) serialised.get("attacking");
        deserialised.onHand = (boolean) serialised.get("onhand");
        if (serialised.containsKey("specialisation")) {
            RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
            if (skillsPluginProvider != null) {
                SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
                deserialised.specialisation = skillsPlugin.getSpecialisation((String) serialised.get("specialisation"));
            }
        }
        return deserialised;
    }

}
