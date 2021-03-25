package com.thecowking.wrought.client.screen.blast_furnace;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
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


public class BlastFurnaceMultiblockScreen extends ContainerScreen<BlastFurnaceContainerMultiblock> {
    final static int COOK_BAR_X_OFFSET = 14;
    final static  int COOK_BAR_Y_OFFSET = 40;
    final static  int COOK_BAR_ICON_U = 0;   // texture position of white arrow icon [u,v]
    final static  int COOK_BAR_ICON_V = 207;
    final static  int COOK_BAR_WIDTH = 17;
    final static  int COOK_BAR_HEIGHT = 30;

    final static int INDICATOR_X_OFFSET = 39;
    final static int INDICATOR_Y_OFFSET = 48;
    final static int INDICATOR_HEIGHT = 11;
    final static int INDICATOR_WIDTH = 11;

    final static int TANK_X_OFFSET = 176 - 18 - 4 - 10;
    final static int TANK_Y_OFFSET = 19;


    final static int METAL_TANK_INDEX = 0;
    final static int SLAG_TANK_INDEX = 1;

    public static final int TANK_WIDTH = 18;
    public static final int TANK_HEIGHT = 56;

    protected BlastFurnaceContainerMultiblock multiBlockContainer;
    private int heatBarHeight = RenderHelper.BLANK_ACTUAL_HEIGHT - 2*RenderHelper.GUI_Y_MARGIN ;
    private int heatBarWidth = RenderHelper.SLOT_SIZE / 2;
    private int heatBarStartX;
    private int heatBarStartY;


    private int progressBarStartX;
    private int progressBarStartY;
    private int progressBarWidth = RenderHelper.SLOT_SIZE + RenderHelper.SLOT_SEP;
    private int progressBarHeight = RenderHelper.BLANK_ACTUAL_HEIGHT - 2*RenderHelper.GUI_Y_MARGIN - 2*RenderHelper.SLOT_SIZE - 4 * RenderHelper.SLOT_SEP;

    private int statusButtonX;
    private int statusButtonY;
    private int statusButtonRadius = RenderHelper.SLOT_SIZE;


    public BlastFurnaceMultiblockScreen(BlastFurnaceContainerMultiblock container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.multiBlockContainer = container;
        this.xSize = 176;
        this.ySize = 240;
        this.heatBarStartX = RenderHelper.GUI_X_MARGIN;
        this.heatBarStartY = RenderHelper.GUI_Y_MARGIN;


        this.statusButtonX = RenderHelper.GUI_X_MARGIN + RenderHelper.SLOT_SIZE + RenderHelper.SLOT_SEP;
        this.statusButtonY = RenderHelper.GUI_Y_MARGIN;


        this.progressBarStartX = RenderHelper.BLANK_X_SIZE - RenderHelper.GUI_X_MARGIN - RenderHelper.SLOT_SIZE - RenderHelper.SLOT_SEP;
        this.progressBarStartY = RenderHelper.GUI_Y_MARGIN + 2*RenderHelper.SLOT_SIZE + 2*RenderHelper.SLOT_SEP;
    }

    @Override
    public void render(MatrixStack stack, int x, int y, float partialTicks)  {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderBackground(stack);
        super.render(stack, x, y, partialTicks);
        this.renderHoveredTooltip(stack, x, y);
    }

    /*
        Is called as the mouse moves around
     */

    @Override
    protected void renderHoveredTooltip(MatrixStack stack, int x, int y) {

        // highlights the item the player is hovering over
        if (this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
            this.renderTooltip(stack, this.hoveredSlot.getStack(), x, y);

            // detects when the player is hovering over the tank
        }  else if(x > xStart() + TANK_X_OFFSET && x < xStart() + TANK_X_OFFSET + TANK_WIDTH && y > yStart() + TANK_Y_OFFSET && y < yStart() + TANK_Y_OFFSET + TANK_HEIGHT)  {
            FluidStack fluidStack = RenderHelper.getFluidInTank(multiBlockContainer, METAL_TANK_INDEX);
            TranslationTextComponent displayName = new TranslationTextComponent(fluidStack.getTranslationKey());
            TranslationTextComponent fluidAmount = new TranslationTextComponent(fluidStack.getAmount() + " / " + RenderHelper.getTanksMaxSize(multiBlockContainer, METAL_TANK_INDEX));
            renderTooltip(stack, displayName, x, y+10);
            renderTooltip(stack, fluidAmount, x, y+27);
            // debug
        }  else if(x > xStart() + INDICATOR_X_OFFSET && x < xStart() + INDICATOR_X_OFFSET + INDICATOR_WIDTH && y > yStart() + INDICATOR_Y_OFFSET && y < yStart() + INDICATOR_Y_OFFSET + INDICATOR_HEIGHT) {
            TranslationTextComponent displayName = new TranslationTextComponent(multiBlockContainer.getStatus());
            renderTooltip(stack, displayName, x, y);
        }  else if(this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null)  {
            renderTooltip(stack, new TranslationTextComponent(String.valueOf(this.hoveredSlot.slotNumber)) , x, y);

        }  else  {
            renderTooltip(stack, new TranslationTextComponent("x = " + x + " y = " + y) , x, y);
        }
    }


