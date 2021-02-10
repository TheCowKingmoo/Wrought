package com.thecowking.wrought.network;

import com.google.common.base.Predicates;
import com.thecowking.wrought.Wrought;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;



/*
    source - https://forge.gemwire.uk/wiki/SimpleChannel
 */

public class WroughtNetworkHandler {

    private static int id = 0;
    private SimpleChannel networkChannel = null;


    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Wrought.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerChannels()  {
        registerBiDirectionalHandlers();
        registerServerHandlers();
        registerClientHandlers();

    }

    private static void registerBiDirectionalHandlers()  {
        // Update Tile
        INSTANCE.registerMessage(
                id++,
                WroughtPacket.Server.UpdateTile.class,
                WroughtPacket.Server.UpdateTile::encode,  // packet encode
                WroughtPacket.Server.UpdateTile::decode,  // packet decode
                WroughtPacket.Server.UpdateTile::onReceived   // packet onRecieved
        );

    }

    private static void registerServerHandlers()  {

    }
    private static void registerClientHandlers()  {

    }
    public static void sendPacketToPlayer(Object packet, ServerPlayerEntity player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}

