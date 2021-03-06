package com.thecowking.wrought.inventory.containers.honey_comb_coke_oven;

import com.thecowking.wrought.inventory.containers.PlayerLayoutContainer;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import com.thecowking.wrought.util.RegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.thecowking.wrought.util.RegistryHandler.H_C_CONTAINER_BUILDER;



public class HCCokeOvenContainer extends PlayerLayoutContainer {
    HCCokeOvenControllerTile tile;
    public BlockPos controllerPos;

    public HCCokeOvenContainer(int id, World world, BlockPos controllerPos, PlayerInventory playerInventory) {
        super(H_C_CONTAINER_BUILDER.get(), id, world, controllerPos, playerInventory);
        this.tile = (HCCokeOvenControllerTile)world.getTileEntity(controllerPos);
        this.controllerPos = controllerPos;

    }

}
