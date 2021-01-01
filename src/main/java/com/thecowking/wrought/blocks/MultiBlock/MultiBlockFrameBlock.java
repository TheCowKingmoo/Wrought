package com.thecowking.wrought.blocks.MultiBlock;

import com.thecowking.wrought.blocks.MultiBlock.honey_comb_coke_oven.HCCokeOvenFrameTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import static com.thecowking.wrought.blocks.MultiBlock.Multiblock.FORMED;
import static com.thecowking.wrought.blocks.MultiBlock.Multiblock.REDSTONE;
import javax.annotation.Nullable;


public class MultiBlockFrameBlock extends Block implements IMultiBlockFrame {

    public MultiBlockFrameBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2.0f)
                .harvestTool(ToolType.PICKAXE)
        );
        setDefaultState(this.getDefaultState().with(FORMED, false));
        setDefaultState(this.getDefaultState().with(BlockStateProperties.POWER_0_15, 0));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(Multiblock.FORMED);
    }

    // creates the tile entity
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if(state.get(Multiblock.FORMED))  {
            return new HCCokeOvenFrameTile();
        }
        return null;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FORMED);
        builder.add(REDSTONE);
    }


    /*
      Will send out a downwards redstone signal
     */
    @SuppressWarnings("deprecation")
    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
         if(side == Direction.UP)   {
             return blockState.get(REDSTONE);
         }
         return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return getWeakPower(blockState, blockAccess, pos, side);
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public boolean canProvidePower(BlockState state) {
        return true;
    }


    @Override
    public void addingToMultblock(BlockState blockState, BlockPos posIn, World worldIn) {
        worldIn.setBlockState(posIn, blockState.with(FORMED, true));
        this.createTileEntity(blockState, worldIn);
    }

    @Override
    public void removeFromMultiBlock(BlockState blockState, BlockPos posIn, World worldIn) {
        worldIn.setBlockState(posIn, blockState.with(FORMED, false));
    }
}
