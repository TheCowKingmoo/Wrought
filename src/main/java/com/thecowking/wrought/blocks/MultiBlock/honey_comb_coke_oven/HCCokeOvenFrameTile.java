package com.thecowking.wrought.blocks.MultiBlock.honey_comb_coke_oven;

import com.thecowking.wrought.blocks.MultiBlock.MultiBlockFrameTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static com.thecowking.wrought.util.RegistryHandler.H_C_COKE_FRAME_TILE;


public class HCCokeOvenFrameTile extends MultiBlockFrameTile {
    private static final Logger LOGGER = LogManager.getLogger();

    public HCCokeOvenFrameTile() {
        super(H_C_COKE_FRAME_TILE.get());
        LOGGER.info("FRAME TILE MADE");
    }

    public HCCokeOvenControllerTile getControllerTile()  {
        if(frameGetControllerPos() != null) {
            TileEntity te = this.world.getTileEntity(frameGetControllerPos());
            if (te instanceof HCCokeOvenControllerTile) {
                HCCokeOvenControllerTile controllerTile = (HCCokeOvenControllerTile) te;
                return controllerTile;
            }
        }
        return null;
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        HCCokeOvenControllerTile controllerTile = getControllerTile();
        if(controllerTile != null)  {
                return controllerTile.getCapability(cap, Direction.WEST);
        }
        return super.getCapability(cap, side);
    }



}
