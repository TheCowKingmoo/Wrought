package com.thecowking.wrought.tileentity;

import com.thecowking.wrought.blocks.Multiblock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static com.thecowking.wrought.blocks.Multiblock.*;


public class MultiBlockFrameTile extends MultiBlockTile {
    private static final Logger LOGGER = LogManager.getLogger();

    private static String NBT_CX = "CX";
    private static String NBT_CY = "CY";
    private static String NBT_CZ = "CZ";
    private static String NBT_JOB = "JOB";

    public BlockPos controllerPos;
    private String job;

    public MultiBlockFrameTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public void setupMultiBlock(BlockPos posIn)  {
        world.setBlockState(this.pos, this.getBlockState().with(Multiblock.FORMED, true));
        frameSetControllerPos(posIn);
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
        frameSetControllerPos(null);
        setJob(null);
    }

    public MultiBlockControllerTile getControllerTile(World worldIn)  {
        if(this.controllerPos == null)  {return null;}
        TileEntity te = Multiblock.getTileFromPos(worldIn, this.controllerPos);
        if(te == null || !(te instanceof MultiBlockControllerTile))  {return null;}
        return (MultiBlockControllerTile) te;
    }

    public BlockPos frameGetControllerPos()  {
        return this.controllerPos;
    }

    public void frameSetControllerPos(BlockPos posIn) {
        LOGGER.info("control pos has been set for frame");
        this.controllerPos = posIn;
    }
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


    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        if(nbt.contains(NBT_CX))  {
            frameSetControllerPos(new BlockPos(nbt.getInt(NBT_CX), nbt.getInt(NBT_CY), nbt.getInt(NBT_CZ)));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        if(frameGetControllerPos() != null)  {
            tag.putInt(NBT_CX, frameGetControllerPos().getX());
            tag.putInt(NBT_CY, frameGetControllerPos().getY());
            tag.putInt(NBT_CZ, frameGetControllerPos().getZ());
        }
        return tag;
    }
}


