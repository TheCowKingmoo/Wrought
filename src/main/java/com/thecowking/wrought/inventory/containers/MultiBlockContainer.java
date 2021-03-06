package com.thecowking.wrought.inventory.containers;

import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MultiBlockContainer extends PlayerLayoutContainer {


    protected MultiBlockControllerTile controller;

    protected MultiBlockContainer(@Nullable ContainerType<?> type, int id, World world, BlockPos controllerPos, PlayerInventory playerInventory) {
        super(type, id, world, controllerPos, playerInventory);
        this.controller = (MultiBlockControllerTile)world.getTileEntity(controllerPos);

    }

    public MultiBlockControllerTile getController()  {
        return this.controller;
    }
    public String getStatus()  { return controller.getStatus(); }
    public BlockPos getControllerPos()  {return this.controller.getPos();}

}
