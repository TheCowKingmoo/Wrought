package com.thecowking.wrought.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.text.ITextComponent;

public class HCCokeOvenScreen extends Screen {
    private int left = 0, top = 0;
    private final int xSize = 196;
    private final int ySize = 128;



    protected HCCokeOvenScreen(ITextComponent titleIn) {
        super(titleIn);
        this.left = (this.width - this.xSize) / 2;
        this.top = (this.height - this.ySize) / 2;

        BuildButton buildButton = new BuildButton(this.left, this.top,(button) -> {
            ///this.lockFluidButton.toggleState();

            //this.mainValve.setFluidLock(this.lockFluidButton.getState());
            //this.lockFluidButton.setState(this.mainValve.getTankConfig().isFluidLocked());
            //NetworkHandler.sendPacketToServer(new FFSPacket.Server.UpdateFluidLock(this.mainValve));
        }
            );

        this.addButton(buildButton);
    }

    @Override
    public void render(MatrixStack stack, int x, int y, float partialTicks)  {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderBackground(stack);
        super.render(stack, x, y, partialTicks);
    }




}
