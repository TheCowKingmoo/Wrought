package com.thecowking.wrought.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
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

        BlockRayTraceResult trace = new BlockRayTraceResult(new Vector3d(0, 0, 0), Direction.UP, pos, false);
        BlockItemUseContext context = new BlockItemUseContext(new ItemUseContext(player, Hand.MAIN_HAND, trace));

        if(blockState == null)  {
            blockState = blockState.getBlock().getStateForPlacement(context);
            // cannot place whatever this gross block is
            if(blockState == null) return false;
        }

        if(itemBlock.tryPlace(context).isSuccessOrConsume()) return true;
        return false;
    }

    public static FakePlayer createFakePlayer(ServerWorld world)  {
       return FakePlayerFactory.get(world,  new GameProfile(UUID.nameUUIDFromBytes(fakeName.getBytes()), fakeName));
    }


}
