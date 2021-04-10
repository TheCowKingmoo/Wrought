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

    private ResourceLocation PROGRESS_BAR = new ResourceLocation(Wrought.MODID, "textures/gui/h_c_progress_bar.png");

    protected BloomeryContainerMultiblock multiBlockContainer;

    public BloomeryMultiblockScreen(BloomeryContainerMultiblock container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.multiBlockContainer = container;
        this.xSize = BLANK_X_SIZE;
        this.ySize = BLANK_Y_SIZE;


        LOGGER.info("w = " + this.width + " x = " + this.xSize + " s = " + this.xStart());



        this.indicatorXOffset = GUI_X_MARGIN + SLOT_SIZE + SLOT_SEP;
        this.indicatorYOffset = GUI_Y_MARGIN;

        this.progressBarXOffset = BLANK_X_SIZE - GUI_X_MARGIN - SLOT_SIZE - SLOT_SEP;
        this.progressBarYOffset = GUI_Y_MARGIN + 2*SLOT_SIZE + 2*SLOT_SEP;

        this.progressBarWidth = SLOT_SIZE + SLOT_SEP;
        this.progressBarHeight = BLANK_ACTUAL_HEIGHT - 2*GUI_Y_MARGIN - 2*SLOT_SIZE - 4 * SLOT_SEP;

        this.heatBarXOffset = GUI_X_MARGIN;
        this.heatBarYOffset = GUI_Y_MARGIN;
        this.heatBarWidth = SLOT_SIZE / 2;
        this.heatBarHeight = BLANK_ACTUAL_HEIGHT - 2*GUI_Y_MARGIN ;

    }


    @Override
    public void render(MatrixStack stack, int x, int y, float partialTicks)  {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderBackground(stack);
        super.render(stack, x, y, partialTicks);
        this.renderHoveredTooltip(stack, x, y);
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
        createProgressBar(stack, this.minecraft.getTextureManager(), xStart() + progressBarXOffset, yStart() + progressBarYOffset, progressBarWidth, progressBarHeight, cookingPercent);


        double heatPercent = multiBlockContainer.getHeatPercentage();


        drawHeatBar(stack, this.minecraft.getTextureManager(), xStart() + heatBarXOffset, yStart() + heatBarYOffset, heatBarWidth, heatBarHeight, heatPercent, getHeatColor());

        //draw indicator
        drawStatusIndicator(stack);
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

}
