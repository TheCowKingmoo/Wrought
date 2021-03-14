package com.thecowking.wrought.tileentity.honey_comb_coke_oven;

import com.thecowking.wrought.data.HCCokeOvenData;
import com.thecowking.wrought.init.RecipeSerializerInit;
import com.thecowking.wrought.inventory.containers.honey_comb_coke_oven.HCCokeOvenContainerMultiblock;
import com.thecowking.wrought.data.MultiblockData;
import com.thecowking.wrought.inventory.containers.OutputFluidTank;
import com.thecowking.wrought.inventory.slots.*;
import com.thecowking.wrought.recipes.HoneyCombCokeOven.HoneyCombCokeOvenRecipe;
import com.thecowking.wrought.tileentity.MultiBlockControllerTileFluid;
import com.thecowking.wrought.tileentity.WroughtMutliblock;
import com.thecowking.wrought.util.*;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import static com.thecowking.wrought.data.MultiblockData.*;
import static com.thecowking.wrought.init.RegistryHandler.*;

public class HCCokeOvenControllerTile extends MultiBlockControllerTileFluid implements INamedContainerProvider, WroughtMutliblock {
    private static final Logger LOGGER = LogManager.getLogger();


    public HCCokeOvenControllerTile() {
        super(H_C_COKE_CONTROLLER_TILE.get(), 1, 2, false, new HCCokeOvenData(), 1, 16000);
        this.status = "Standing By";


        this.everything = LazyOptional.of(() -> new CombinedInvWrapper(this.inputSlots, this.outputSlots, this.fluidItemInputSlots, this.fluidItemOutputSlots));
        this.automation = LazyOptional.of(() -> new AutomationCombinedInvWrapper(this.inputSlots, this.outputSlots, this.fluidItemInputSlots, this.fluidItemOutputSlots));

    }

    @Nullable
    @Override
    public Container createMenu(final int windowID, final PlayerInventory playerInv, final PlayerEntity playerIn) {
        return new HCCokeOvenContainerMultiblock(windowID, this.world, getControllerPos(), playerInv);
    }

    /*
        Name that is displayed on the GUI
     */
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("Coke Oven Controller");
    }




}
