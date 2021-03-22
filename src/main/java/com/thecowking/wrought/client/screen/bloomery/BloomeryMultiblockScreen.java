package com.thecowking.wrought.client.screen.bloomery;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thecowking.wrought.Wrought;
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


public class BloomeryMultiblockScreen extends ContainerScreen<BloomeryContainerMultiblock> {
    final static int COOK_BAR_X_OFFSET = 14;
    final static  int COOK_BAR_Y_OFFSET = 40;
    final static  int COOK_BAR_ICON_U = 0;   // texture position of white arrow icon [u,v]
    final static  int COOK_BAR_ICON_V = 207;
    final static  int COOK_BAR_WIDTH = 18;
    final static  int COOK_BAR_HEIGHT = 18;
    private static final Logger LOGGER = LogManager.getLogger();


    final static int INDICATOR_X_OFFSET = 39;
    final static int INDICATOR_Y_OFFSET = 48;
    final static int INDICATOR_HEIGHT = 11;
    final static int INDICATOR_WIDTH = 11;


    private int HEAT_HEIGHT = 18;
    private int HEAT_WIDTH = HEAT_HEIGHT*3;


    private int statusIndicatorStartX;
    private int statusIndicatorStartY;

    private int heatBarStartX;
    private int heatBarStartY;

    private int progressBarStartX;
    private int progressBarStartY;

    private int SIZE_MACHINE_GUI_Y = 114;





    private ResourceLocation PROGRESS_BAR = new ResourceLocation(Wrought.MODID, "textures/gui/h_c_progress_bar.png");

    protected BloomeryContainerMultiblock multiBlockContainer;

    public BloomeryMultiblockScreen(BloomeryContainerMultiblock container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.multiBlockContainer = container;
        this.xSize = 184;
        this.ySize = 240;

        this.progressBarStartX = xStart() + this.xSize / 2 - COOK_BAR_WIDTH / 2;
        this.progressBarStartY = container.MIDDLE_Y - COOK_BAR_HEIGHT;

        this.statusIndicatorStartX = xStart() + RenderHelper.GUI_X_MARGIN;
        this.statusIndicatorStartY = 0;

        this.heatBarStartX = xStart() + RenderHelper.GUI_X_MARGIN;
        this.heatBarStartY = yStart() + this.ySize / 2 - RenderHelper.GUI_Y_MARGIN - HEAT_HEIGHT;;
    }


    public int getCenterX()  {
        return  xStart() + this.xSize / 2;
    }
    public int getCenterY()  {
        return  yStart() + this.SIZE_MACHINE_GUI_Y / 2;
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


        double percent = multiBlockContainer.getProgress();
        RenderHelper.createProgressBar(stack, this.minecraft.getTextureManager(), getCenterX(), getCenterY(), 64, 16, percent);
        // progress bar
        //drawProgressBar();

        //draw indicator
        drawStatusIndicator();

        // draw heat bar
        drawHeatBar();


    }


    protected void drawHeatBar()  {
        double percent = multiBlockContainer.getHeatPercentage();
        int color = RenderHelper.convertARGBToInt(255, 0, 0, 1);
        RenderHelper.fillGradient(heatBarStartX, heatBarStartY, (int)(heatBarStartX + HEAT_WIDTH * percent), heatBarStartY + HEAT_HEIGHT, color, color, 0F);
    }

    protected void drawStatusIndicator()  {
        int color = getStatusColor();
        RenderHelper.fillGradient(statusIndicatorStartX, statusIndicatorStartY, statusIndicatorStartX + INDICATOR_WIDTH, statusIndicatorStartY + INDICATOR_HEIGHT, color, color, 0F);
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
