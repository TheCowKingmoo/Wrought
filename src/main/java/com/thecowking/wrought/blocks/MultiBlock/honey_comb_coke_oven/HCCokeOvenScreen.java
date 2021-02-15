package com.thecowking.wrought.blocks.MultiBlock.honey_comb_coke_oven;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thecowking.wrought.Wrought;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
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
    final static int COOK_BAR_XPOS = 49;
    final static  int COOK_BAR_YPOS = 60;
    final static  int COOK_BAR_ICON_U = 0;   // texture position of white arrow icon [u,v]
    final static  int COOK_BAR_ICON_V = 207;
    final static  int COOK_BAR_WIDTH = 80;
    final static  int COOK_BAR_HEIGHT = 17;
    private static final Logger LOGGER = LogManager.getLogger();

    final static int TANK_X_OFFSET = 0;
    final static int TANK_Y_OFFSET = 0;
    final static int TANK_WIDTH = 20;
    final static int TANK_HEIGHT = 80;



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
            TranslationTextComponent text = new TranslationTextComponent(fluidStack.getDisplayName().getString());
            text.appendString(fluidStack.getAmount() + " / " + container.getTankMaxSize());
            renderTooltip(matrixStack, text, x, y+10);

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




    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)  {
        int xStart = (this.width - this.xSize) / 2;
        int yStart = (this.height - this.ySize) / 2;

        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(stack, xStart, yStart, 0,0, this.xSize, this.ySize);

        double processTime = container.getProgress();
        this.minecraft.getTextureManager().bindTexture(PROGRESS_BAR);
        this.blit(stack, xStart + COOK_BAR_XPOS, yStart + COOK_BAR_YPOS, COOK_BAR_ICON_U, COOK_BAR_ICON_V,
                (int) (processTime * COOK_BAR_WIDTH), COOK_BAR_HEIGHT);

        drawFluid(stack, container.getFluid(), xStart() + TANK_X_OFFSET, yStart() + TANK_Y_OFFSET);
    }


    protected ITextComponent getName() {
        return new TranslationTextComponent("multi_block.wrought.coke_oven");
    }

    protected void drawForgegroundString(MatrixStack matrixStack) {
        font.func_243248_b(matrixStack, getName(), 28 + 0, 4 + 0, 4210752);
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
        drawTiledTexture(x, y, getTexture(fluidStack.getFluid().getAttributes().getStillTexture(fluidStack)), TANK_WIDTH, TANK_HEIGHT);

        matrixStack.pop();
    }


    public FluidStack getFluidStackInTank()  {
        return container.getFluid();
    }

    public void drawTiledTexture(int x, int y, TextureAtlasSprite icon, int width, int height) {
        int i;
        int j;

        int drawHeight;
        int drawWidth;

        for (i = 0; i < width; i += 16) {
            for (j = 0; j < height; j += 16) {
                drawWidth = Math.min(width - i, 16);
                drawHeight = Math.min(height - j, 16);
                drawScaledTexturedModelRectFromIcon(x + i, y + j, icon, drawWidth, drawHeight);
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
        buffer.pos(x, y + height, zLevel).tex(minU, minV + (maxV - minV) * height / 16F).endVertex();
        buffer.pos(x + width, y + height, zLevel).tex(minU + (maxU - minU) * width / 16F, minV + (maxV - minV) * height / 16F).endVertex();
        buffer.pos(x + width, y, zLevel).tex(minU + (maxU - minU) * width / 16F, minV).endVertex();
        buffer.pos(x, y, zLevel).tex(minU, minV).endVertex();
        Tessellator.getInstance().draw();
    }


    public static void setGLColorFromInt(int color) {
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        GlStateManager.color4f(red, green, blue, 1.0F);
    }


}
