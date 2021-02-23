package com.thecowking.wrought.client;

import com.thecowking.wrought.Wrought;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;

public class BuildButton extends ImageButton {
    private static final ResourceLocation texture = new ResourceLocation(Wrought.MODID, "textures/gui/gui_tank_no_valve.png");

    BuildButton(int x, int y, IPressable onPress)  {
        super(x,y, 8, 8, 0, 128, 0, texture, onPress);

    }
}
