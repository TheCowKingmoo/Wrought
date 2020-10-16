package com.thecowking.wrought.blocks.MultiBlock;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static com.thecowking.wrought.blocks.MultiBlock.Multiblock.*;


public class MultiBlockFrameTile extends MultiBlockTile {
    private static final Logger LOGGER = LogManager.getLogger();

    private BlockPos controllerPos;
    private String job;

    public MultiBlockFrameTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public void setupMultiBlock(BlockPos posIn)  {
        world.setBlockState(this.pos, this.getBlockState().with(Multiblock.FORMED, true));
        setControllerPos(posIn);
    }
    public void destroyMultiBlock()  {
        if(world.isRemote)  {
            return;
        }
        setRedstonePower(0);
        clearNBT();
        setFormed(false);
    }

    public void clearNBT()  {
        setControllerPos(null);
        setJob(null);
    }

    public MultiBlockControllerTile getControllerTile(World worldIn)  {
        if(this.controllerPos == null)  {return null;}
        TileEntity te = Multiblock.getTileFromPos(worldIn, this.controllerPos);
        if(te == null || !(te instanceof MultiBlockControllerTile))  {return null;}
        return (MultiBlockControllerTile) te;
    }

    public BlockPos getControllerPos()  {return this.controllerPos;}
    public void setControllerPos(BlockPos posIn) {this.controllerPos = posIn;}
    public String getJob()  {return this.job;}

    /*
      sets the job string - controller dependant jobs will be updated if changing
     */
    public void setJob(String inputJob)  {
        this.job = inputJob;
    }
    public void setRedstonePower(int power)  {this.world.setBlockState(this.pos, getBlockState().with(Multiblock.REDSTONE, power));}
    public boolean isRedstonePowered(BlockPos posIn)  {
        LOGGER.info("checking redstone");
        if(Multiblock.getTileFromPos(this.world, posIn) instanceof MultiBlockFrameTile)  {
            int redstoneNum = this.world.getBlockState(posIn).get(REDSTONE);
            if(redstoneNum > 0)  {return true;  }
        }
        return false;
    }
}


