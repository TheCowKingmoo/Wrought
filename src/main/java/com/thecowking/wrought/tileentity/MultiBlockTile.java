package com.thecowking.wrought.tileentity;

import com.thecowking.wrought.data.MultiblockData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MultiBlockTile extends TileEntity {
    private static final Logger LOGGER = LogManager.getLogger();

    public MultiBlockTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public boolean isFormed(BlockPos posIn) {
        if(MultiblockData.getTileFromPos(this.world, posIn) instanceof MultiBlockTile)  {
            return this.world.getBlockState(posIn).get(MultiblockData.FORMED);
        }
        return false;
    }


    public boolean isRunning(BlockPos posIn) {
        if(MultiblockData.getTileFromPos(this.world, posIn) instanceof MultiBlockTile)  {
            return this.world.getBlockState(posIn).get(MultiblockData.RUNNING);
        }
        return false;
    }

    public void setFormed(boolean b)  {this.world.setBlockState(pos, getBlockState().with(MultiblockData.FORMED, b));}
    public void setOn(boolean b)  {
        LOGGER.info("TURN ON");
        this.world.setBlockState(pos, getBlockState().with(MultiblockData.RUNNING, b));
    }


    public BlockPos yankControllerPos()  {
        if (this instanceof MultiBlockControllerTile)  {
            LOGGER.info("GETTING CONTROLLER FOR CONTROLLER");
            return ((MultiBlockControllerTile)this).getControllerPos();
        }  else  {
            LOGGER.info("GETTING CONTROLLER FOR FRAME");
            LOGGER.info(((MultiBlockFrameTile)this).frameGetControllerPos());
            LOGGER.info(this.world);
            return ((MultiBlockFrameTile)this).frameGetControllerPos();
        }
    }

}
