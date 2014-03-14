package net.wayward_realms.waywardlib.professions;

import org.bukkit.Material;

import static org.bukkit.Material.*;

public enum ToolType {

    PICKAXE(WOOD_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, GOLD_PICKAXE, DIAMOND_PICKAXE),
    SWORD(WOOD_SWORD, STONE_SWORD, IRON_SWORD, GOLD_SWORD, DIAMOND_SWORD),
    HOE(WOOD_HOE, STONE_HOE, IRON_HOE, GOLD_HOE, DIAMOND_HOE),
    AXE(WOOD_AXE, STONE_AXE, IRON_AXE, GOLD_AXE, DIAMOND_AXE),
    SPADE(WOOD_SPADE, STONE_SPADE, IRON_SPADE, GOLD_SPADE, DIAMOND_SPADE);

    private final Material[] materials;

    private ToolType(Material... materials) {
        this.materials = materials;
    }

    public Material[] getMaterials() {
        return materials;
    }

}
