package com.thecowking.wrought.tileentity.blast_furance;

import com.thecowking.wrought.blocks.blast_furance.BlastFurnaceData;
import com.thecowking.wrought.inventory.containers.HCCokeOvenContainer;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntityType;
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



    public void openGUI(World worldIn, BlockPos pos, PlayerEntity player, BlastFurnaceBrickControllerTile tileEntity) {
        INamedContainerProvider containerProvider = new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("Honey Comb Coke Oven Controller");
            }

            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new HCCokeOvenContainer(i, worldIn, getControllerPos(), playerInventory);
            }
        };
        NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, ((BlastFurnaceBrickControllerTile) tileEntity).getPos());
    }

}
