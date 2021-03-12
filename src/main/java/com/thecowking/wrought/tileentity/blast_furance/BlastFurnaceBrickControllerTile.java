package com.thecowking.wrought.tileentity.blast_furance;

import com.thecowking.wrought.data.BlastFurnaceData;
import com.thecowking.wrought.inventory.slots.*;
import com.thecowking.wrought.recipes.HoneyCombCokeOven.HoneyCombCokeOvenRecipe;
import com.thecowking.wrought.tileentity.MultiBlockControllerTileFluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.thecowking.wrought.init.RegistryHandler.BLAST_FURNACE_BRICK_CONTROLLER_TILE;


public class BlastFurnaceBrickControllerTile extends MultiBlockControllerTileFluid {

    private static int NUMBER_INTERNAL_TANKS = 3;
    private static int NUMBER_INPUTS_OUTPUTS_SLOTS = 3;
    private static int DEFAULT_TANK_SIZE = 16000;


    public BlastFurnaceBrickControllerTile() {
        super(BLAST_FURNACE_BRICK_CONTROLLER_TILE.get(), 3, 3, true, new BlastFurnaceData(), NUMBER_INTERNAL_TANKS, DEFAULT_TANK_SIZE);
    }



    @Override
    public void tick() {
        super.tick();
        this.status = "Blast Furnace";
        attemptRunOperation();
    }




}
