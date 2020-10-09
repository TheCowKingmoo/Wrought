package com.thecowking.wrought.blocks;


import com.thecowking.wrought.blocks.honeycomb_coke_oven.HCCokeOvenControllerTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import org.jetbrains.annotations.Nullable;

public class MultiBlockControllerBlock extends Block implements IMultiBlockControllerBlock {


    public MultiBlockControllerBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2.0f)
                .harvestTool(ToolType.PICKAXE)
        );
        setDefaultState(this.getDefaultState().with(Multiblock.FORMED, false).with(Multiblock.LIT, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(Multiblock.FORMED, Multiblock.LIT);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

   // @Override
    // public int getLightValue  {
    //    return state.get(LIT) ? super.getLightValue() : 0;
    //}

    @Override
    public boolean hasComparatorInputOverride(BlockState state)  {
        return true;
    }
    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)  {
        return Container.calcRedstone(Multiblock.getTileFromPos(worldIn, pos));
    }









}

