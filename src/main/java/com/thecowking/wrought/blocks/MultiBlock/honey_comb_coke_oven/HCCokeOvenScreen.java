package com.thecowking.wrought.blocks.MultiBlock.honey_comb_coke_oven;

import com.mojang.blaze3d.matrix.MatrixStack;

import com.thecowking.wrought.Wrought;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;


public class HCCokeOvenScreen extends ContainerScreen<HCCokeOvenContainer> {
    // TODO - screen
    private ResourceLocation GUI = new ResourceLocation(Wrought.MODID, "textures/gui/h_c_gui.png");

    public HCCokeOvenScreen(HCCokeOvenContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
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

    }




}
