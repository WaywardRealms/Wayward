package net.wayward_realms.waywardlocks.keyring;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.character.CharacterPlugin;
import net.wayward_realms.waywardlocks.WaywardLocks;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyringManager {

    private WaywardLocks plugin;

    private Map<Integer, List<ItemStack>> keyrings = new HashMap<>();

    public KeyringManager(WaywardLocks plugin) {
        this.plugin = plugin;
    }

    public List<ItemStack> getKeyring(Character character) {
        if (keyrings.get(character.getId()) == null) {
            keyrings.put(character.getId(), new ArrayList<ItemStack>());
        }
        return keyrings.get(character.getId());
    }

    public List<ItemStack> getKeyring(OfflinePlayer player) {
        RegisteredServiceProvider<CharacterPlugin> characterPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(CharacterPlugin.class);
        if (characterPluginProvider != null) {
            CharacterPlugin characterPlugin = characterPluginProvider.getProvider();
            return getKeyring(characterPlugin.getActiveCharacter(player));
        }
        return null;
    }

    public void showKeyring(Player player) {
        Inventory keyringInventory = plugin.getServer().createInventory(null, 27, "Keyring");
        keyringInventory.setContents(getKeyring(player).toArray(new ItemStack[27]));
        player.openInventory(keyringInventory);
    }

    public void saveKeyrings() {
        File keyringFile = new File(plugin.getDataFolder(), "keyrings.yml");
        YamlConfiguration keyringConfig = new YamlConfiguration();
        for (Map.Entry<Integer, List<ItemStack>> entry : keyrings.entrySet()) {
            keyringConfig.set(entry.getKey().toString(), entry.getValue());
        }
        try {
            keyringConfig.save(keyringFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void loadKeyrings() {
        File keyringFile = new File(plugin.getDataFolder(), "keyrings.yml");
        if (keyringFile.exists()) {
            YamlConfiguration keyringConfig = new YamlConfiguration();
            try {
                keyringConfig.load(keyringFile);
            } catch (IOException | InvalidConfigurationException exception) {
                exception.printStackTrace();
            }
            for (String section : keyringConfig.getKeys(false)) {
                keyrings.put(Integer.parseInt(section), (List<ItemStack>) keyringConfig.getList(section));
            }
        }
    }

}
