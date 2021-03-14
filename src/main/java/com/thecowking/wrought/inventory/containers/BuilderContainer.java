package com.thecowking.wrought.inventory.containers;

import com.thecowking.wrought.data.IMultiblockData;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BuilderContainer extends MultiBlockContainer{
    public IMultiblockData data;
    public BuilderContainer(@Nullable ContainerType<?> type, int id, World world, BlockPos controllerPos, PlayerInventory playerInventory, IMultiblockData data) {
        super(type, id, world, controllerPos, playerInventory);
        this.data = data;
    }
}
