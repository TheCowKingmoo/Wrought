package com.thecowking.wrought.client.screen.casting_machine;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thecowking.wrought.client.screen.MultiBlockFluidScreen;
import com.thecowking.wrought.client.screen.MultiblockScreen;
import com.thecowking.wrought.inventory.containers.blast_furnace.BlastFurnaceContainerMultiblock;
import com.thecowking.wrought.inventory.containers.casting_machine.CastingMachineContainerMultiblock;
import com.thecowking.wrought.util.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;


public class CastingMachineMultiblockScreen extends MultiBlockFluidScreen<CastingMachineContainerMultiblock> {


    private int progressBarStartX;
    private int progressBarStartY;
    private int progressBarWidth = SLOT_SIZE + SLOT_SEP;
    private int progressBarHeight = BLANK_ACTUAL_HEIGHT - 2*GUI_Y_MARGIN - 2*SLOT_SIZE - 4 * SLOT_SEP;

    private int statusButtonX;
    private int statusButtonY;
    private int statusButtonRadius = SLOT_SIZE;


    public CastingMachineMultiblockScreen(CastingMachineContainerMultiblock container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name, 2);

        this.indicatorXOffset = GUI_X_MARGIN + SLOT_SIZE + SLOT_SEP;;
        this.indicatorYOffset = GUI_Y_MARGIN;


        this.statusButtonX = GUI_X_MARGIN + SLOT_SIZE + SLOT_SEP;
        this.statusButtonY = GUI_Y_MARGIN;

        // idea for the indexed slot is that this will be the middle most input slot
        this.progressBarStartX = this.multiBlockContainer.xSlot[0] + MultiblockScreen.SLOT_SIZE / 2;
        this.progressBarStartY = this.multiBlockContainer.ySlot[0] + 2*MultiblockScreen.SLOT_SIZE + MultiblockScreen.SLOT_SEP;

        this.tankXOffset[0] = this.multiBlockContainer.xSlot[0] - 1;
        this.tankYOffset[0] = this.multiBlockContainer.ySlot[0] - TANK_HEIGHT - MultiblockScreen.SLOT_SEP;

    }

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

        // draw input tank
        int x = xStart() + tankXOffset[0];
        int y = tankYOffset[0];
        double secondTankPercent = multiBlockContainerFluid.getInputTankPercentFull(0);
        createTankBackGround(stack, x, y, DEFAULT_TANK_BACKGROUND, this.minecraft.getTextureManager(), TANK_WIDTH, TANK_HEIGHT, TANK_WIDTH, TANK_HEIGHT);
        RenderHelper.drawFluid(stack, getFluidInTank(multiBlockContainerFluid, 0), x, y, TANK_WIDTH, TANK_HEIGHT, multiBlockContainerFluid, secondTankPercent);
        createTankBackGround(stack, x, y, DEFAULT_TANK_GAUGE, this.minecraft.getTextureManager(), TANK_WIDTH, TANK_HEIGHT, TANK_WIDTH, TANK_HEIGHT);

        drawStatusIndicator(stack);
    }


    @Override
    protected void renderHoveredTooltip(MatrixStack stack, int x, int y) {
        super.renderHoveredTooltip(stack, x, y);
        if(this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null)  {
            renderTooltip(stack, new TranslationTextComponent(String.valueOf(this.hoveredSlot.slotNumber)) , x, y - 20);

        }  else  {
            renderTooltip(stack, new TranslationTextComponent("x = " + x + " y = " + y) , x, y - 10);
        }
    }

    protected ITextComponent getName() {
        return new TranslationTextComponent("Casting Machine");
    }

}
