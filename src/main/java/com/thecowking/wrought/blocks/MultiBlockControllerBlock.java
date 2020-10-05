package com.thecowking.wrought.blocks;


import com.thecowking.wrought.blocks.honeycomb_coke_oven.HCCokeOvenControllerTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
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
        setDefaultState(this.getDefaultState().with(Multiblock.FORMED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.POWERED, Multiblock.FORMED);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    // creates the tile entity
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        LOGGER.info("made a TE");
        return new HCCokeOvenControllerTile();
    }


    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos posIn, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (player instanceof ServerPlayerEntity) {
            TileEntity tileEntity = worldIn.getTileEntity(posIn);
            if (tileEntity instanceof IMultiBlockControllerTile) {
                IMultiBlockControllerTile controllerTile = (IMultiBlockControllerTile) tileEntity;
                if(controllerTile.isFormed(worldIn) )  {
                    controllerTile.openGUI(worldIn, posIn, player, controllerTile);
                }  else if(controllerTile.isValidMultiBlockFormer(player.getHeldItem(hand).getItem())) {
                    LOGGER.info("no gui- attempt to form");
                    // attempts to form the multi-block
                    controllerTile.tryToFormMultiBlock(worldIn, posIn);
                }  else  {
                    controllerTile.openGUI(worldIn, posIn, player, controllerTile);
                }
            } else {
                LOGGER.info(posIn);
                LOGGER.info(tileEntity);
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return ActionResultType.SUCCESS;
    }






}

