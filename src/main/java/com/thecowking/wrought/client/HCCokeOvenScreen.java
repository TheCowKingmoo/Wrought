package com.thecowking.wrought.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.inventory.containers.HCCokeOvenContainer;
import com.thecowking.wrought.inventory.containers.HCCokeOvenContainerMultiblock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HCCokeOvenScreen extends ContainerScreen<HCCokeOvenContainer> {
    private int left = 0, top = 0;
    private int xSize;
    private int ySize;
    private BuildButton buildButton;

    private ResourceLocation GUI = new ResourceLocation(Wrought.MODID, "textures/gui/h_c_gui_build.png");
    private static final Logger LOGGER = LogManager.getLogger();


    public HCCokeOvenScreen(HCCokeOvenContainer container, PlayerInventory inv, ITextComponent name)  {
        super(container, inv, name);
        this.xSize = 176;
        this.ySize = 240;
        this.left = (this.width - this.xSize) / 2;
        this.top = (this.height - this.ySize) / 2;

    }

    @Override
    protected void init()  {
        super.init();
        this.addButton(new BuildButton(this.left + 100, this.top + 100, 100, 100));
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
        //this.blit(stack, xStart(), yStart(), 0,0, this.xSize, this.ySize);
        //buildButton.render(stack);


    }
    public int xStart() {
        return (this.width - this.xSize) / 2;
    }

    public int yStart() {
        return (this.height - this.ySize) / 2;
    }


}
