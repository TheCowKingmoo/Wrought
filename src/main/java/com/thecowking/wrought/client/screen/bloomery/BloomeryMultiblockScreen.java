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
    final static  int COOK_BAR_WIDTH = 17;
    final static  int COOK_BAR_HEIGHT = 30;
    private static final Logger LOGGER = LogManager.getLogger();


    final static int INDICATOR_X_OFFSET = 39;
    final static int INDICATOR_Y_OFFSET = 48;
    final static int INDICATOR_HEIGHT = 11;
    final static int INDICATOR_WIDTH = 11;




    private ResourceLocation PROGRESS_BAR = new ResourceLocation(Wrought.MODID, "textures/gui/h_c_progress_bar.png");

    protected BloomeryContainerMultiblock multiBlockContainer;

    public BloomeryMultiblockScreen(BloomeryContainerMultiblock container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.multiBlockContainer = container;
        this.xSize = 176;
        this.ySize = 240;
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

        // progress bar exists behind the main background
        drawProgressBar(stack);

        //draw indicator before background
        drawStatusIndicator(stack);

        // Draws the main background
        this.minecraft.getTextureManager().bindTexture(RenderHelper.BLANK_GUI_BACKGROUND);
        this.blit(stack, xStart(), yStart(), 0,0, this.xSize, this.ySize);
        RenderHelper.slotRunner(stack, multiBlockContainer, this.minecraft.getTextureManager(), xStart(), yStart());


    }

    /*
        1. Draws a black background
        2. Draws a box that expands downwards the larger the processTime is.
            The main gui has an arrow cutout that will go over thi process box and give the appearnce of an arrow.
     */
    protected void drawProgressBar(MatrixStack stack)  {
        // draw a background for where the progress bar will not be

        // get texture for the progress bar
        this.minecraft.getTextureManager().bindTexture(PROGRESS_BAR);

        // gets the value from 0 to 1 of how much progress the cooking item has
        double processTime = multiBlockContainer.getProgress();

        // draw on screen
        this.blit(stack, xStart() + COOK_BAR_X_OFFSET, yStart() + COOK_BAR_Y_OFFSET, COOK_BAR_ICON_U, COOK_BAR_ICON_V,
                COOK_BAR_WIDTH, (int) (processTime * COOK_BAR_HEIGHT));
    }




    protected void drawStatusIndicator(MatrixStack stack)  {
        int color = getStatusColor();
        RenderHelper.fillGradient(xStart() + INDICATOR_X_OFFSET, yStart() + INDICATOR_Y_OFFSET, xStart() + INDICATOR_X_OFFSET + INDICATOR_WIDTH, yStart() + INDICATOR_Y_OFFSET + INDICATOR_HEIGHT, color, color, 0F);
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