    public int xStart() {
        return (this.width - this.xSize) / 2;
    }
    public int yStart() {
        return (this.height - this.ySize) / 2;
    }


    /*
        Does as the name suggests -> draws the main background to the gui
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)  {
        // Draws the main background
        this.minecraft.getTextureManager().bindTexture(RenderHelper.BLANK_GUI_BACKGROUND);
        this.blit(stack, xStart(), yStart(), 0,0, this.xSize, this.ySize);


        RenderHelper.slotRunner(stack, multiBlockContainer, this.minecraft.getTextureManager(), xStart(), yStart());

        // progress bar
        double cookingPercent = multiBlockContainer.getProgress();
        RenderHelper.createProgressBar(stack, this.minecraft.getTextureManager(), xStart() + progressBarStartX, yStart() + progressBarStartY, progressBarWidth, progressBarHeight, cookingPercent);



        int color = 0;
        if(this.multiBlockContainer.enoughHeatToCraft())  {
            color = RenderHelper.convertARGBToInt(255, 128, 0, 1);
        }  else  {
            color = RenderHelper.convertARGBToInt(255, 0, 0, 1);
        }
        double heatPercent = multiBlockContainer.getHeatPercentage();
        RenderHelper.drawHeatBar(stack, this.minecraft.getTextureManager(), xStart() + heatBarStartX, yStart() + heatBarStartY, heatBarWidth, heatBarHeight, heatPercent, color);

        //draw first tank
        RenderHelper.createTankBackGround(stack, xStart() - TANK_WIDTH + X_SIZE - GUI_X_MARGIN - SLOT_SIZE - SLOT_SEP, yStart() + TANK_Y_OFFSET, this.minecraft.getTextureManager(), TANK_WIDTH, TANK_HEIGHT);
        RenderHelper.drawFluid(stack, RenderHelper.getFluidInTank(multiBlockContainer, METAL_TANK_INDEX), xStart() - TANK_WIDTH + X_SIZE - GUI_X_MARGIN - SLOT_SIZE - SLOT_SEP, yStart() + TANK_Y_OFFSET, TANK_WIDTH, TANK_HEIGHT, multiBlockContainer, METAL_TANK_INDEX);
        RenderHelper.createTankGauge(stack, xStart() - TANK_WIDTH + X_SIZE - GUI_X_MARGIN - SLOT_SIZE - SLOT_SEP, yStart() + TANK_Y_OFFSET, this.minecraft.getTextureManager(), TANK_WIDTH, TANK_HEIGHT);

        //draw second tank
        RenderHelper.createTankBackGround(stack, xStart() - TANK_WIDTH + X_SIZE - GUI_X_MARGIN - 3 * SLOT_SIZE - 3 * SLOT_SEP, yStart() + TANK_Y_OFFSET, this.minecraft.getTextureManager(), TANK_WIDTH, TANK_HEIGHT);
        RenderHelper.drawFluid(stack, RenderHelper.getFluidInTank(multiBlockContainer, SLAG_TANK_INDEX), xStart() - TANK_WIDTH + X_SIZE - GUI_X_MARGIN - 3 * SLOT_SIZE - 3 * SLOT_SEP, yStart() + TANK_Y_OFFSET, TANK_WIDTH, TANK_HEIGHT, multiBlockContainer, SLAG_TANK_INDEX);
        RenderHelper.createTankGauge(stack, xStart() - TANK_WIDTH + X_SIZE - GUI_X_MARGIN - 3 * SLOT_SIZE - 3 * SLOT_SEP, yStart() + TANK_Y_OFFSET, this.minecraft.getTextureManager(), TANK_WIDTH, TANK_HEIGHT);


        drawStatusIndicator(stack);

    }


    protected void drawStatusIndicator(MatrixStack stack)  {
        int color = RenderHelper.getStatusColor(this.container.getStatus());
        RenderHelper.fillGradient(xStart() + X_SIZE - SLOT_SEP, yStart() + SLOT_SEP, xStart() + INDICATOR_X_OFFSET + INDICATOR_WIDTH, yStart() + INDICATOR_Y_OFFSET + INDICATOR_HEIGHT, color, color, 0F);
    }

    protected ITextComponent getName() {
        return new TranslationTextComponent("Honey Comb Coke Oven");
    }

    /*
        This draws both title for the screen and the player inventory
        this had to be overridden as I cannot change the location of the titles otherwise
     */
    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.func_243248_b(matrixStack, this.title, (float)this.titleX, (float)this.titleY, 4210752);
        this.font.func_243248_b(matrixStack, this.playerInventory.getDisplayName(), (float)this.playerInventoryTitleX, (float)(this.playerInventoryTitleY+30), 4210752);
    }



}
