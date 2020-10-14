package com.thecowking.wrought.blocks;

import com.thecowking.wrought.blocks.honeycomb_coke_oven.HCCokeOvenControllerTile;
import com.thecowking.wrought.blocks.honeycomb_coke_oven.HCCokeOvenFrameTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import org.jetbrains.annotations.Nullable;

import static com.thecowking.wrought.blocks.Multiblock.FORMED;
import static com.thecowking.wrought.blocks.Multiblock.REDSTONE;
import static net.minecraft.util.Direction.DOWN;

public class MultiBlockFrameBlock extends Block implements IMultiBlockFrameBlock {

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
        return true;
    }

    // creates the tile entity
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new HCCokeOvenFrameTile();
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



}
