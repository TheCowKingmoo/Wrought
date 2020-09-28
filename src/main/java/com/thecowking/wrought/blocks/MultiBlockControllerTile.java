package com.thecowking.wrought.blocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiBlockControllerTile extends TileEntity implements IMultiBlockControllerTile {

    public MultiBlockControllerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public boolean isFormed(World worldIn) {
        return false;
    }

    @Override
    public void setFormed(World worldIn, boolean b) {

    }

    @Override
    public boolean isValidMultiBlockFormer(Item item) {
        return false;
    }

    @Override
    public void tryToFormMultiBlock(World worldIn, BlockPos pos) {

    }

    @Override
    public void openGUI(World worldIn, BlockPos pos, PlayerEntity player, IMultiBlockControllerTile tileEntity) {

    }

    @Override
    public void setDirty(boolean b) {

    }
}
