package com.thecowking.wrought.tileentity.casting_machine;

import com.thecowking.wrought.data.CastingMachineData;
import com.thecowking.wrought.data.HCCokeOvenData;
import com.thecowking.wrought.inventory.containers.casting_machine.CastingMachineContainerMultiblock;
import com.thecowking.wrought.inventory.containers.honey_comb_coke_oven.HCCokeOvenContainerMultiblock;
import com.thecowking.wrought.inventory.slots.AutomationCombinedInvWrapper;
import com.thecowking.wrought.tileentity.MultiBlockControllerTileFluid;
import com.thecowking.wrought.tileentity.WroughtMutliblock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static com.thecowking.wrought.init.RegistryHandler.CASTING_MACHINE_CONTROLLER_TILE;

public class CastingMachineControllerTile extends MultiBlockControllerTileFluid implements INamedContainerProvider, WroughtMutliblock {

    public CastingMachineControllerTile() {
        super(CASTING_MACHINE_CONTROLLER_TILE.get(), 0, 1, false, new CastingMachineData(), 0, 1, 16000);
        this.status = "Standing By";
        this.everything = LazyOptional.of(() -> new CombinedInvWrapper(this.inputSlots, this.outputSlots, this.fluidItemInputSlots, this.fluidItemOutputSlots));
        this.automation = LazyOptional.of(() -> new AutomationCombinedInvWrapper(this.inputSlots, this.outputSlots, this.fluidItemInputSlots, this.fluidItemOutputSlots));
    }

    @Nullable
    @Override
    public Container createMenu(final int windowID, final PlayerInventory playerInv, final PlayerEntity playerIn) {
        return new CastingMachineContainerMultiblock(windowID, this.world, getControllerPos(), playerInv);
    }

    /*
        Name that is displayed on the GUI
     */
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("Casting Machine");
    }




}
