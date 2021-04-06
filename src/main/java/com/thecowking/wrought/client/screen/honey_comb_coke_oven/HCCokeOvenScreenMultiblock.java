package com.thecowking.wrought.client.screen.honey_comb_coke_oven;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.client.screen.MultiBlockFluidScreen;
import com.thecowking.wrought.client.screen.MultiblockScreen;
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

import static com.thecowking.wrought.data.BlastFurnaceData.X_SIZE;


public class HCCokeOvenScreenMultiblock extends MultiBlockFluidScreen<HCCokeOvenContainerMultiblock> {
    final static int COOK_BAR_X_OFFSET = 14;
    final static  int COOK_BAR_Y_OFFSET = 40;
    final static  int COOK_BAR_ICON_U = 0;   // texture position of white arrow icon [u,v]
    final static  int COOK_BAR_ICON_V = 207;
    final static  int COOK_BAR_WIDTH = 17;
    final static  int COOK_BAR_HEIGHT = 30;
    private static final Logger LOGGER = LogManager.getLogger();

    final static int TANK_X_OFFSET = 129;
    final static int TANK_Y_OFFSET = 19;
    final static int TANK_WIDTH = 17;
    final static int TANK_HEIGHT = 74;

    final static int TANK_INDEX = 0;


    private ResourceLocation GUI = new ResourceLocation(Wrought.MODID, "textures/gui/h_c_gui.png");

    private HCCokeOvenContainerMultiblock multiBlockContainer;

    public HCCokeOvenScreenMultiblock(HCCokeOvenContainerMultiblock container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.multiBlockContainer = container;
        this.xSize = 176;
        this.ySize = 240;
        this.indicatorXOffset = 39;
        this.indicatorYOffset = 48;

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

        // highlights the item the player is hovering over
        if (this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
            this.renderTooltip(stack, this.hoveredSlot.getStack(), x, y);

            // detects when the player is hovering over the tank
        }  else if(x > xStart() + TANK_X_OFFSET && x < xStart() + TANK_X_OFFSET + TANK_WIDTH && y > yStart() + TANK_Y_OFFSET && y < yStart() + TANK_Y_OFFSET + TANK_HEIGHT)  {
            FluidStack fluidStack = getFluidInTank(multiBlockContainer, 0);
            TranslationTextComponent displayName = new TranslationTextComponent(fluidStack.getTranslationKey());
            TranslationTextComponent fluidAmount = new TranslationTextComponent(fluidStack.getAmount() + " / " + getTanksMaxSize(multiBlockContainer,0));
            renderTooltip(stack, displayName, x, y+10);
            renderTooltip(stack, fluidAmount, x, y+27);
            // debug
        }  else  {
            renderTooltip(stack, new TranslationTextComponent("x = " + x + " y = " + y) , x, y);
        }
    }

    /*
        Does as the name suggests -> draws the main background to the gui
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)  {

        // progress bar exists behind the main background
        double cookingPercent = multiBlockContainer.getProgress();
        createProgressBar(stack, this.minecraft.getTextureManager(), xStart() + COOK_BAR_X_OFFSET, yStart() + COOK_BAR_Y_OFFSET, COOK_BAR_WIDTH, COOK_BAR_HEIGHT, cookingPercent);


        double firstTankPercent = multiBlockContainerFluid.getTankPercentFull(0);
        int x = xStart() + TANK_X_OFFSET;
        int y = yStart() + TANK_Y_OFFSET;
        createTankBackGround(stack, x, y, DEFAULT_TANK_BACKGROUND, this.minecraft.getTextureManager(), TANK_WIDTH, TANK_HEIGHT, TANK_WIDTH, TANK_HEIGHT);
        RenderHelper.drawFluid(stack, getFluidInTank(multiBlockContainerFluid, 0), x, y, TANK_WIDTH, TANK_HEIGHT, multiBlockContainerFluid, firstTankPercent);
        createTankBackGround(stack, x, y, DEFAULT_TANK_GAUGE, this.minecraft.getTextureManager(), TANK_WIDTH, TANK_HEIGHT, TANK_WIDTH, TANK_HEIGHT);

        //draw indicator before background
        drawStatusIndicator(stack);

        // Draws the main background
        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(stack, xStart(), yStart(), 0,0, this.xSize, this.ySize);

    }

    protected ITextComponent getName() {
        return new TranslationTextComponent("Honey Comb Coke Oven");
    }




}