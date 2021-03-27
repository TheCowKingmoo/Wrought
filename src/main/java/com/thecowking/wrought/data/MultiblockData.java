package com.thecowking.wrought.data;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiblockData {
    public static final BooleanProperty FORMED = BooleanProperty.create("formed");
    public static final BooleanProperty RUNNING = BooleanProperty.create("running");
    public static final IntegerProperty REDSTONE = BlockStateProperties.POWER_0_15;
    public static final BooleanProperty JOB = BooleanProperty.create("job");


    public static final String INPUT_SLOTS = "input_slots";
    public static final String OUTPUT_SLOTS = "output_slots";
    public static final String FLUID_ITEM_INPUT_SLOTS = "fluid_item_input_slots";
    public static final String FLUID_ITEM_OUTPUT_SLOTS = "fluid_item_output_slots";
    public static final String FUEL_INPUT_SLOTS = "fuel_input_slots";






    public static final String JOB_ENERGY_IN = "E_IN";
    public static final String JOB_REDSTONE_IN = "R_IN";
    public static final String JOB_REDSTONE_OUT = "R_OUT";
    public static final String FLUID_TANK = "fluid_tank_";

    public static final String STATUS = "status";

    public static final String BURN_TIME = "burn_time";
    public static final String BURN_COMPLETE_TIME = "burn_complete_time";
    public static final String FUEL_TIME = "fuel_time";
    public static final String FUEL_TIME_COMPLETE = "fuel_complete_time";
    public static final String RECIPE_HEAT_LEVEL = "recipe_heat_level";

    public static final String HEAT_LEVEL = "heat_level";




    public static TileEntity getTileFromPos(World worldIn, BlockPos posIn)  {return worldIn.getTileEntity(posIn);}

    public static BlockPos getUnderlyingBlock(BlockPos posIn)  {
        if(posIn.getY() > 255 || posIn.getY() < 1)  {
            return null;
        }
        return new BlockPos(posIn.getX(), posIn.getY()-1, posIn.getZ());
    }

}
