package com.thecowking.wrought.blocks.refactory_brick;

import com.thecowking.wrought.blocks.IMultiBlockFrame;
import com.thecowking.wrought.blocks.MultiBlockFrameBlock;
import com.thecowking.wrought.blocks.MultiblockFrameSlab;
import com.thecowking.wrought.blocks.honey_comb_coke_oven.HCCokeOvenFrameBlock;
import com.thecowking.wrought.data.MultiblockData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import static com.thecowking.wrought.data.MultiblockData.FORMED;
import static com.thecowking.wrought.data.MultiblockData.getUnderlyingBlock;

public class RefactoryBrickSlab extends MultiblockFrameSlab {

}