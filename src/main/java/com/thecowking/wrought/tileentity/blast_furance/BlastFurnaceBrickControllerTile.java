package com.thecowking.wrought.tileentity.blast_furance;

import com.thecowking.wrought.data.BlastFurnaceData;
import com.thecowking.wrought.data.IMultiblockData;
import com.thecowking.wrought.inventory.containers.honey_comb_coke_oven.HCCokeOvenContainer;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import static com.thecowking.wrought.util.RegistryHandler.BLAST_FURANCE_BRICK_CONTROLLER_TILE;

public class BlastFurnaceBrickControllerTile extends MultiBlockControllerTile {
    public BlastFurnaceBrickControllerTile() {
        super(BLAST_FURANCE_BRICK_CONTROLLER_TILE.get(), new BlastFurnaceData());
    }
}
