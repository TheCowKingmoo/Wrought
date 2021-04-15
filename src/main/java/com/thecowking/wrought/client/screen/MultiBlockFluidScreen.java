package com.thecowking.wrought.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.inventory.containers.MultiBlockContainer;
import com.thecowking.wrought.inventory.containers.MultiBlockContainerFluid;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;

public class MultiBlockFluidScreen <MULTICONTAINER extends MultiBlockContainerFluid> extends MultiblockScreen<MULTICONTAINER> {
    protected MultiBlockContainerFluid multiBlockContainerFluid;
    protected int[] tankXOffset;
    protected int[] tankYOffset;
    protected int numTanks;

    public ResourceLocation DEFAULT_TANK_BACKGROUND = new ResourceLocation(Wrought.MODID, "textures/gui/tank_frame.png");
    public ResourceLocation DEFAULT_TANK_GAUGE = new ResourceLocation(Wrought.MODID, "textures/gui/tank_gauge.png");

    public MultiBlockFluidScreen(MULTICONTAINER screenContainer, PlayerInventory inv, ITextComponent titleIn, int numTanks) {
        super(screenContainer, inv, titleIn);
        this.multiBlockContainerFluid = screenContainer;
        this.numTanks = numTanks;
        this.tankXOffset = new int[numTanks];
        this.tankYOffset = new int[numTanks];
    }

    public void createTankBackGround(MatrixStack stack, int x, int y, ResourceLocation tankBackground, TextureManager manager, int width, int height, int tankWidth, int tankHeight)  {
        manager.bindTexture(tankBackground);
        AbstractGui.blit(stack, x, y, 0, 0, width, height, tankWidth, tankHeight);
    }

    public FluidStack getFluidInTank(MultiBlockContainerFluid container, int index)  {
        return container.getFluidController().getFluidInTank(index);
    }
    public int getTanksMaxSize(MultiBlockContainerFluid container, int index)  {
        return container.getFluidController().getOutputTankMaxSize(index);
    }
    public int getFluidInTanksHeight(MultiBlockContainerFluid container, int tankHeight, int tankIndex)  {
        return (int)(tankHeight * container.getFluidController().getPercentageInTank(tankIndex));
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack stack, int x, int y) {
        // iterate thru every tank
        for(int i = 0; i < this.numTanks; i++)  {
            if(x > xStart() + tankXOffset[i] && x < xStart() + tankXOffset[i] + TANK_WIDTH && y > yStart() + tankYOffset[i] && y < yStart() + tankYOffset[i] + TANK_HEIGHT)  {
                FluidStack fluidStack = getFluidInTank(multiBlockContainerFluid, i);
                TranslationTextComponent displayName = new TranslationTextComponent(fluidStack.getTranslationKey());
                TranslationTextComponent fluidAmount = new TranslationTextComponent(fluidStack.getAmount() + " / " + getTanksMaxSize(multiBlockContainerFluid, i));
                renderTooltip(stack, new TranslationTextComponent(String.valueOf(i)), x, 37);
                renderTooltip(stack, displayName, x, y+10);
                renderTooltip(stack, fluidAmount, x, y+27);
                return;
            }
        }
        super.renderHoveredTooltip(stack, x, y);
    }



}
