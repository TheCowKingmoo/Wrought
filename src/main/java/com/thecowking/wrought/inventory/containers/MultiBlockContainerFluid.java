package com.thecowking.wrought.inventory.containers;

import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.tileentity.MultiBlockControllerTileFluid;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MultiBlockContainerFluid extends PlayerLayoutContainer {


   protected MultiBlockControllerTileFluid fluidController;

    protected MultiBlockContainerFluid(@Nullable ContainerType<?> type, int id, World world, BlockPos controllerPos, PlayerInventory playerInventory) {
        super(type, id, world, controllerPos, playerInventory);
        this.fluidController = (MultiBlockControllerTileFluid)world.getTileEntity(controllerPos);
    }

    public MultiBlockControllerTileFluid getFluidController()  { return (MultiBlockControllerTileFluid)this.fluidController; }
}
