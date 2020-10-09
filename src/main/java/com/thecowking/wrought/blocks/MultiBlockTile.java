package com.thecowking.wrought.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiBlockTile extends TileEntity {
    public MultiBlockTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public boolean isFormed(BlockPos posIn) {
        if(Multiblock.getTileFromPos(this.world, posIn) instanceof MultiBlockTile)  {
            return this.world.getBlockState(posIn).get(Multiblock.FORMED);
        }
        return false;
    }
    public void setFormed(boolean b)  {this.world.setBlockState(pos, getBlockState().with(Multiblock.FORMED, b));}


    public BlockPos getBlockPostion()  {return this.pos;}

}
