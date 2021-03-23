package com.thecowking.wrought.tileentity.refactory_brick;

import com.thecowking.wrought.tileentity.MultiBlockFrameTile;
import com.thecowking.wrought.tileentity.blast_furance.BlastFurnaceBrickControllerTile;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static com.thecowking.wrought.init.RegistryHandler.BLAST_FURNACE_BRICK_FRAME_TILE;
import static com.thecowking.wrought.init.RegistryHandler.REFACTORY_BRICK_FRAME_TILE;


public class RefactoryBrickFrameTile extends MultiBlockFrameTile {
    private static final Logger LOGGER = LogManager.getLogger();

    public RefactoryBrickFrameTile() {
        super(REFACTORY_BRICK_FRAME_TILE.get());
    }

    public BlastFurnaceBrickControllerTile getControllerTile()  {
        if(frameGetControllerPos() != null) {
            TileEntity te = this.world.getTileEntity(frameGetControllerPos());
            if (te instanceof HCCokeOvenControllerTile) {
                BlastFurnaceBrickControllerTile controllerTile = (BlastFurnaceBrickControllerTile) te;
                return controllerTile;
            }
        }
        return null;
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        BlastFurnaceBrickControllerTile controllerTile = getControllerTile();
        if(controllerTile != null)  {
                return controllerTile.getCapability(cap, Direction.WEST);
        }
        return super.getCapability(cap, side);
    }



}
