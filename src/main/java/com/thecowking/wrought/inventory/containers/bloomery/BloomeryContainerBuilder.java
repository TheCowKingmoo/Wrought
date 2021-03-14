package com.thecowking.wrought.inventory.containers.bloomery;

import com.thecowking.wrought.data.BloomeryData;
import com.thecowking.wrought.inventory.containers.BuilderContainer;
import com.thecowking.wrought.inventory.containers.MultiBlockContainer;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.thecowking.wrought.init.RegistryHandler.BLAST_FURNACE_BUILDER_CONTAINER;
import static com.thecowking.wrought.init.RegistryHandler.BLOOMERY_BUILDER_CONTAINER;


public class BloomeryContainerBuilder extends BuilderContainer {
    public BloomeryContainerBuilder(int id, World world, BlockPos controllerPos, PlayerInventory playerInventory) {
        super(BLOOMERY_BUILDER_CONTAINER.get(), id, world, controllerPos, playerInventory, new BloomeryData());
        this.controller = (MultiBlockControllerTile)world.getTileEntity(controllerPos);
    }
}
