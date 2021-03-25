package com.thecowking.wrought.init;

import com.thecowking.wrought.Wrought;
import com.thecowking.wrought.fluids.MoltenMetalFluid;
import com.thecowking.wrought.util.RenderHelper;
import com.thecowking.wrought.util.WroughtFluidUtil;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FluidInit {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void initFluids()  {

        // Creosote
        WroughtFluidUtil.createFluidBlock("creosote", Material.WATER, RenderHelper.convertARGBToInt(255,255,61, 0.5), 1200, 1200, new ResourceLocation(Wrought.MODID, "blocks/creosote_still"), new ResourceLocation(Wrought.MODID,"blocks/creosote_flowing"), new ResourceLocation(Wrought.MODID,"blocks/creosote_overlay"));


        // Molten Fluids - this constructor will get the color from the source block
        new MoltenMetalFluid("molten_iron", Material.LAVA, Blocks.IRON_BLOCK);
        new MoltenMetalFluid("molten_gold", Material.LAVA, Blocks.GOLD_BLOCK);

        // TODO - better cap for modded ingots
        // Molten Fluids -  provide the color manually
        new MoltenMetalFluid("molten_copper", Material.LAVA, RenderHelper.convertARGBToInt(255,0, 0,0.5));
        new MoltenMetalFluid("molten_lead", Material.LAVA, RenderHelper.convertARGBToInt(255,128, 0,0.5));
        new MoltenMetalFluid("molten_osmium", Material.LAVA, RenderHelper.convertARGBToInt(57,167, 206,0.5));
        new MoltenMetalFluid("molten_silver", Material.LAVA, RenderHelper.convertARGBToInt(224,224, 224,0.5));
        new MoltenMetalFluid("molten_tin", Material.LAVA, RenderHelper.convertARGBToInt(160,160, 160,0.5));

    }

}
