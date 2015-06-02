package net.wayward_realms.waywardlib.util.database.table.row;

import net.wayward_realms.waywardlib.util.database.TableRowImpl;
import org.bukkit.inventory.ItemFlag;

public class ItemFlagRow extends TableRowImpl {

    private int itemStackId;
    private ItemFlag itemFlag;

    public ItemFlagRow(int id) {
        super(id);
    }

    public int getItemStackId() {
        return itemStackId;
    }

    public void setItemStackId(int itemStackId) {
        this.itemStackId = itemStackId;
    }

    public ItemFlag getItemFlag() {
        return itemFlag;
    }

    public void setItemFlag(ItemFlag itemFlag) {
        this.itemFlag = itemFlag;
    }

}
