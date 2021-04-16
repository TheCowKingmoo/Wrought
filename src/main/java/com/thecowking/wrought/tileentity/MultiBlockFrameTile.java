package com.thecowking.wrought.tileentity;

import com.thecowking.wrought.data.MultiblockData;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.thecowking.wrought.data.MultiblockData.*;


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
        world.setBlockState(this.pos, this.getBlockState().with(MultiblockData.FORMED, true));
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

    public void setRedstonePower(int power)  {this.world.setBlockState(this.pos, getBlockState().with(MultiblockData.REDSTONE, power));}

    public boolean isRedstonePowered(BlockPos posIn)  {
        LOGGER.info("checking redstone");
        if(MultiblockData.getTileFromPos(this.world, posIn) instanceof MultiBlockFrameTile)  {
            int redstoneNum = this.world.getBlockState(posIn).get(REDSTONE);
            if(redstoneNum > 0)  {return true;  }
        }
        return false;
    }

    public MultiBlockControllerTile frameGetControllerTE()  {
        if (this.controllerPos == null)  return null;
        TileEntity te = this.world.getTileEntity(this.controllerPos);
        if (te instanceof MultiBlockControllerTile) {
            return (MultiBlockControllerTile) te;
        }
        return null;
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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        MultiBlockControllerTile controllerTile = frameGetControllerTE();
        if(controllerTile != null)  {
            return controllerTile.getCapability(cap, Direction.WEST);
        }
        return super.getCapability(cap, side);
    }
}


