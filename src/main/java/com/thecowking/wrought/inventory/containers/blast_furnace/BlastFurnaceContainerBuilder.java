package com.thecowking.wrought.inventory.containers.blast_furnace;

import com.thecowking.wrought.inventory.containers.MultiBlockContainer;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.thecowking.wrought.init.RegistryHandler.BLAST_FURNACE_BUILDER_CONTAINER;


public class BlastFurnaceContainerBuilder extends MultiBlockContainer {
    public BlastFurnaceContainerBuilder(int id, World world, BlockPos controllerPos, PlayerInventory playerInventory) {
        super(BLAST_FURNACE_BUILDER_CONTAINER.get(), id, world, controllerPos, playerInventory);
        this.controller = (MultiBlockControllerTile)world.getTileEntity(controllerPos);
    }
}
