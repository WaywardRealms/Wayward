package net.wayward_realms.waywardlocks.keyring;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlocks.WaywardLocks;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KeyringManager {

    private WaywardLocks plugin;

    public KeyringManager(WaywardLocks plugin) {
        this.plugin = plugin;
    }

    public List<ItemStack> getKeyring(Character character) {
        File keyringFile = new File(plugin.getDataFolder(), "keyrings.yml");
        if (keyringFile.exists()) {
            YamlConfiguration keyringConfig = YamlConfiguration.loadConfiguration(keyringFile);
            if (keyringConfig.get("" + character.getId()) == null) return new ArrayList<>();
            return (List<ItemStack>) keyringConfig.getList("" + character.getId());
        }
        return new ArrayList<>();
    }

    public void setKeyring(Character character, List<ItemStack> keyring) {
        File keyringFile = new File(plugin.getDataFolder(), "keyrings.yml");
        YamlConfiguration keyringConfig = YamlConfiguration.loadConfiguration(keyringFile);
        keyringConfig.set("" + character.getId(), keyring);
        try {
            keyringConfig.save(keyringFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public List<ItemStack> getKeyring(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            return getKeyring(characterPlugin.getActiveCharacter(player));
        }
        return null;
    }

    public void setKeyring(OfflinePlayer player, List<ItemStack> keyring) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            setKeyring(characterPlugin.getActiveCharacter(player), keyring);
        }
    }

    public void showKeyring(Player player) {
        Inventory keyringInventory = plugin.getServer().createInventory(null, 27, "Keyring");
        keyringInventory.setContents(getKeyring(player).toArray(new ItemStack[27]));
        player.openInventory(keyringInventory);
    }

}
