package com.thecowking.wrought.network;

import com.thecowking.wrought.data.HCCokeOvenData;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.util.MultiBlockHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketFormMultiblock {
    private final BlockPos controllerPos;


    public PacketFormMultiblock(PacketBuffer buf)  {
        this.controllerPos = buf.readBlockPos();
    }

    public PacketFormMultiblock(BlockPos controllerPos)  {
        this.controllerPos = controllerPos;
    }

    public void toBytes(PacketBuffer buf)  {
        buf.writeBlockPos(this.controllerPos);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx)  {
        ctx.get().enqueueWork(() -> {
            ServerWorld serverWorld = ctx.get().getSender().getServerWorld();
            MultiBlockHelper.autoBuildMultiblock(serverWorld, ctx.get().getSender(), this.controllerPos, ((MultiBlockControllerTile)serverWorld.getTileEntity(this.controllerPos)).getData());
            MultiBlockHelper.tryToFormMultiBlock(serverWorld, ctx.get().getSender(), this.controllerPos, ((MultiBlockControllerTile)serverWorld.getTileEntity(this.controllerPos)).getData());
            ctx.get().getSender().closeScreen();
                }
        );
        ctx.get().setPacketHandled(true);
    }

}
