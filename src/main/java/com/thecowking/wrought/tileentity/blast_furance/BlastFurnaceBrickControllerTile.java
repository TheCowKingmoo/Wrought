package com.thecowking.wrought.tileentity.blast_furance;

import com.thecowking.wrought.data.BlastFurnaceData;
import com.thecowking.wrought.inventory.containers.blast_furnace.BlastFurnaceContainerMultiblock;
import com.thecowking.wrought.inventory.containers.honey_comb_coke_oven.HCCokeOvenContainerMultiblock;
import com.thecowking.wrought.inventory.slots.*;
import com.thecowking.wrought.recipes.HoneyCombCokeOven.HoneyCombCokeOvenRecipe;
import com.thecowking.wrought.tileentity.MultiBlockControllerTileFluid;
import com.thecowking.wrought.tileentity.WroughtMutliblock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.thecowking.wrought.init.RegistryHandler.BLAST_FURNACE_BRICK_CONTROLLER_TILE;


public class BlastFurnaceBrickControllerTile extends MultiBlockControllerTileFluid implements INamedContainerProvider, WroughtMutliblock {

    private static final int NUMBER_INTERNAL_TANKS = 3;
    private static final int NUMBER_ITEM_INPUT_SLOTS = 3;
    private static final int NUMBER_ITEM_OUTPUT_SLOTS = 3;

    private static int DEFAULT_TANK_SIZE = 16000;
    private static boolean NEEDS_FUEL = true;


    public BlastFurnaceBrickControllerTile() {
        super(BLAST_FURNACE_BRICK_CONTROLLER_TILE.get(), NUMBER_ITEM_INPUT_SLOTS, NUMBER_ITEM_OUTPUT_SLOTS, NEEDS_FUEL, new BlastFurnaceData(), NUMBER_INTERNAL_TANKS, DEFAULT_TANK_SIZE);
        this.status = "Standing By";

        this.everything = LazyOptional.of(() -> new CombinedInvWrapper(this.inputSlots, this.outputSlots, this.fuelInputSlot, this.fluidItemInputSlots, this.fluidItemOutputSlots));
        this.automation = LazyOptional.of(() -> new AutomationCombinedInvWrapper(this.inputSlots, this.outputSlots, this.fuelInputSlot, this.fluidItemInputSlots, this.fluidItemOutputSlots));
    }

    @Nullable
    @Override
    public Container createMenu(final int windowID, final PlayerInventory playerInv, final PlayerEntity playerIn) {
        return new BlastFurnaceContainerMultiblock(windowID, this.world, getControllerPos(), playerInv);
    }

    /*
        Name that is displayed on the GUI
     */
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("Blast Furnace Controller");
    }


}
