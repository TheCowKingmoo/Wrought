package com.thecowking.wrought.client.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
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




    public BuildButton(int x, int y, int width, int height, BlockPos controllerPos) {
        super(x, y, width, height,
                new StringTextComponent("Auto Build"), new BuildClick(controllerPos));
        this.controllerPos = controllerPos;
    }

    public void setShowButton(boolean b)  {this.showButton = b;}



    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if(!this.showButton)  return;
        this.fillGradient(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height, 0xFFFF0000, 0xFF0000FF);
        drawCenteredString(matrixStack, fontrenderer, this.getMessage(), this.x + this.width / 2,
                this.y + (this.height - 8) / 2, this.getFGColor() | MathHelper.ceil(this.alpha * 255.0F) << 24);
        if (this.isHovered()) {
            this.renderToolTip(matrixStack, mouseX, mouseY);
        }
    }

}
