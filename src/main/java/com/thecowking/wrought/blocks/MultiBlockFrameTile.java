package com.thecowking.wrought.blocks;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MultiBlockFrameTile extends TileEntity {
    private static final Logger LOGGER = LogManager.getLogger();

    private BlockPos controllerPos;

    public MultiBlockFrameTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }
    public BlockPos getControllerPos()  {return this.controllerPos;}
    public void setControllerPos(BlockPos posIn) {
        LOGGER.info("setting controllerPos for frame at - ");
        LOGGER.info(this.pos);
        LOGGER.info(" with ");
        LOGGER.info(posIn);
        this.controllerPos = posIn;
    }

    public void setFormed(World worldIn, boolean b)  {worldIn.setBlockState(this.pos, this.getBlockState().with(Multiblock.FORMED, b));}
    public boolean isFormed(World worldIn) {return worldIn.getBlockState(pos).get(Multiblock.FORMED);}
}


