package com.thecowking.wrought.blocks.honeycomb_coke_oven;

import com.thecowking.wrought.blocks.MultiBlockFrameTile;
import com.thecowking.wrought.blocks.Multiblock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static com.thecowking.wrought.util.RegistryHandler.H_C_COKE_FRAME_TILE;


public class HCCokeOvenFrameTile extends MultiBlockFrameTile {
    private static final Logger LOGGER = LogManager.getLogger();

    private static String NBT_CX = "CX";
    private static String NBT_CY = "CY";
    private static String NBT_CZ = "CZ";
    private static String NBT_JOB = "JOB";


    public HCCokeOvenFrameTile() {
        super(H_C_COKE_FRAME_TILE.get());
    }

    public HCCokeOvenControllerTile getControllerTile()  {
        if(getControllerPos() != null) {
            TileEntity te = this.world.getTileEntity(getControllerPos());
            if (te instanceof HCCokeOvenControllerTile) {
                HCCokeOvenControllerTile controllerTile = (HCCokeOvenControllerTile) te;
                return controllerTile;
            }
        }
        return null;
    }

    public void setupMultiBlock(BlockPos posIn)  {
        world.setBlockState(this.pos, this.getBlockState().with(Multiblock.FORMED, true));
        setControllerPos(posIn);
    }
    public void destroyMultiBlock()  {
        if(world.isRemote)  {
            return;
        }
        clearNBT();
        setFormed(false);
    }

    public void clearNBT()  {
        setControllerPos(null);
        setJob(null);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        if(nbt.contains(NBT_CX))  {
            setControllerPos(new BlockPos(nbt.getInt(NBT_CX), nbt.getInt(NBT_CY), nbt.getInt(NBT_CZ)));
        }
        if (nbt.contains(NBT_JOB)) {
            setJob(nbt.getString(NBT_JOB));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        if(getControllerPos() != null)  {
            tag.putInt(NBT_CX, getControllerPos().getX());
            tag.putInt(NBT_CY, getControllerPos().getY());
            tag.putInt(NBT_CZ, getControllerPos().getZ());
        }
        if(getJob() != null)  {
            tag.putString(NBT_JOB, getJob());
        }
        return tag;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        HCCokeOvenControllerTile controllerTile = getControllerTile();
        if(controllerTile != null)  {
            if (getJob() == Multiblock.JOB_ITEM_IN && cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
                return controllerTile.getCapability(cap, Direction.UP);
            }  else if(getJob() == Multiblock.JOB_ITEM_OUT && cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY))  {
                return controllerTile.getCapability(cap, Direction.DOWN);
            }
        }
        return super.getCapability(cap, side);
    }





}
