package net.wayward_realms.waywardmoderation.logging;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InventoryChange implements ConfigurationSerializable {

    private String playerName;
    private ItemStack itemStack;
    private Date date;
    private InventoryChangeType type;

    public InventoryChange(ItemStack itemStack, InventoryChangeType type) {
        this.playerName = "NONE";
        this.itemStack = itemStack;
        this.date = new Date();
        this.type = type;
    }

    public InventoryChange(ItemStack itemStack, InventoryChangeType type, OfflinePlayer player) {
        this(itemStack, type);
        this.playerName = player.getName();
    }

    public InventoryChange() {}

    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(playerName);
    }

    public void setPlayer(OfflinePlayer player) {
        this.playerName = player.getName();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public InventoryChangeType getType() {
        return type;
    }

    public void setType(InventoryChangeType type) {
        this.type = type;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("player", playerName);
        serialised.put("item", itemStack);
        serialised.put("date", date);
        serialised.put("type", type.toString());
        return serialised;
    }

    public static InventoryChange deserialize(Map<String, Object> serialised) {
        InventoryChange deserialised = new InventoryChange();
        deserialised.playerName = (String) serialised.get("player");
        deserialised.itemStack = (ItemStack) serialised.get("item");
        deserialised.date = (Date) serialised.get("date");
        deserialised.type = InventoryChangeType.valueOf((String) serialised.get("type"));
        return deserialised;
    }

}
