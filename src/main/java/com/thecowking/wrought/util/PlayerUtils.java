package com.thecowking.wrought.util;

import com.mojang.authlib.GameProfile;
import com.thecowking.wrought.Wrought;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.LoggingPrintStream;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.UUID;

public class PlayerUtils {
    private static String fakeName = "wrought_autobuilder";


    // Source - https://github.com/McJtyMods/McJtyLib/blob/1.16/src/main/java/mcjty/lib/varia/BlockTools.java
    public static boolean placeBlockUsingPlayer(PlayerEntity player, BlockPos pos, BlockState blockState)  {

        if(!(blockState.getBlock().asItem() instanceof BlockItem))  return false;
        BlockItem itemBlock = (BlockItem) blockState.getBlock().asItem();

        Direction dir = Direction.UP;

        if(blockState.getBlock() instanceof StairsBlock)  {
            dir = blockState.get(StairsBlock.FACING);
        }

        BlockRayTraceResult trace = new BlockRayTraceResult(new Vector3d(pos.getX(), pos.getY(), pos.getZ()), dir, pos, false);
        BlockItemUseContext context = new BlockItemUseContext(new ItemUseContext(player, Hand.MAIN_HAND, trace));
        Wrought.LOGGER.info(context.getNearestLookingDirection());


        if(blockState == null)  {
            blockState = blockState.getBlock().getStateForPlacement(context);
            // cannot place whatever this gross block is
            if(blockState == null) return false;
        }

        if(itemBlock.tryPlace(context).isSuccessOrConsume()) return true;
        return false;
    }

    public static boolean canFakePlayerPlace(PlayerEntity player, BlockPos pos, BlockState blockState)  {
        if(!(blockState.getBlock().asItem() instanceof BlockItem))  return false;

        BlockRayTraceResult trace = new BlockRayTraceResult(new Vector3d(pos.getX(), pos.getY(), pos.getZ()), Direction.UP, pos, false);
        BlockItemUseContext context = new BlockItemUseContext(new ItemUseContext(player, Hand.MAIN_HAND, trace));
        return context.canPlace();
    }

    public static FakePlayer createFakePlayer(ServerWorld world)  {
       return FakePlayerFactory.get(world,  new GameProfile(UUID.nameUUIDFromBytes(fakeName.getBytes()), fakeName));
    }


}
