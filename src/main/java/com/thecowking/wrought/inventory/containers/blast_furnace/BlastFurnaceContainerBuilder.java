package com.thecowking.wrought.inventory.containers.blast_furnace;

import com.thecowking.wrought.inventory.containers.MultiBlockContainer;
import com.thecowking.wrought.inventory.containers.PlayerLayoutContainer;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.tileentity.blast_furance.BlastFurnaceBrickControllerTile;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import com.thecowking.wrought.util.RegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.thecowking.wrought.util.RegistryHandler.BLAST_FURNACE_BUILDER_CONTAINER;
import static com.thecowking.wrought.util.RegistryHandler.H_C_CONTAINER_BUILDER;


public class BlastFurnaceContainerBuilder extends MultiBlockContainer {
    public BlastFurnaceContainerBuilder(int id, World world, BlockPos controllerPos, PlayerInventory playerInventory) {
        super(BLAST_FURNACE_BUILDER_CONTAINER.get(), id, world, controllerPos, playerInventory);
        this.controller = (MultiBlockControllerTile)world.getTileEntity(controllerPos);
    }
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        //BlockPos targetBlock = new BlockPos(playerIn.getLookVec());
        return isWithinUsableDistance(IWorldPosCallable.of(controller.getWorld(), this.controllerPos), playerIn, RegistryHandler.BLAST_FURANCE_BRICK_CONTROLLER.get());

        //return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), targetBlock), playerIn, RegistryHandler.H_C_COKE_CONTROLLER_BLOCK.get());
    }

}
