package com.thecowking.wrought.network;

import com.thecowking.wrought.Wrought;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel INSTANCE;
    private static int ID = 0;
    public static int nextID()  {return ID++;}

    public static void registerMessages()  {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Wrought.MODID, "blagh"), () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(nextID(),
                PacketFormMultiblock.class,
                // encoder
                PacketFormMultiblock::toBytes,
                // decoder
                PacketFormMultiblock::new,
                // handler
                PacketFormMultiblock::handle);
    }
    public static void sendToClient(Object packet, ServerPlayerEntity player) {
        INSTANCE.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }

}
