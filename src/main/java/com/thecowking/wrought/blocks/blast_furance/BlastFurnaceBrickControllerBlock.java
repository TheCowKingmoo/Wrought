package com.thecowking.wrought.blocks.blast_furance;

import com.thecowking.wrought.blocks.INameableTile;
import com.thecowking.wrought.blocks.MultiBlockControllerBlock;
import com.thecowking.wrought.data.HCCokeOvenData;
import com.thecowking.wrought.tileentity.blast_furance.BlastFurnaceBrickControllerTile;
import com.thecowking.wrought.util.MultiBlockHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;


public class BlastFurnaceBrickControllerBlock extends MultiBlockControllerBlock implements INameableTile {
    private static final Logger LOGGER = LogManager.getLogger();

    String tile;
    INamedContainerProvider containerProvider;

    public BlastFurnaceBrickControllerBlock() {

        super();
    }

    // creates the tile entity
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BlastFurnaceBrickControllerTile();
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



    @Override
    public void onBlockHarvested(World worldIn, BlockPos posIn, BlockState state, PlayerEntity player) {
        MultiBlockHelper.destroyMultiBlock(worldIn, posIn, new HCCokeOvenData());
    }


    @Override
    public void setTileName(String name) {
        this.tile = name;
    }
}