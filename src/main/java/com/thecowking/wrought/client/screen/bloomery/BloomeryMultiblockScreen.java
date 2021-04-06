package com.thecowking.wrought.client.screen.bloomery;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.client.screen.MultiblockScreen;
import com.thecowking.wrought.inventory.containers.bloomery.BloomeryContainerMultiblock;
import com.thecowking.wrought.util.RenderHelper;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;


public class BloomeryMultiblockScreen extends MultiblockScreen<BloomeryContainerMultiblock> {
    private static final Logger LOGGER = LogManager.getLogger();

    private int progressBarStartX;
    private int progressBarStartY;
    private int progressBarWidth = SLOT_SIZE + SLOT_SEP;
    private int progressBarHeight = BLANK_ACTUAL_HEIGHT - 2*GUI_Y_MARGIN - 2*SLOT_SIZE - 4 * SLOT_SEP;

    private int statusButtonX;
    private int statusButtonY;
    private int statusButtonRadius = SLOT_SIZE;


    private int heatBarStartX;
    private int heatBarStartY;
    private int heatBarHeight = BLANK_ACTUAL_HEIGHT - 2*GUI_Y_MARGIN ;
    private int heatBarWidth = SLOT_SIZE / 2;

    private ResourceLocation PROGRESS_BAR = new ResourceLocation(Wrought.MODID, "textures/gui/h_c_progress_bar.png");

    protected BloomeryContainerMultiblock multiBlockContainer;

    public BloomeryMultiblockScreen(BloomeryContainerMultiblock container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.multiBlockContainer = container;
        this.xSize = BLANK_X_SIZE;
        this.ySize = BLANK_Y_SIZE;


        LOGGER.info("w = " + this.width + " x = " + this.xSize + " s = " + this.xStart());


        this.statusButtonX = GUI_X_MARGIN + SLOT_SIZE + SLOT_SEP;
        this.statusButtonY = GUI_Y_MARGIN;


        this.progressBarStartX = BLANK_X_SIZE - GUI_X_MARGIN - SLOT_SIZE - SLOT_SEP;
        this.progressBarStartY = GUI_Y_MARGIN + 2*SLOT_SIZE + 2*SLOT_SEP;

        this.heatBarStartX = GUI_X_MARGIN;
        this.heatBarStartY = GUI_Y_MARGIN;

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
            // tells user what the status is
        }  else if(x > xStart() + statusButtonX && x < xStart() + statusButtonX + statusButtonRadius && y > yStart() + statusButtonY && y < yStart() + statusButtonY + statusButtonRadius) {
            TranslationTextComponent displayName = new TranslationTextComponent(multiBlockContainer.getStatus());
            renderTooltip(stack, displayName, x, y);
            // tell user what the item is called
        }  else if(x > xStart() + heatBarStartX && x < xStart() + heatBarStartX + heatBarWidth && y > yStart() + heatBarStartY && y < yStart() + heatBarStartY + heatBarHeight)  {
            TranslationTextComponent displayName = new TranslationTextComponent("heat is = " + multiBlockContainer.getCurrentHeatLevel());
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
        this.minecraft.getTextureManager().bindTexture(BLANK_GUI_BACKGROUND);
        this.blit(stack, xStart(), yStart(), 0,0, this.xSize, this.ySize);



        slotRunner(stack, multiBlockContainer, this.minecraft.getTextureManager(), xStart(), yStart());

        // progress bar
        double cookingPercent = multiBlockContainer.getProgress();
        createProgressBar(stack, this.minecraft.getTextureManager(), xStart() + progressBarStartX, yStart() + progressBarStartY, progressBarWidth, progressBarHeight, cookingPercent);


        double heatPercent = multiBlockContainer.getHeatPercentage();

        int color = 0;
        if(this.multiBlockContainer.enoughHeatToCraft())  {
            color = RenderHelper.convertARGBToInt(255, 128, 0, 1);
        }  else  {
            color = RenderHelper.convertARGBToInt(255, 0, 0, 1);
        }

        drawHeatBar(stack, this.minecraft.getTextureManager(), xStart() + heatBarStartX, yStart() + heatBarStartY, heatBarWidth, heatBarHeight, heatPercent, color);

        //draw indicator
        RenderHelper.drawStatusIndicator(xStart() + statusButtonX, yStart() + statusButtonY, statusButtonRadius, getStatusColor());


    }

    protected ITextComponent getName() {
        return new TranslationTextComponent("Bloomery");
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

    public int getStatusColor()  {
        String status = multiBlockContainer.getStatus();
        if(status == "Processing")  {
            //yellow
            return RenderHelper.convertARGBToInt(255,255,0,1);
        } else if( status == "Standing By")  {
            //green
            return  RenderHelper.convertARGBToInt(0,255,0,1);
        }
        // red
        return RenderHelper.convertARGBToInt(255,0,0,1);
    }


}
