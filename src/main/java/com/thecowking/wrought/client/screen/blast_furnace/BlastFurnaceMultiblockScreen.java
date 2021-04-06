package com.thecowking.wrought.client.screen.blast_furnace;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thecowking.wrought.client.screen.MultiBlockFluidScreen;
import com.thecowking.wrought.client.screen.MultiblockScreen;
import com.thecowking.wrought.inventory.containers.blast_furnace.BlastFurnaceContainerMultiblock;
import com.thecowking.wrought.inventory.containers.honey_comb_coke_oven.HCCokeOvenContainerMultiblock;
import com.thecowking.wrought.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import static com.thecowking.wrought.data.BlastFurnaceData.*;


public class BlastFurnaceMultiblockScreen extends MultiBlockFluidScreen<BlastFurnaceContainerMultiblock> {



    final static int TANK_X_OFFSET = 176 - 18 - 4 - 10;
    final static int TANK_Y_OFFSET = 5;


    final static int METAL_TANK_INDEX = 0;
    final static int SLAG_TANK_INDEX = 1;

    public static final int TANK_WIDTH = 18;
    public static final int TANK_HEIGHT = 70;

    private int heatBarHeight = BLANK_ACTUAL_HEIGHT - 2 * GUI_Y_MARGIN ;
    private int heatBarWidth = SLOT_SIZE / 2;
    private int heatBarStartX;
    private int heatBarStartY;


    private int progressBarStartX;
    private int progressBarStartY;
    private int progressBarWidth = SLOT_SIZE + SLOT_SEP;
    private int progressBarHeight = BLANK_ACTUAL_HEIGHT - 2*GUI_Y_MARGIN - 2*SLOT_SIZE - 4 * SLOT_SEP;

    private int statusButtonX;
    private int statusButtonY;
    private int statusButtonRadius = SLOT_SIZE;


    public BlastFurnaceMultiblockScreen(BlastFurnaceContainerMultiblock container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.heatBarStartX = GUI_X_MARGIN;
        this.heatBarStartY = GUI_Y_MARGIN;

        this.indicatorXOffset = GUI_X_MARGIN + SLOT_SIZE + SLOT_SEP;;
        this.indicatorYOffset = GUI_Y_MARGIN;


        this.statusButtonX = GUI_X_MARGIN + SLOT_SIZE + SLOT_SEP;
        this.statusButtonY = GUI_Y_MARGIN;


        this.progressBarStartX = 2* SLOT_SIZE + SLOT_SEP;
        this.progressBarStartY = GUI_X_MARGIN + 3*SLOT_SIZE + 3*SLOT_SEP + GUI_Y_MARGIN;
    }

    /*
    @Override
    public void render(MatrixStack stack, int x, int y, float partialTicks)  {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderBackground(stack);
        super.render(stack, x, y, partialTicks);
        this.renderHoveredTooltip(stack, x, y);
    }

     */

    /*
        Is called as the mouse moves around
     */

    @Override
    protected void renderHoveredTooltip(MatrixStack stack, int x, int y) {
        super.renderHoveredTooltip(stack, x, y);

        if(x > xStart() + TANK_X_OFFSET && x < xStart() + TANK_X_OFFSET + TANK_WIDTH && y > yStart() + TANK_Y_OFFSET && y < yStart() + TANK_Y_OFFSET + TANK_HEIGHT)  {
            FluidStack fluidStack = getFluidInTank(multiBlockContainerFluid, METAL_TANK_INDEX);
            TranslationTextComponent displayName = new TranslationTextComponent(fluidStack.getTranslationKey());
            TranslationTextComponent fluidAmount = new TranslationTextComponent(fluidStack.getAmount() + " / " + getTanksMaxSize(multiBlockContainerFluid, METAL_TANK_INDEX));
            renderTooltip(stack, displayName, x, y+10);
            renderTooltip(stack, fluidAmount, x, y+27);
            // debug
        } else if(this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null)  {
            renderTooltip(stack, new TranslationTextComponent(String.valueOf(this.hoveredSlot.slotNumber)) , x, y);

        }  else  {
            renderTooltip(stack, new TranslationTextComponent("x = " + x + " y = " + y) , x, y);
        }
    }

    /*
        Does as the name suggests -> draws the main background to the gui
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)  {
        // Draws the main background
        this.minecraft.getTextureManager().bindTexture(BLANK_GUI_BACKGROUND);
        this.blit(stack, xStart(), yStart(), 0,0, this.xSize, this.ySize);


        slotRunner(stack, multiBlockContainer, this.minecraft.getTextureManager(), xStart(), yStart());

        // progress bar
        double cookingPercent = multiBlockContainer.getProgress();
        createProgressBar(stack, this.minecraft.getTextureManager(), xStart() + progressBarStartX, yStart() + progressBarStartY, progressBarWidth, progressBarHeight, cookingPercent);

        int color = 0;
        if(this.multiBlockContainer.enoughHeatToCraft())  {
            color = RenderHelper.convertARGBToInt(255, 128, 0, 1);
        }  else  {
            color = RenderHelper.convertARGBToInt(255, 0, 0, 1);
        }
        double heatPercent = multiBlockContainer.getHeatPercentage();
        drawHeatBar(stack, this.minecraft.getTextureManager(), xStart() + heatBarStartX, yStart() + heatBarStartY, heatBarWidth, heatBarHeight, heatPercent, color);

        //    public static void drawFluid(MatrixStack matrixStack, FluidStack fluidStack, int x, int y, int width, int height, MultiBlockContainerFluid container, double percent)  {
        //draw first tank
        double firstTankPercent = multiBlockContainerFluid.getTankPercentFull(METAL_TANK_INDEX);
        int x = xStart() + X_SIZE - SLOT_SIZE  - GUI_X_MARGIN;
        int y = yStart() + TANK_Y_OFFSET;
        createTankBackGround(stack, x, y, DEFAULT_TANK_BACKGROUND, this.minecraft.getTextureManager(), TANK_WIDTH, TANK_HEIGHT, TANK_WIDTH, TANK_HEIGHT);
        RenderHelper.drawFluid(stack, getFluidInTank(multiBlockContainerFluid, METAL_TANK_INDEX), x, y, TANK_WIDTH, TANK_HEIGHT, multiBlockContainerFluid, firstTankPercent);
        createTankBackGround(stack, x, y, DEFAULT_TANK_GAUGE, this.minecraft.getTextureManager(), TANK_WIDTH, TANK_HEIGHT, TANK_WIDTH, TANK_HEIGHT);

        //draw second tank
        x = xStart() + X_SIZE - 2*SLOT_SIZE - SLOT_SEP - GUI_X_MARGIN;
        double secondTankPercent = multiBlockContainerFluid.getTankPercentFull(SLAG_TANK_INDEX);
        createTankBackGround(stack, x, y, DEFAULT_TANK_BACKGROUND, this.minecraft.getTextureManager(), TANK_WIDTH, TANK_HEIGHT, TANK_WIDTH, TANK_HEIGHT);
        RenderHelper.drawFluid(stack, getFluidInTank(multiBlockContainerFluid, SLAG_TANK_INDEX), x, y, TANK_WIDTH, TANK_HEIGHT, multiBlockContainerFluid, secondTankPercent);
        createTankBackGround(stack, x, y, DEFAULT_TANK_GAUGE, this.minecraft.getTextureManager(), TANK_WIDTH, TANK_HEIGHT, TANK_WIDTH, TANK_HEIGHT);
        drawStatusIndicator(stack);
    }


    protected ITextComponent getName() {
        return new TranslationTextComponent("Blast Furnace");
    }


}
