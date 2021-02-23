package com.thecowking.wrought.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;
import java.util.function.Function;

public class RenderHelper {



    // getting fluids texture source from cyclops core
    public static final Function<ResourceLocation, TextureAtlasSprite> TEXTURE_GETTER =
            location -> Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(location);

    public static TextureAtlasSprite getFluidTexture(FluidStack fluid)  {
        return TEXTURE_GETTER.apply(fluid.getFluid().getAttributes().getStillTexture(fluid));
    }


    //https://stackoverflow.com/questions/7358533/how-to-pack-argb-to-one-integer-uniquely


    // ints are 32 bits
    // AAAAAAAA RRRRRRRR GGGGGGGG BBBBBBBB
    public static int convertARGBToInt(int r, int g, int b, double a)  {
        int intA = (int)(a * 256);
        int red = r & 0xFF;
        int blue = b & 0xFF;
        int green = g  & 0xFF;
        int alpha = intA & 0xFF;
        int argb = (alpha<<24) | (red << 16) | (green << 8) | (blue) ;
        return argb;
    }

}
