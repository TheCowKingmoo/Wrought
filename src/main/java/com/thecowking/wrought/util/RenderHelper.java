package com.thecowking.wrought.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.inventory.containers.MultiBlockContainer;
import com.thecowking.wrought.inventory.containers.MultiBlockContainerFluid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.function.Function;

public class RenderHelper {
    private static final Logger LOGGER = LogManager.getLogger();

    // getting fluids texture source from cyclops core
    public static final Function<ResourceLocation, TextureAtlasSprite> TEXTURE_GETTER =
            location -> Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(location);








    // TODO - this will need to be reworked if i ever get all these images into one file
    // TODO - was told that I sohuld use a power of two as well

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
        int red = r & 255;
        int blue = b & 255;
        int green = g  & 255;
        int alpha = intA & 255;
        int argb = (255<<24) + (red << 16) + (green << 8) + (blue) ;
        return argb;
    }

    public static void fillGradient(int left, int top, int right, int bottom, int startColor, int endColor, float zLevel) {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;

        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        GlStateManager.disableAlphaTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)right, (double)top, (double)zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double)left, (double)top, (double)zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double)left, (double)bottom, (double)zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, (double)zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableTexture();
    }


    public static int getCenterPixelValue(TextureAtlasSprite icon)  {
        int frameCount = icon.getFrameCount();
        LOGGER.info("frame count = " + frameCount);
        int height = icon.getHeight();
        LOGGER.info("height = " + height);
        int width = icon.getWidth();
        LOGGER.info(width);
        int RGBA = icon.getPixelRGBA(frameCount, width / 2, height / 2);
        LOGGER.info("RGBA = " + RGBA);
        return RGBA;
    }


    public static void drawStatusIndicator(int x, int y, int radius, int color)  {
        int black = convertARGBToInt(0,0,0,1);
        RenderHelper.fillGradient(x, y, x+ radius, y + radius, black, black, 0F);
        RenderHelper.fillGradient(x+1, y+1, x + radius - 1, y + radius - 1, color, color, 0F);
        int halfX = x + radius/2 - 1;
        int halfY = y + radius/2 - 1;
        RenderHelper.fillGradient(halfX, y, halfX + 2, y + radius, black, black, 0F);
        RenderHelper.fillGradient(x, halfY, x + radius, halfY + 2, black, black, 0F);

    }


    public static void drawFluid(MatrixStack matrixStack, FluidStack fluidStack, int x, int y, int width, int height, MultiBlockContainerFluid container, double percent)  {
        if(fluidStack == null || fluidStack.isEmpty())  {
            return;
        }
        matrixStack.push();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("textures/atlas/blocks.png"));
        int color = fluidStack.getFluid().getAttributes().getColor(fluidStack);
        setGLColorFromInt(color);


        fillGradient(x + 1, y  + height - (int)(percent *  height + 1), x + width - 1, y + height - 1, color, color, 0F);


        drawTiledTexture(x, y + height, getTexture(fluidStack.getFluid().getAttributes().getStillTexture(fluidStack)), width, (int)(height * percent), fluidStack.getAmount() / 1000);

        matrixStack.pop();
    }

    public static void drawTiledTexture(int x, int y, TextureAtlasSprite icon, int width, int height, int numBuckets) {
        int i;
        int j;

        int drawHeight;
        int drawWidth;

        for (i = 0; i < width; i += 16) {
            for (j = 0; j < height; j += 16) {
                drawWidth = Math.min(width - i, 16);
                drawHeight = Math.min(height - j, 16);
                drawScaledTexturedModelRectFromIcon(x + i, y - j, icon, drawWidth, drawHeight);
            }
        }
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static TextureAtlasSprite getTexture(ResourceLocation location) {
        return Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(location);
    }

    public static void drawScaledTexturedModelRectFromIcon(int x, int y, TextureAtlasSprite icon, int width, int height) {
        if ( icon == null ) {
            return;
        }
        float minU = icon.getMinU();
        float maxU = icon.getMaxU();
        float minV = icon.getMinV();
        float maxV = icon.getMaxV();

        float zLevel = 0f;

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        // Bottom Left
        buffer.pos(x, y, zLevel).tex(minU, minV + (maxV - minV) * height / 16F).endVertex();
        // Bottom Right
        buffer.pos(x + width, y, zLevel).tex(minU + (maxU - minU) * width / 16F, minV + (maxV - minV) * height / 16F).endVertex();
        // Top Right
        buffer.pos(x + width, y - height, zLevel).tex(minU + (maxU - minU) * width / 16F, minV).endVertex();
        // Top Left
        buffer.pos(x, y - height, zLevel).tex(minU, minV).endVertex();
        // Draw
        Tessellator.getInstance().draw();
    }




    public static void setGLColorFromInt(int color) {
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        GlStateManager.color4f(red, green, blue, 1.0F);
    }



}
