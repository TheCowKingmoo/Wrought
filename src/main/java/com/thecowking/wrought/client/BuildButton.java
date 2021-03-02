package com.thecowking.wrought.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thecowking.wrought.Wrought;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BuildButton extends Button {
    private static final Logger LOGGER = LogManager.getLogger();
    FontRenderer fontrenderer =  Minecraft.getInstance().fontRenderer;



    public BuildButton(int x, int y, int width, int height) {

        super(x, y, width, height,
                new StringTextComponent("Auto Build"), btn -> LOGGER.info("Hello clicked!"));
    }



    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.fillGradient(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height, 0xFFFF0000, 0xFF0000FF);
        drawCenteredString(matrixStack, fontrenderer, this.getMessage(), this.x + this.width / 2,
                this.y + (this.height - 8) / 2, this.getFGColor() | MathHelper.ceil(this.alpha * 255.0F) << 24);
        if (this.isHovered()) {
            this.renderToolTip(matrixStack, mouseX, mouseY);
        }
    }

    public void buttonClick()  {
        World world = Minecraft.getInstance().world;
    }
}
