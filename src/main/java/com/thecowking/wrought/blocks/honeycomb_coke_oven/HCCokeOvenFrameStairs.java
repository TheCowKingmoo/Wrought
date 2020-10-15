package com.thecowking.wrought.blocks.honeycomb_coke_oven;

import com.thecowking.wrought.util.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

import java.util.function.Supplier;

public class HCCokeOvenFrameStairs extends StairsBlock {

    public HCCokeOvenFrameStairs() {
        super(RegistryHandler.H_C_COKE_FRAME_BLOCK.get().getDefaultState(), Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2.0f)
                .harvestTool(ToolType.PICKAXE));
    }
}


