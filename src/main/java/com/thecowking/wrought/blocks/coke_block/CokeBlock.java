package com.thecowking.wrought.blocks.coke_block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class CokeBlock extends Block {
    public CokeBlock() {
        super(Properties.create(Material.EARTH)
                .sound(SoundType.STONE)
                .hardnessAndResistance(2.0f)
                .harvestTool(ToolType.PICKAXE)
        );
    }
}