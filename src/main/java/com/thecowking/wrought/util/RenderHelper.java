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

import java.awt.*;
import java.util.function.Function;

public class RenderHelper {
    private static final Logger LOGGER = LogManager.getLogger();

    // getting fluids texture source from cyclops core
    public static final Function<ResourceLocation, TextureAtlasSprite> TEXTURE_GETTER =
            location -> Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(location);

    public static final ResourceLocation TANK_BACKGROUND = new ResourceLocation(Wrought.MODID, "textures/gui/tank_frame.png");
    public static final ResourceLocation TANK_GAUGE = new ResourceLocation(Wrought.MODID, "textures/gui/tank_gauge.png");
    public static final ResourceLocation BLANK_GUI_BACKGROUND = new ResourceLocation(Wrought.MODID, "textures/gui/background_and_inventory.png");
    public static final ResourceLocation SLOT_IMAGE = new ResourceLocation(Wrought.MODID, "textures/gui/slot.png");
    public static final ResourceLocation PROGRESS_BAR = new ResourceLocation(Wrought.MODID, "textures/gui/h_c_progress_bar.png");
    public static final int SLOT_WIDTH_HEIGHT = 18;


    public static final int TANK_WIDTH = 18;
    public static final int TANK_HEIGHT = 56;


    public static int GUI_X_MARGIN = 10;
    public static int SLOT_SIZE = 18;
    public static int SLOT_SEP = 2;
    public static int GUI_Y_MARGIN = 20;
    public static int X_SIZE = 176;
    public static int Y_SIZE = 240;



    public static void slotRunner(MatrixStack stack, MultiBlockContainer container, TextureManager manager, int xStart, int yStart)  {
        for(int i = 36; i < 36 + container.getNumMachineSlots(); i++)  {
            int x = container.getSlot(i).xPos;
            int y = container.getSlot(i).yPos;
            createSlot(stack, x+xStart-1, y+yStart-1, manager);
        }

    }

    // TODO - this will need to be reworked if i ever get all these images into one file
    // TODO - was told that I sohuld use a power of two as well
    public static void createSlot(MatrixStack stack, int x, int y, TextureManager manager)  {
        manager.bindTexture(SLOT_IMAGE);
        AbstractGui.blit(stack, x, y, 0, 0, 18, 18, 18, 18);
    }

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

        //LOGGER.info("alpha = " + (startColor >> 24 & 255) + " r = " + (startColor >> 16 & 255) + " g =  " + (startColor >> 8 & 255) + " b = " + (startColor & 255));

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


    public static void createTankBackGround(MatrixStack stack, int x, int y, TextureManager manager, int width, int height)  {
        manager.bindTexture(TANK_BACKGROUND);
        AbstractGui.blit(stack, x, y, 0, 0, width, height, TANK_WIDTH, TANK_HEIGHT);
    }

    public static void createTankGauge(MatrixStack stack, int x, int y, TextureManager manager, int width, int height)  {
        manager.bindTexture(TANK_GAUGE);
        AbstractGui.blit(stack, x, y, 0, 0, width, height, TANK_WIDTH, TANK_HEIGHT);
    }

    public static FluidStack getFluidInTank(MultiBlockContainerFluid container, int index)  {
        return container.getFluidController().getFluidInTank(index);
    }
    public static int getTanksMaxSize(MultiBlockContainerFluid container, int index)  {
        return container.getFluidController().getOutputTankMaxSize(index);
    }
    public static int getFluidInTanksHeight(MultiBlockContainerFluid container, int tankIndex)  {
        return (int)(TANK_HEIGHT * container.getFluidController().getPercentageInTank(tankIndex));
    }



}
