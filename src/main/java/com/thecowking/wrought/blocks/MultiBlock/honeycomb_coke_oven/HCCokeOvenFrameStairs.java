package com.thecowking.wrought.blocks.MultiBlock.honeycomb_coke_oven;

import com.thecowking.wrought.blocks.MultiBlock.IMultiBlockFrame;
import com.thecowking.wrought.blocks.MultiBlock.MultiBlockFrameBlock;
import com.thecowking.wrought.blocks.MultiBlock.Multiblock;
import com.thecowking.wrought.util.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import static com.thecowking.wrought.blocks.MultiBlock.Multiblock.*;

public class HCCokeOvenFrameStairs extends StairsBlock implements IMultiBlockFrame {

    public HCCokeOvenFrameStairs() {
        super(RegistryHandler.H_C_COKE_FRAME_BLOCK.get().getDefaultState(), Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2.0f)
                .harvestTool(ToolType.PICKAXE)
        );
        setDefaultState(this.getDefaultState().with(FORMED, false));
    }
    /*
      Looks below it to grab a Tile Entity that can find the controller and start deconstructing the multi-block
     */
    @Override
    public void onBlockHarvested(World worldIn, BlockPos posIn, BlockState state, PlayerEntity player) {
        // Jump out if we are not Server Side
        if(!(player instanceof ServerPlayerEntity))  {
            return;
        }
        if(state.get(Multiblock.FORMED))  {
            BlockPos pos = getUnderlyingBlock(posIn);
            if(pos != null) {
                Block b = worldIn.getBlockState(pos).getBlock();
                if(b instanceof HCCokeOvenFrameBlock)  {
                    HCCokeOvenFrameBlock frame = (HCCokeOvenFrameBlock) b;
                    frame.updateMultiBlock(worldIn, pos);
                }
            }
        }
        super.onBlockHarvested(worldIn, posIn, state, player);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, SHAPE, WATERLOGGED);
        builder.add(FORMED);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos posIn, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (player instanceof ServerPlayerEntity && worldIn.getBlockState(posIn).get(Multiblock.FORMED)) {
            BlockPos frameBlockPos = getUnderlyingBlock(posIn);
            Block frameBlock = worldIn.getBlockState(frameBlockPos).getBlock();
            if (frameBlock instanceof MultiBlockFrameBlock) {
                ((MultiBlockFrameBlock) frameBlock).onBlockActivated(worldIn.getBlockState(frameBlockPos), worldIn, frameBlockPos, player, hand, trace);
            }
        }
        return super.onBlockActivated(state, worldIn, posIn, player, hand, trace);
    }

    public void addingToMultblock(BlockState blockState, BlockPos posIn, World worldIn) {
        worldIn.setBlockState(posIn, blockState.with(FORMED, true));
    }

    @Override
    public void removeFromMultiBlock(BlockState blockState, BlockPos posIn, World worldIn) {
        worldIn.setBlockState(posIn, blockState.with(FORMED, false));
    }

}


