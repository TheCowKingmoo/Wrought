package com.thecowking.wrought.fluids;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.util.WroughtFluidUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class MoltenMetalFluid  {
    public String id;
    private int colour;
    private Material material;



    public MoltenMetalFluid(String id, Material material, Block ingotBlock)  {
        this.id = id;
        this.colour = ingotBlock.getMaterialColor().colorValue;
        this.material = material;
        createFluid();
    }


    public MoltenMetalFluid(String id, Material material, int colour)  {
        this.id = id;
        this.colour = colour;
        this.material = material;
        createFluid();
    }

    private void createFluid()  {
        WroughtFluidUtil.createFluidBlock(this.id, this.material, this.colour, 1200, 1200, new ResourceLocation(Wrought.MODID, "blocks/molten_still"), new ResourceLocation(Wrought.MODID,"blocks/molten_flow"), new ResourceLocation(Wrought.MODID,"blocks/molten_overlay"));
    }

}
