package net.wayward_realms.waywardlib.util.database.table.row;

import net.wayward_realms.waywardlib.util.database.TableRowImpl;
import org.bukkit.inventory.ItemStack;

public class ItemStackRow extends TableRowImpl {

    private ItemStack itemStack;

    public ItemStackRow(int id) {
        super(id);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

}
