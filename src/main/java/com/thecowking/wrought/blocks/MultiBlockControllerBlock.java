package com.thecowking.wrought.blocks;



import com.thecowking.wrought.blocks.honey_comb_coke_oven.HCCokeOven;
import com.thecowking.wrought.tileentity.MultiBlockControllerTile;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import com.thecowking.wrought.util.MultiBlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
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

public class MultiBlockControllerBlock extends Block implements IMultiBlockControllerBlock {


    public MultiBlockControllerBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2.0f)
                .harvestTool(ToolType.PICKAXE)
        );

        setDefaultState(this.getDefaultState().with(Multiblock.FORMED, false).with(Multiblock.RUNNING, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(Multiblock.FORMED, BlockStateProperties.FACING, Multiblock.RUNNING);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        if (state.get(Multiblock.RUNNING))  {
            return 14;
        }
        return 0;
    }





    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos posIn, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (player instanceof ServerPlayerEntity) {
            TileEntity tileEntity = world.getTileEntity(posIn);
            if (tileEntity instanceof MultiBlockControllerTile) {
                MultiBlockControllerTile controllerTile = (MultiBlockControllerTile) tileEntity;
                if(controllerTile.isFormed(controllerTile.getPos()) )  {

                    controllerTile.openGUIMultiblock(world, player);

                }  else if(controllerTile.isValidMultiBlockFormer(player.getHeldItem(hand).getItem())) {
                    LOGGER.info("no gui- attempt to form");
                    // attempts to form the multi-blocks
                    MultiBlockHelper.tryToFormMultiBlock(world, player, posIn, new HCCokeOven());
                }  else  {
                    controllerTile.openGUI(world, posIn, player, controllerTile);
                }
            } else {
                LOGGER.info(posIn);
                LOGGER.info(tileEntity);
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        super.onBlockActivated(state, world, posIn, player, hand, trace);
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state)  {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)  {
        return Container.calcRedstone(Multiblock.getTileFromPos(worldIn, pos));
    }

}

