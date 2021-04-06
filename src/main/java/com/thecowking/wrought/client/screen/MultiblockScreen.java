package com.thecowking.wrought.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.inventory.containers.MultiBlockContainer;
import com.thecowking.wrought.inventory.containers.MultiBlockContainerFluid;
import com.thecowking.wrought.util.RenderHelper;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;


public class MultiblockScreen <MULTICONTAINER extends MultiBlockContainer> extends ContainerScreen<MULTICONTAINER> {

    protected int indicatorXOffset = 0;
    protected int indicatorYOffset = 0;
    protected int indicatorHeight = 0;
    protected int indicatorWidth = 0;

    public static final int SLOT_WIDTH_HEIGHT = 18;
    public static final int TANK_WIDTH = 18;
    public static final int TANK_HEIGHT = 56;

    public static int GUI_X_MARGIN = 10;
    public static int SLOT_SIZE = 18;
    public static int SLOT_SEP = 2;
    public static int GUI_Y_MARGIN = 20;

    public static int BLANK_X_SIZE = 176;
    public static int BLANK_Y_SIZE = 240;
    public static int BLANK_TITLE_HEIGHT = 12;
    public static int BLANK_ACTUAL_HEIGHT = 118;
    public static int BLANK_USABLE_HEIGHT = BLANK_ACTUAL_HEIGHT - BLANK_TITLE_HEIGHT;

    protected MultiBlockContainer multiBlockContainer;

    public static final ResourceLocation BLANK_GUI_BACKGROUND = new ResourceLocation(Wrought.MODID, "textures/gui/background_and_inventory.png");
    public static final ResourceLocation DEFAULT_SLOT_IMAGE = new ResourceLocation(Wrought.MODID, "textures/gui/slot.png");
    public static final ResourceLocation DEFAULT_PROGRESS_BAR = new ResourceLocation(Wrought.MODID, "textures/gui/h_c_progress_bar.png");
    public static final ResourceLocation DOWN_ARROW_CUTOUT = new ResourceLocation(Wrought.MODID, "textures/gui/downarrowcutout.png");


    public MultiblockScreen(MULTICONTAINER screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.multiBlockContainer = screenContainer;
        this.xSize = 176;
        this.ySize = 240;
        this.indicatorWidth = 11;
        this.indicatorHeight = 11;
    }

    public int xStart() {
        return (this.width - this.xSize) / 2;
    }
    public int yStart() {
        return (this.height - this.ySize) / 2;
    }


    @Override
    public void render(MatrixStack stack, int x, int y, float partialTicks)  {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderBackground(stack);
        super.render(stack, x, y, partialTicks);
        this.renderHoveredTooltip(stack, x, y);
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack stack, int x, int y) {
        // highlights the item the player is hovering over
        if (this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
            this.renderTooltip(stack, this.hoveredSlot.getStack(), x, y);
        }  else if(x > xStart() + indicatorXOffset && x < xStart() + indicatorXOffset + indicatorWidth && y > yStart() + indicatorYOffset && y < yStart() + indicatorYOffset + indicatorHeight) {
            TranslationTextComponent displayName = new TranslationTextComponent(multiBlockContainer.getStatus());
            renderTooltip(stack, displayName, x, y);
        }

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
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

    protected void drawStatusIndicator(MatrixStack stack)  {
        int color = getStatusColor(this.container.getStatus());
        RenderHelper.fillGradient(xStart() + indicatorXOffset, yStart() + indicatorYOffset, xStart() + indicatorXOffset + indicatorWidth, yStart() + indicatorYOffset + indicatorHeight, color, color, 0F);
    }


        /*
        1. Draws a black background
        2. Draws a box that expands downwards the larger the processTime is.
            The main gui has an arrow cutout that will go over thi process box and give the appearnce of an arrow.
     */
    public void createProgressBar(MatrixStack stack, TextureManager manager, int x, int y, int width, int height, double percent)  {
        int adjX = x - width / 2;
        int adjY = y - height / 2;

        int backgroundColor = RenderHelper.convertARGBToInt(0, 0, 0, 1);
        RenderHelper.fillGradient(adjX, adjY, adjX + width, adjY + height, backgroundColor, backgroundColor, 0F);

        int color = RenderHelper.convertARGBToInt(255, 255, 0, 1);
        RenderHelper.fillGradient(adjX, adjY, adjX + width, (int)(adjY + height * percent), color, color, 0F);

        manager.bindTexture(DOWN_ARROW_CUTOUT);
        AbstractGui.blit(stack, adjX, adjY, 0, 0, width, height, width, height);
    }


    public void drawHeatBar(MatrixStack stack, TextureManager manager, int x, int y, int width, int height, double percent, int color)  {
        int backgroundColor = RenderHelper.convertARGBToInt(0, 0, 0, 1);
        RenderHelper.fillGradient(x, y, x + width, y + height, backgroundColor, backgroundColor, 0F);
        RenderHelper.fillGradient(x + 1, y  + height - (int)(percent *  height + 1), x + width - 1, y + height - 1, color, color, 0F);
    }



    public void slotRunner(MatrixStack stack, MultiBlockContainer container, TextureManager manager, int xStart, int yStart)  {
        for(int i = 36; i < 36 + multiBlockContainer.getNumMachineSlots(); i++)  {
            int x = multiBlockContainer.getSlot(i).xPos;
            int y = multiBlockContainer.getSlot(i).yPos;
            createSlot(stack, x+xStart-1, y+yStart-1, DEFAULT_SLOT_IMAGE, manager);
        }
    }

    public void createSlot(MatrixStack stack, int x, int y, ResourceLocation slot, TextureManager manager)  {
        manager.bindTexture(slot);
        AbstractGui.blit(stack, x, y, 0, 0, 18, 18, 18, 18);
    }


    public int getStatusColor(String status)  {
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
