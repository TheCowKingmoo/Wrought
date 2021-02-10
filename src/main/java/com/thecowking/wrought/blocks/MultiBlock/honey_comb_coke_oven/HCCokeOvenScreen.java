package com.thecowking.wrought.blocks.MultiBlock.honey_comb_coke_oven;

import com.mojang.blaze3d.matrix.MatrixStack;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.util.RenderHelper;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HCCokeOvenScreen extends ContainerScreen<HCCokeOvenContainer> {
    final static int COOK_BAR_XPOS = 49;
    final static  int COOK_BAR_YPOS = 60;
    final static  int COOK_BAR_ICON_U = 0;   // texture position of white arrow icon [u,v]
    final static  int COOK_BAR_ICON_V = 207;
    final static  int COOK_BAR_WIDTH = 80;
    final static  int COOK_BAR_HEIGHT = 17;
    private static final Logger LOGGER = LogManager.getLogger();



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
        this.renderBackground(stack);
        super.render(stack, x, y, partialTicks);
        this.renderHoveredTooltip(stack, x, y);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)  {
        int xStart = (this.width - this.xSize) / 2;
        int yStart = (this.height - this.ySize) / 2;


        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(stack, xStart, yStart, 0,0, this.xSize, this.ySize);

        double processTime = container.getProgress();
        this.minecraft.getTextureManager().bindTexture(PROGRESS_BAR);
        //LOGGER.info("progress = " + processTime);
        this.blit(stack, xStart + COOK_BAR_XPOS, yStart + COOK_BAR_YPOS, COOK_BAR_ICON_U, COOK_BAR_ICON_V,
                (int) (processTime * COOK_BAR_WIDTH), COOK_BAR_HEIGHT);

        TextureAtlasSprite fluidTexture = RenderHelper.getFluidTexture(container.getFluid());
        //LOGGER.info(container.getFluid().getDisplayName());
        this.blit(stack, xStart, yStart, COOK_BAR_ICON_U, COOK_BAR_ICON_V, COOK_BAR_HEIGHT, fluidTexture);


        //blit(stack, xStart, yStart, 0, 0, 64, 64);
        //blit(stack, 10, 10, 10, 0F, 0F, 64, 64, 32, 32);

    }

    protected ITextComponent getName() {
        return new TranslationTextComponent("multi_block.wrought.coke_oven");
    }

    protected void drawForgegroundString(MatrixStack matrixStack) {
        font.func_243248_b(matrixStack, getName(), 28 + 0, 4 + 0, 4210752);
    }

}
