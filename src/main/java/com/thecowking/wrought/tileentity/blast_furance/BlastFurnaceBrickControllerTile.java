package com.thecowking.wrought.tileentity.blast_furance;

import com.thecowking.wrought.data.BlastFurnaceData;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.tileentity.MultiBlockControllerTileFluid;

import static com.thecowking.wrought.util.RegistryHandler.BLAST_FURNACE_BRICK_CONTROLLER_TILE;


public class BlastFurnaceBrickControllerTile extends MultiBlockControllerTileFluid {
    public BlastFurnaceBrickControllerTile() {
        super(BLAST_FURNACE_BRICK_CONTROLLER_TILE.get(), new BlastFurnaceData(), 16000);
    }
}
