package com.thecowking.wrought.inventory.containers.bloomery;

import com.thecowking.wrought.init.RegistryHandler;
import com.thecowking.wrought.inventory.containers.MultiBlockContainer;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.thecowking.wrought.init.RegistryHandler.BLAST_FURNACE_BUILDER_CONTAINER;


public class BloomeryContainerBuilder extends MultiBlockContainer {
    public BloomeryContainerBuilder(int id, World world, BlockPos controllerPos, PlayerInventory playerInventory) {
        super(RegistryHandler.BLOOMERY_BUILDER_CONTAINER.get(), id, world, controllerPos, playerInventory);
        this.controller = (MultiBlockControllerTile)world.getTileEntity(controllerPos);
    }
}
