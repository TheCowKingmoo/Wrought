package com.thecowking.wrought.client.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thecowking.wrought.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BuildButton extends Button {
    private static final Logger LOGGER = LogManager.getLogger();
    public BlockPos controllerPos;
    FontRenderer fontrenderer =  Minecraft.getInstance().fontRenderer;
    private boolean showButton = false;
    private Screen screen;




    public BuildButton(int x, int y, int width, int height, BlockPos controllerPos, Screen screen) {
        super(x, y, width, height,
                new StringTextComponent("Auto Build"), new BuildClick(controllerPos));
        this.controllerPos = controllerPos;
        this.screen = screen;
    }

    public void setShowButton(boolean b)  {this.showButton = b;}



    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if(!this.showButton)  return;
        int color = RenderHelper.convertARGBToInt(255, 255, 0, 1);
        this.fillGradient(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height, color, color);
        drawCenteredString(matrixStack, fontrenderer, this.getMessage(), this.x + this.width / 2,
                this.y + (this.height - 8) / 2, this.getFGColor() | MathHelper.ceil(this.alpha * 255.0F) << 24);
        if (this.isHovered()) {
            this.renderToolTip(matrixStack, mouseX, mouseY);
        }
    }

}
