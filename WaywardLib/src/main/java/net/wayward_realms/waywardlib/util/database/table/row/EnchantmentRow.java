package net.wayward_realms.waywardlib.util.database.table.row;

import net.wayward_realms.waywardlib.util.database.TableRowImpl;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentRow extends TableRowImpl {

    private int itemStackId;
    private Enchantment enchantment;
    private int level;

    public EnchantmentRow(int id) {
        super(id);
    }

    public int getItemStackId() {
        return itemStackId;
    }

    public void setItemStackId(int itemStackId) {
        this.itemStackId = itemStackId;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public void setEnchantment(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
