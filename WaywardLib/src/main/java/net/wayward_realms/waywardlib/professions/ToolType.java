package net.wayward_realms.waywardlib.professions;

import org.bukkit.Material;

import static org.bukkit.Material.*;

public enum ToolType {

    PICKAXE(WOOD_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, GOLD_PICKAXE, DIAMOND_PICKAXE),
    SWORD(WOOD_SWORD, STONE_SWORD, IRON_SWORD, GOLD_SWORD, DIAMOND_SWORD),
    HOE(WOOD_HOE, STONE_HOE, IRON_HOE, GOLD_HOE, DIAMOND_HOE),
    AXE(WOOD_AXE, STONE_AXE, IRON_AXE, GOLD_AXE, DIAMOND_AXE),
    SPADE(WOOD_SPADE, STONE_SPADE, IRON_SPADE, GOLD_SPADE, DIAMOND_SPADE),

    BOW(Material.BOW),

    HELMET(LEATHER_HELMET, CHAINMAIL_HELMET, GOLD_HELMET, IRON_HELMET, DIAMOND_HELMET),
    CHESTPLATE(LEATHER_CHESTPLATE, CHAINMAIL_CHESTPLATE, GOLD_CHESTPLATE, IRON_CHESTPLATE, DIAMOND_CHESTPLATE),
    LEGGINGS(LEATHER_LEGGINGS, CHAINMAIL_LEGGINGS, GOLD_LEGGINGS, IRON_LEGGINGS, DIAMOND_LEGGINGS),
    BOOTS(LEATHER_BOOTS, CHAINMAIL_BOOTS, GOLD_BOOTS, IRON_BOOTS, DIAMOND_BOOTS);

    /**
     * Gets the tooltype of a material
     *
     * @param material the material
     * @return the tooltype, or none if the material is not a tool
     */
    public static ToolType getToolType(Material material) {
        if (material.toString().endsWith("_PICKAXE")) return PICKAXE;
        if (material.toString().endsWith("_SWORD")) return SWORD;
        if (material.toString().endsWith("_HOE")) return HOE;
        if (material.toString().endsWith("_AXE")) return AXE;
        if (material.toString().endsWith("_SPADE")) return SPADE;
        if (material.toString().endsWith("_HELMET")) return HELMET;
        if (material.toString().endsWith("_CHESTPLATE")) return CHESTPLATE;
        if (material.toString().endsWith("_LEGGINGS")) return LEGGINGS;
        if (material.toString().endsWith("_BOOTS")) return BOOTS;
        if (material == Material.BOW) return BOW;
        return null;
    }

    private final Material[] materials;

    private ToolType(Material... materials) {
        this.materials = materials;
    }

    /**
     * Gets the materials of the tooltype
     *
     * @return the materials of the tool's type
     */
    public Material[] getMaterials() {
        return materials;
    }

}
