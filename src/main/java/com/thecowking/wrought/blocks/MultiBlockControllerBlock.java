package com.thecowking.wrought.blocks;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.common.ToolType;

public class MultiBlockControllerBlock extends Block implements IMultiBlockControllerBlock {

    public MultiBlockControllerBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2.0f)
                .harvestTool(ToolType.PICKAXE)
        );
        setDefaultState(this.getDefaultState().with(Multiblock.FORMED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.POWERED, Multiblock.FORMED);
    }
}

