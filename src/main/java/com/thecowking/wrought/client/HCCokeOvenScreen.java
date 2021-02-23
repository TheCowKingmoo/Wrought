package com.thecowking.wrought.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.inventory.containers.HCCokeOvenContainer;
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


public class HCCokeOvenScreen extends ContainerScreen<HCCokeOvenContainer> {
    final static int COOK_BAR_X_OFFSET = 14;
    final static  int COOK_BAR_Y_OFFSET = 40;
    final static  int COOK_BAR_ICON_U = 0;   // texture position of white arrow icon [u,v]
    final static  int COOK_BAR_ICON_V = 207;
    final static  int COOK_BAR_WIDTH = 17;
    final static  int COOK_BAR_HEIGHT = 30;
    private static final Logger LOGGER = LogManager.getLogger();

    final static int TANK_X_OFFSET = 83;
    final static int TANK_Y_OFFSET = 21;
    final static int TANK_WIDTH = 17;
    final static int TANK_HEIGHT = 69;


    private ResourceLocation GUI = new ResourceLocation(Wrought.MODID, "textures/gui/h_c_gui.png");
    private ResourceLocation PROGRESS_BAR = new ResourceLocation(Wrought.MODID, "textures/gui/h_c_progress_bar.png");

    private HCCokeOvenContainer ovenContainer;

    public HCCokeOvenScreen(HCCokeOvenContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.ovenContainer = container;
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
    protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y) {

        // highlights the item the player is hovering over
        if (this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
            this.renderTooltip(matrixStack, this.hoveredSlot.getStack(), x, y);

        // detects when the player is hovering over the tank
        }  else if(x > xStart() + TANK_X_OFFSET && x < xStart() + TANK_X_OFFSET + TANK_WIDTH && y > yStart() + TANK_Y_OFFSET && y < yStart() + TANK_Y_OFFSET + TANK_HEIGHT)  {
            FluidStack fluidStack = getFluidStackInTank();
            TranslationTextComponent displayName = new TranslationTextComponent(fluidStack.getTranslationKey());
            TranslationTextComponent fluidAmount = new TranslationTextComponent(fluidStack.getAmount() + " / " + container.getTankMaxSize());
            renderTooltip(matrixStack, displayName, x, y+10);
            renderTooltip(matrixStack, fluidAmount, x, y+27);
            // debug
        }  else  {
            renderTooltip(matrixStack, new TranslationTextComponent("x = " + x + " y = " + y) , x, y);
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

        // Draws the main background
        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(stack, xStart(), yStart(), 0,0, this.xSize, this.ySize);

        // Draws the fluid tank
        drawFluid(stack, container.getFluid(), xStart() + TANK_X_OFFSET, yStart() + TANK_Y_OFFSET);
    }

    /*
        1. Draws a black background
        2. Draws a box that expands downwards the larger the processTime is.
            The main gui has an arrow cutout that will go over thi process box and give the appearnce of an arrow.
     */
    protected void drawProgressBar(MatrixStack stack)  {
        // draw a background for where the progress bar will not be
        this.fill(stack, xStart() + COOK_BAR_X_OFFSET, yStart() + COOK_BAR_Y_OFFSET, COOK_BAR_ICON_U, COOK_BAR_ICON_V, RenderHelper.convertARGBToInt(255,255,255,1));

        // get texture for the progress bar
        this.minecraft.getTextureManager().bindTexture(PROGRESS_BAR);

        // gets the value from 0 to 1 of how much progress the cooking item has
        double processTime = container.getProgress();

        // draw on screen
        this.blit(stack, xStart() + COOK_BAR_X_OFFSET, yStart() + COOK_BAR_Y_OFFSET, COOK_BAR_ICON_U, COOK_BAR_ICON_V,
                COOK_BAR_WIDTH, (int) (processTime * COOK_BAR_HEIGHT));
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

    public void drawFluid(MatrixStack matrixStack, FluidStack fluidStack, int x, int y)  {
        if(fluidStack == null || fluidStack.isEmpty())  {
            return;
        }
        matrixStack.push();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("textures/atlas/blocks.png"));
        int color = fluidStack.getFluid().getAttributes().getColor(fluidStack);
        setGLColorFromInt(color);
        drawTiledTexture(x, y+TANK_HEIGHT, getTexture(fluidStack.getFluid().getAttributes().getStillTexture(fluidStack)), TANK_WIDTH, (int)(TANK_HEIGHT * container.getPercentageInTank()), fluidStack.getAmount() / 1000);

        matrixStack.pop();
    }


    public FluidStack getFluidStackInTank()  {
        return container.getFluid();
    }

    public void drawTiledTexture(int x, int y, TextureAtlasSprite icon, int width, int height, int numBuckets) {
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

    public void drawScaledTexturedModelRectFromIcon(int x, int y, TextureAtlasSprite icon, int width, int height) {
        if ( icon == null ) {
            return;
        }
        float minU = icon.getMinU();
        float maxU = icon.getMaxU();
        float minV = icon.getMinV();
        float maxV = icon.getMaxV();

        float zLevel = 1.0f;

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
