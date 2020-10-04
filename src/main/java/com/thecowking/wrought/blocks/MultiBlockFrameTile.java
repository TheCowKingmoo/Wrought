package com.thecowking.wrought.blocks;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public class MultiBlockFrameTile extends TileEntity {
    private static String NBT_CX = "CX";
    private static String NBT_CY = "CY";
    private static String NBT_CZ = "CZ";
    private BlockPos controllerPos;


    public MultiBlockFrameTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

}


