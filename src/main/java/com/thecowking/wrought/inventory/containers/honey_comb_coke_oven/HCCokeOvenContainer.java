package com.thecowking.wrought.inventory.containers.honey_comb_coke_oven;

import com.thecowking.wrought.data.BloomeryData;
import com.thecowking.wrought.data.HCCokeOvenData;
import com.thecowking.wrought.inventory.containers.BuilderContainer;
import com.thecowking.wrought.inventory.containers.PlayerLayoutContainer;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.thecowking.wrought.init.RegistryHandler.BLOOMERY_BUILDER_CONTAINER;
import static com.thecowking.wrought.init.RegistryHandler.H_C_CONTAINER_BUILDER;



public class HCCokeOvenContainer extends BuilderContainer {
    public HCCokeOvenContainer(int id, World world, BlockPos controllerPos, PlayerInventory playerInventory) {
        super(H_C_CONTAINER_BUILDER.get(), id, world, controllerPos, playerInventory, new HCCokeOvenData());
        this.controller = (MultiBlockControllerTile)world.getTileEntity(controllerPos);
    }
}
