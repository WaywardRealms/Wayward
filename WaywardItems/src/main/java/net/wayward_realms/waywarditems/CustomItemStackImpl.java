package net.wayward_realms.waywarditems;

import net.wayward_realms.waywardlib.items.CustomItemStack;
import net.wayward_realms.waywardlib.items.CustomMaterial;
import net.wayward_realms.waywardlib.items.ItemsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;

public class CustomItemStackImpl implements CustomItemStack {

    private CustomMaterial material;
    private int amount;

    public CustomItemStackImpl(CustomMaterial material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public CustomItemStackImpl(ItemStack itemStack) {
        RegisteredServiceProvider<ItemsPlugin> itemsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(ItemsPlugin.class);
        if (itemsPluginProvider != null) {
            ItemsPlugin itemsPlugin = itemsPluginProvider.getProvider();
            for (CustomMaterial customMaterial : itemsPlugin.getMaterials()) {
                if (customMaterial.isMaterial(itemStack)) {
                    this.material = customMaterial;
                    break;
                }
            }
        }
        this.amount = itemStack.getAmount();
    }

    @Override
    public CustomMaterial getMaterial() {
        return material;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public ItemStack toMinecraftItemStack() {
        ItemStack minecraftStack = new ItemStack(material.getMinecraftMaterial(), amount);
        ItemMeta meta = minecraftStack.getItemMeta();
        meta.setDisplayName(material.getName());
        minecraftStack.setItemMeta(meta);
        return minecraftStack;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("material", material);
        serialized.put("amount", amount);
        return serialized;
    }

    public static CustomItemStackImpl deserialize(Map<String, Object> serialized) {
        return new CustomItemStackImpl((CustomMaterialImpl) serialized.get("material"), (int) serialized.get("amount"));
    }

}
