package com.thecowking.wrought.blocks.MultiBlock;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Multiblock {
    public static final BooleanProperty FORMED = BooleanProperty.create("formed");
    public static final BooleanProperty RUNNING = BooleanProperty.create("running");
    public static final IntegerProperty REDSTONE = BlockStateProperties.POWER_0_15;


    public static final String JOB_ENERGY_IN = "E_IN";
    public static final String JOB_REDSTONE_IN = "R_IN";
    public static final String JOB_REDSTONE_OUT = "R_OUT";
    public static final String JOB_ITEM_IN = "I_IN";
    public static final String JOB_ITEM_OUT = "I_OUT";
    public static final String JOB_FLUID_IN = "F_IN";
    public static final String JOB_FLUID_OUT = "F_OUT";
    public static final String DIRECTION_FACING = "D_F";
    public static final String NUM_TICKS = "N_TICKS";

    public static final String INVENTORY_IN = "inv_in";
    public static final String INVENTORY_OUT = "inv_out";
    public static final String FLUID_INVENTORY_IN = "fluid_inv_in";
    public static final String FLUID_INVENTORY_OUT = "fluid_inv_out";
    public static final String FLUID_TANK = "fluid_tank";






    public static final String REDSTONE_IN_X = "R_IN_X";
    public static final String REDSTONE_OUT_X = "R_OUT_X";
    public static final String REDSTONE_IN_Y = "R_IN_Y";
    public static final String REDSTONE_OUT_Y = "R_OUT_Y";
    public static final String REDSTONE_IN_Z = "R_IN_Z";
    public static final String REDSTONE_OUT_Z = "R_OUT_Z";



    public static final int INDEX_ITEM_INPUT = 0;
    public static final int INDEX_ITEM_OUTPUT = 1;
    public static final int INDEX_FLUID_ITEM_INPUT = 2;
    public static final int INDEX_FLUID_ITEM_OUTPUT = 3;


    public static TileEntity getTileFromPos(World worldIn, BlockPos posIn)  {return worldIn.getTileEntity(posIn);}

    public static BlockPos getUnderlyingBlock(BlockPos posIn)  {
        if(posIn.getY() > 255 || posIn.getY() < 1)  {
            return null;
        }
        return new BlockPos(posIn.getX(), posIn.getY()-1, posIn.getZ());
    }

}
