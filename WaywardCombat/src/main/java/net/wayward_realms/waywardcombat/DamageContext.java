package net.wayward_realms.waywardcombat;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardlib.skills.Specialisation;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;

public class DamageContext implements ConfigurationSerializable {

    private int attacking;
    private int defending;
    private boolean onHand;
    private Specialisation specialisation;

    public DamageContext(Character attacking, Character defending) {
        this.attacking = attacking.getId();
        this.defending = defending.getId();
    }

    private DamageContext() {}

    public Character getAttacking() {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            return characterPlugin.getCharacter(attacking);
        }
        return null;
    }

    public void setAttacking(Character attacking) {
        this.attacking = attacking.getId();
    }

    public Character getDefending() {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            return characterPlugin.getCharacter(defending);
        }
        return null;
    }

    public void setDefending(Character defending) {
        this.defending = defending.getId();
    }

    public boolean isOnHand() {
        return onHand;
    }

    public void setOnHand(boolean onHand) {
        this.onHand = onHand;
    }

    public Specialisation getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(Specialisation specialisation) {
        this.specialisation = specialisation;
    }

    public String getRoll() {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
        if (characterPluginProvider != null && skillsPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
            return skillsPlugin.getDamageRoll(characterPlugin.getCharacter(attacking), specialisation, onHand, characterPlugin.getCharacter(defending));
        }
        return "0d100";
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("attacking", attacking);
        serialised.put("defending", defending);
        serialised.put("onhand", onHand);
        if (specialisation != null) serialised.put("specialisation", specialisation.getName());
        return serialised;
    }

    public static DamageContext deserialize(Map<String, Object> serialised) {
        DamageContext deserialised = new DamageContext();
        deserialised.attacking = (int) serialised.get("attacking");
        deserialised.defending = (int) serialised.get("defending");
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
