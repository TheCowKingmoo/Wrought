package com.thecowking.wrought.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.inventory.containers.HCCokeOvenContainer;
import com.thecowking.wrought.inventory.containers.HCCokeOvenContainerMultiblock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class HCCokeOvenScreen extends ContainerScreen<HCCokeOvenContainer> {
    private int left = 0, top = 0;
    private int xSize;
    private int ySize;

    private ResourceLocation GUI = new ResourceLocation(Wrought.MODID, "textures/gui/h_c_gui_build.png");

    public HCCokeOvenScreen(HCCokeOvenContainer container, PlayerInventory inv, ITextComponent name)  {
        super(container, inv, name);
        this.xSize = 176;
        this.ySize = 240;
        this.left = (this.width - this.xSize) / 2;
        this.top = (this.height - this.ySize) / 2;

        BuildButton buildButton = new BuildButton(this.left, this.top,(button) -> {
        }
            );

    }

    @Override
    public void render(MatrixStack stack, int x, int y, float partialTicks)  {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderBackground(stack);
        super.render(stack, x, y, partialTicks);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY)  {

        // Draws the main background
        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(stack, xStart(), yStart(), 0,0, this.xSize, this.ySize);

    }
    public int xStart() {
        return (this.width - this.xSize) / 2;
    }

    public int yStart() {
        return (this.height - this.ySize) / 2;
    }


}
