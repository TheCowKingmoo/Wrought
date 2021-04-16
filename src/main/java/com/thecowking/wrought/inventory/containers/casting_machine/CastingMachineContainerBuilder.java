package com.thecowking.wrought.inventory.containers.casting_machine;

import com.thecowking.wrought.data.BlastFurnaceData;
import com.thecowking.wrought.data.CastingMachineData;
import com.thecowking.wrought.inventory.containers.BuilderContainer;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.thecowking.wrought.init.RegistryHandler.CASTING_MACHINE_CONTAINER;


public class CastingMachineContainerBuilder extends BuilderContainer {
    public CastingMachineContainerBuilder(int id, World world, BlockPos controllerPos, PlayerInventory playerInventory) {
        super(CASTING_MACHINE_CONTAINER.get(), id, world, controllerPos, playerInventory, new CastingMachineData());
        this.controller = (MultiBlockControllerTile)world.getTileEntity(controllerPos);
    }
}
