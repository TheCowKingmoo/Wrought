package com.thecowking.wrought.network;

import com.thecowking.wrought.blocks.INameableTile;
import com.thecowking.wrought.blocks.MultiBlock.MultiBlockControllerTile;
import com.thecowking.wrought.blocks.MultiBlock.honey_comb_coke_oven.HCCokeOvenControllerTile;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


/*
    Sources
        Documentation
            https://forge.gemwire.uk/wiki/SimpleChannel
        Example
            https://github.com/Lordmau5/FFS/tree/1.16.4/src/main/java/com/lordmau5/ffs/network
 */

public abstract class WroughtPacket {
    public static abstract class Client  {

    }
    public static abstract class Server  {


        public static class UpdateTile  {

            private BlockPos pos;
            private String name;
            private FluidStack fluidStack;
            private int progress;
            private int complete;

            public UpdateTile()  {

            }

            /*
                My understanding is that the packet works like this
                    -> write everything in encode to a byte array in order of the code
                    -> read in a byte array in order of the code
             */
            public UpdateTile(HCCokeOvenControllerTile te, String name)  {
                this.pos = te.getPos();
                //this.name = name;
                this.progress = te.operationProgression;
                this.complete = te.operationComplete;
                this.fluidStack = te.getFluidInTank();

            }
            public void encode(PacketBuffer buffer)  {
                buffer.writeBlockPos(this.pos);
                //buffer.writeString(this.name);
                buffer.writeInt(this.progress);
                buffer.writeInt(this.complete);
                //fluidStack.writeToPacket(buffer);
            }

            public static UpdateTile decode(PacketBuffer buffer)  {
                UpdateTile packet = new UpdateTile();
                packet.pos = buffer.readBlockPos();
                //packet.name = buffer.readString();
                packet.progress = buffer.readInt();
                packet.complete = buffer.readInt();
                //packet.fluidStack = buffer.readFluidStack();
                return packet;
            }

            public BlockPos getPos()  {return this.pos;}
            public String getName() {return name;}
            public int getProgress() {return this.progress;}
            public int getComplete()    {return this.complete;}
            public FluidStack getFluidStack() {return this.fluidStack;}

            public static void onReceived(UpdateTile msg, Supplier<NetworkEvent.Context> ctxSupplier)  {
                NetworkEvent.Context ctx = ctxSupplier.get();

                ctx.enqueueWork(() -> {
                    ServerPlayerEntity playerEntity = ctx.getSender();
                    World world = playerEntity != null ? playerEntity.world : null;

                    if (world != null) {
                        TileEntity tile = world.getTileEntity(msg.getPos());
                        if (tile instanceof MultiBlockControllerTile && tile instanceof INameableTile) {
                            MultiBlockControllerTile controllerTile = (MultiBlockControllerTile) tile;
                            ((INameableTile) controllerTile).setTileName(msg.getName());
                            controllerTile.markForUpdateNow();
                        }
                    }
                });

            }


        }

    }
}
