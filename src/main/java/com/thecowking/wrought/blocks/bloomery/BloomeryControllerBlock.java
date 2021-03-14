package com.thecowking.wrought.blocks.bloomery;

import com.thecowking.wrought.blocks.INameableTile;
import com.thecowking.wrought.blocks.MultiBlockControllerBlock;
import com.thecowking.wrought.tileentity.bloomery.BloomeryControllerTile;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import com.thecowking.wrought.util.MultiBlockHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;


public class BloomeryControllerBlock extends MultiBlockControllerBlock implements INameableTile {
    private static final Logger LOGGER = LogManager.getLogger();

    String tileName;

    public BloomeryControllerBlock() {
        super();
    }

    // creates the tile entity
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BloomeryControllerTile();
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos posIn, BlockState state, PlayerEntity player) {
        MultiBlockHelper.destroyMultiBlock(worldIn, posIn);
    }

    @Override
    public void setTileName(String name) {
        this.tileName = name;
    }
}