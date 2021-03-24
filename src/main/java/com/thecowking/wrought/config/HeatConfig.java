package com.thecowking.wrought.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class HeatConfig {
    public static ForgeConfigSpec.IntValue heatDissipation;
    public static void init(ForgeConfigSpec.Builder server, ForgeConfigSpec.Builder client)  {
        server.comment("Heat Dissipation");

        heatDissipation = server
                .comment("How quickly heat will dissipate in related machines. Every second the current heat will be divided by this number then subtracted from the original value")
                .defineInRange("heat.dissipate", 100, 1, 1000);
    }

}
