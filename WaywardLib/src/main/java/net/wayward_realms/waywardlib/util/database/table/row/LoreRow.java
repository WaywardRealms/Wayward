package net.wayward_realms.waywardlib.util.database.table.row;

import net.wayward_realms.waywardlib.util.database.TableRowImpl;

public class LoreRow extends TableRowImpl {

    private int itemStackId;
    private int row;
    private String lore;

    public LoreRow(int id) {
        super(id);
    }

    public int getItemStackId() {
        return itemStackId;
    }

    public void setItemStackId(int itemStackId) {
        this.itemStackId = itemStackId;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

}
