package com.thecowking.wrought.fluids;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class BaseMoltenFluid extends Fluid {
    public BaseMoltenFluid(String fluidName, ResourceLocation resourceLocation, ResourceLocation resourceLocation1) {
        super();
    }

    @Override
    public Item getFilledBucket() {
        return null;
    }

    @Override
    protected boolean canDisplace(FluidState fluidState, IBlockReader blockReader, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    protected Vector3d getFlow(IBlockReader blockReader, BlockPos pos, FluidState fluidState) {
        return null;
    }

    @Override
    public int getTickRate(IWorldReader p_205569_1_) {
        return 0;
    }

    @Override
    protected float getExplosionResistance() {
        return 0;
    }

    @Override
    public float getActualHeight(FluidState p_215662_1_, IBlockReader p_215662_2_, BlockPos p_215662_3_) {
        return 0;
    }

    @Override
    public float getHeight(FluidState p_223407_1_) {
        return 0;
    }

    @Override
    protected BlockState getBlockState(FluidState state) {
        return null;
    }

    @Override
    public boolean isSource(FluidState state) {
        return false;
    }

    @Override
    public int getLevel(FluidState state) {
        return 0;
    }

    @Override
    public VoxelShape func_215664_b(FluidState p_215664_1_, IBlockReader p_215664_2_, BlockPos p_215664_3_) {
        return null;
    }
}
