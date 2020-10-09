package com.thecowking.wrought.blocks;

import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Multiblock {
    public static final BooleanProperty FORMED = BooleanProperty.create("formed");
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public static final String JOB_ENERGY_IN = "E_IN";
    public static final String JOB_REDSTONE_IN = "R_IN";
    public static final String JOB_REDSTONE_OUT = "R_OUT";
    public static final String JOB_ITEM_IN = "I_IN";
    public static final String JOB_ITEM_OUT = "I_OUT";
    public static final String JOB_FLUID_IN = "F_IN";
    public static final String JOB_FLUID_OUT = "F_OUT";
    public static final int INDEX_ITEM_INPUT = 0;
    public static final int INDEX_ITEM_OUTPUT = 1;
    public static final int INDEX_ITEM_EXTRA_OUTPUT = 2;
    public static final String CUSTOM_NAME ="CUSTOMNAME";
    public static TileEntity getTileFromPos(World worldIn, BlockPos posIn)  {return worldIn.getTileEntity(posIn);}
    /*
West = -x
East = +X
North = -Z
South = +Z
this function will return the North-Western corner of the multi block to be formed
*/
    public static BlockPos findLowsestValueCorner(BlockPos centerPos, int xLength, int yLength, int zLength)  {
        if(centerPos == null)  return null;
        return new BlockPos(centerPos.getX() - (xLength / 2), centerPos.getY() - (yLength / 2), centerPos.getZ() - (zLength / 2));
    }

}
