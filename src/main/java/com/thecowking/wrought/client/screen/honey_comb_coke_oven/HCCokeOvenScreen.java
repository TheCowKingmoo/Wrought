package com.thecowking.wrought.client.screen.honey_comb_coke_oven;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.data.HCCokeOvenData;
import com.thecowking.wrought.client.button.BuildButton;
import com.thecowking.wrought.inventory.containers.honey_comb_coke_oven.HCCokeOvenContainer;
import com.thecowking.wrought.util.InventoryUtils;
import com.thecowking.wrought.util.MultiBlockHelper;
import com.thecowking.wrought.util.RenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class HCCokeOvenScreen extends ContainerScreen<HCCokeOvenContainer> {
    private HCCokeOvenContainer container;
    private BuildButton buildButton;
    private final int BUILD_BUTTON_HEIGHT = 25;
    private final int BUILD_BUTTON_WIDTH = 50;

    private final int MESSAGE_Y_SEPERATION = 10;
    private final int X_BORDER = 15;

    private ResourceLocation GUI = new ResourceLocation(Wrought.MODID, "textures/gui/h_c_gui_build.png");
    private static final Logger LOGGER = LogManager.getLogger();
    private HashMap<Block, Integer> missingMembers;
    private HashMap<Block, Integer> inInvetory;



    public HCCokeOvenScreen(HCCokeOvenContainer container, PlayerInventory inv, ITextComponent name)  {
        super(container, inv, name);
        this.xSize = 176;
        this.ySize = 240;
        this.container = container;
        this.missingMembers = MultiBlockHelper.getMissingBlocks(Minecraft.getInstance().world, this.container.controllerPos, new HCCokeOvenData());
        this.inInvetory = InventoryUtils.checkVsPlayerInventory(missingMembers, inv.player);
    }

    @Override
    protected void init()  {
        super.init();
        this.buildButton = new BuildButton(this.width / 2 - BUILD_BUTTON_WIDTH / 2 , this.ySize / 2 - BUILD_BUTTON_HEIGHT, BUILD_BUTTON_WIDTH, BUILD_BUTTON_HEIGHT, this.container.controllerPos, this);
        addButton(this.buildButton);
        if(missingMembers == null)  {this.missingMembers = MultiBlockHelper.getMissingBlocks(Minecraft.getInstance().world, this.container.controllerPos, new HCCokeOvenData());}

    }



    @Override
    public void render(MatrixStack stack, int x, int y, float partialTicks)  {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderBackground(stack);
        super.render(stack, x, y, partialTicks);
        this.renderHoveredTooltip(stack, x, y);

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

    public void displayMissingBlocks(MatrixStack stack, int startMessageX, int startMessageY)  {
        int green = RenderHelper.convertARGBToInt(0, 255, 0, 1);
        int red = RenderHelper.convertARGBToInt(255, 0, 0, 1);
        FontRenderer fontrenderer =  Minecraft.getInstance().fontRenderer;
        if(missingMembers.size() == 0)  {
            drawString(stack, fontrenderer, "All Blocks Are Correct", startMessageX, startMessageY, green);

            buildButton.setShowButton(true);

            return;
        }





        drawString(stack, fontrenderer, "Block(s) To Build", startMessageX,
                startMessageY, RenderHelper.convertARGBToInt(255, 0, 0, 1));

        boolean goodToGo = true;
        for(Map.Entry<Block, Integer> e: missingMembers.entrySet())  {
            Integer integerNumInv = inInvetory.get(e.getKey());
            int numInv = 0;
            if (integerNumInv != null)  {
                numInv = integerNumInv.intValue();
            }
            int numNeeded = e.getValue();
            int color = red;


            if(numInv >= numNeeded)  {
                color = green;
            } else  {
                goodToGo = false;
            }
            // write the number of needed blocks to place in world
            startMessageY = startMessageY + MESSAGE_Y_SEPERATION;
            drawString(stack, fontrenderer, e.getKey().getTranslationKey() + " x " + e.getValue(), startMessageX + 4,
                    startMessageY, color);
        }

        buildButton.setShowButton(goodToGo);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int x, int y) {
        this.font.func_243248_b(stack, this.title, (float)this.titleX, (float)this.titleY, 4210752);
        displayMissingBlocks(stack, X_BORDER, this.titleY + X_BORDER);
    }




}
