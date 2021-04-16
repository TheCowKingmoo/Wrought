package com.thecowking.wrought.blocks.casting_machine;

import com.thecowking.wrought.blocks.INameableTile;
import com.thecowking.wrought.blocks.MultiBlockControllerBlock;
import com.thecowking.wrought.tileentity.casting_machine.CastingMachineControllerTile;
import com.thecowking.wrought.util.MultiBlockHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;


public class CastingMachineControllerBlock extends MultiBlockControllerBlock implements INameableTile {

    String tile;
    INamedContainerProvider containerProvider;

    public CastingMachineControllerBlock() {
        super();
    }

    // creates the tile entity
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CastingMachineControllerTile();
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos posIn, BlockState state, PlayerEntity player) {
        MultiBlockHelper.destroyMultiBlock(worldIn, posIn);
    }

    @Override
    public void setTileName(String name) {
        this.tile = name;
    }
}