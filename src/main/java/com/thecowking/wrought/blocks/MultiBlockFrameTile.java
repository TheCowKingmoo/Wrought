package com.thecowking.wrought.blocks;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MultiBlockFrameTile extends MultiBlockTile {
    private static final Logger LOGGER = LogManager.getLogger();

    private BlockPos controllerPos;


    public MultiBlockFrameTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }
    public BlockPos getControllerPos()  {return this.controllerPos;}
    public void setControllerPos(BlockPos posIn) {this.controllerPos = posIn;}

}


