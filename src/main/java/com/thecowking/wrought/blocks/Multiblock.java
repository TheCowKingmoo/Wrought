package com.thecowking.wrought.blocks;

import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Multiblock {
    public static final BooleanProperty FORMED = BooleanProperty.create("formed");
    public static final String JOB_ENERGY_IN = "E_IN";
    public static final String JOB_REDSTONE_IN = "R_IN";
    public static final String JOB_REDSTONE_OUT = "R_OUT";
    public static final String JOB_ITEM_IN = "I_IN";
    public static final String JOB_FLUID_IN = "F_IN";
    public static final String JOB_FLUID_OUT = "F_OUT";
    public static TileEntity getTileFromPos(World worldIn, BlockPos posIn)  {return worldIn.getTileEntity(posIn);}
    /*
West = -x
East = +X
North = -Z
South = +Z
this function will return the North-Western corner of the multi block to be formed
*/
    public static BlockPos findLowsestValueCorner(BlockPos centerPos, int xLength, int yLength, int zLength)  {
        return new BlockPos(centerPos.getX() - (xLength / 2), centerPos.getY() - (yLength / 2), centerPos.getZ() - (zLength / 2));
    }

}
