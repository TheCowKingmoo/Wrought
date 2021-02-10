package com.thecowking.wrought.blocks.MultiBlock;



import com.thecowking.wrought.blocks.INameableTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.Container;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class MultiBlockControllerBlock extends Block implements IMultiBlockControllerBlock {


    public MultiBlockControllerBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2.0f)
                .harvestTool(ToolType.PICKAXE)
        );

        setDefaultState(this.getDefaultState().with(Multiblock.FORMED, false).with(Multiblock.RUNNING, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(Multiblock.FORMED, BlockStateProperties.FACING, Multiblock.RUNNING);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        if (state.get(Multiblock.RUNNING))  {
            return 14;
        }
        return 0;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state)  {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)  {
        return Container.calcRedstone(Multiblock.getTileFromPos(worldIn, pos));
    }

}

