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

    /*
        Converts rgb + opacity to argb which minecraft uses a lot of

        Ints are 32 bits so the bit layout is ->
        AAAAAAAA RRRRRRRR GGGGGGGG BBBBBBBB
          a = alpha
          r = red
          g = green
          b = blue
     */
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
