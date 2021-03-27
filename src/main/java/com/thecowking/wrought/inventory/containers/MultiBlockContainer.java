package com.thecowking.wrought.inventory.containers;

import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MultiBlockContainer extends PlayerLayoutContainer {


    protected MultiBlockControllerTile controller;
    protected int numSlot = 0;

    protected MultiBlockContainer(@Nullable ContainerType<?> type, int id, World world, BlockPos controllerPos, PlayerInventory playerInventory) {
        super(type, id, world, controllerPos, playerInventory);
        this.controller = (MultiBlockControllerTile)world.getTileEntity(controllerPos);

    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public MultiBlockControllerTile getController()  {
        return this.controller;
    }
    public String getStatus()  { return controller.getStatus(); }
    public BlockPos getControllerPos()  {return this.controller.getPos();}
    public int getNumMachineSlots()  {return this.numSlot; }
    public double getProgress()  {
        if (controller.timeComplete == 0)  {return 0;}
        return (double)controller.timeElapsed / (controller.timeComplete);
    }
    public int getCurrentHeatLevel()  {return this.controller.currentHeatLevel; }
    public int getMaxHeatLevel()  {return this.controller.maxHeatLevel; }
    public double getHeatPercentage()  {
        if(getMaxHeatLevel() == 0)  return 0;
        return (double)getCurrentHeatLevel() / (double)getMaxHeatLevel();
    }
    public boolean enoughHeatToCraft()  {
        return controller.currentHeatLevel >= controller.recipeHeatLevel;
    }



}
