package com.thecowking.wrought.blocks.honey_comb_coke_oven;

import com.thecowking.wrought.blocks.INameableTile;
import com.thecowking.wrought.blocks.MultiBlockControllerBlock;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nullable;


import static com.thecowking.wrought.blocks.Multiblock.getTileFromPos;


public class HCCokeOvenControllerBlock extends MultiBlockControllerBlock implements INameableTile {
    private static final Logger LOGGER = LogManager.getLogger();

    String tile;



    public HCCokeOvenControllerBlock() {
        super();
    }

    // creates the tile entity
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new HCCokeOvenControllerTile();
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


    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos posIn, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (player instanceof ServerPlayerEntity) {
            TileEntity tileEntity = worldIn.getTileEntity(posIn);
            if (tileEntity instanceof HCCokeOvenControllerTile) {
                HCCokeOvenControllerTile controllerTile = (HCCokeOvenControllerTile) tileEntity;
                if(controllerTile.isFormed(controllerTile.getPos()) )  {
                    controllerTile.openGUI(worldIn, posIn, player, controllerTile);
                }  else if(controllerTile.isValidMultiBlockFormer(player.getHeldItem(hand).getItem())) {
                    LOGGER.info("no gui- attempt to form");
                    // attempts to form the multi-blocks
                    controllerTile.tryToFormMultiBlock(worldIn, player, posIn);
                }
            } else {
                LOGGER.info(posIn);
                LOGGER.info(tileEntity);
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos posIn, BlockState state, PlayerEntity player) {
        TileEntity tileEntity = getTileFromPos(worldIn, posIn);
        if(tileEntity instanceof HCCokeOvenControllerTile && !worldIn.isRemote)  {
            HCCokeOvenControllerTile castedTile = (HCCokeOvenControllerTile) tileEntity;
            castedTile.destroyMultiBlock(worldIn, posIn);
            worldIn.removeTileEntity(posIn);
        }
    }


    @Override
    public void setTileName(String name) {
        this.tile = name;

    }
}