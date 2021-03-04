package com.thecowking.wrought.blocks;


import com.thecowking.wrought.data.MultiblockData;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.util.MultiBlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class MultiBlockControllerBlock extends Block implements IMultiBlockControllerBlock {


    public MultiBlockControllerBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2.0f)
                .harvestTool(ToolType.PICKAXE)
        );

        setDefaultState(this.getDefaultState().with(MultiblockData.FORMED, false).with(MultiblockData.RUNNING, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(MultiblockData.FORMED, BlockStateProperties.FACING, MultiblockData.RUNNING);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        if (state.get(MultiblockData.RUNNING))  {
            return 14;
        }
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos posIn, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (player instanceof ServerPlayerEntity) {
            TileEntity tileEntity = world.getTileEntity(posIn);
            if (tileEntity instanceof MultiBlockControllerTile) {
                MultiBlockControllerTile controllerTile = (MultiBlockControllerTile) tileEntity;
                if(controllerTile.isValidMultiBlockFormer(player.getHeldItem(hand).getItem()))  {
                    MultiBlockHelper.tryToFormMultiBlock(world, player, posIn, controllerTile.getData());
                }  else  {
                    controllerTile.openGUI(world, player, controllerTile.isFormed());
                }
            } else {
                LOGGER.info(posIn);
                LOGGER.info(tileEntity);
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        super.onBlockActivated(state, world, posIn, player, hand, trace);
        return ActionResultType.SUCCESS;
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
        return Container.calcRedstone(MultiblockData.getTileFromPos(worldIn, pos));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction[] d = context.getNearestLookingDirections();
        for(int i = 0; i < d.length; i++)  {
            if(d[i] != Direction.UP && d[i] != Direction.DOWN)  {
                return getDefaultState().with(BlockStateProperties.FACING, d[i].getOpposite());
            }
        }
        return getDefaultState().with(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite());
    }

}

