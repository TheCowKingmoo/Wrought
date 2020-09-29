package com.thecowking.wrought.blocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiBlockControllerTile extends TileEntity implements IMultiBlockControllerTile {

    public MultiBlockControllerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }


    // returns the blockstate of the multiblocks controllers "Formed" state
    public boolean isFormed(World worldIn) {return worldIn.getBlockState(pos).get(Multiblock.FORMED);}

    // sets the blockstate "formed" for the multiblocks controller
    public void setFormed(World worldIn, boolean b) {worldIn.setBlockState(pos, getBlockState().with(Multiblock.FORMED, b));}

    public boolean isValidMultiBlockFormer(Item item) {
        return false;
    }

    public Direction getDirectionFacing(World worldIn)  {return worldIn.getBlockState(pos).get(BlockStateProperties.FACING);}


    public void tryToFormMultiBlock(World worldIn, BlockPos pos) {}

    public void openGUI(World worldIn, BlockPos pos, PlayerEntity player, IMultiBlockControllerTile tileEntity) {

    }

    public void setDirty(boolean b) {

    }
}
