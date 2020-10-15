package com.thecowking.wrought.blocks.honeycomb_coke_oven;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class HCCokeOvenFrameSlab extends SlabBlock {
    public HCCokeOvenFrameSlab() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2.0f)
                .harvestTool(ToolType.PICKAXE)
        );
    }
}
