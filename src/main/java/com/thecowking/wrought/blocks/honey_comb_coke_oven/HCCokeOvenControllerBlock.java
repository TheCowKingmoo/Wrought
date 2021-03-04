package com.thecowking.wrought.blocks.honey_comb_coke_oven;

import com.thecowking.wrought.blocks.INameableTile;
import com.thecowking.wrought.blocks.MultiBlockControllerBlock;
import com.thecowking.wrought.data.HCCokeOvenData;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCCokeOvenControllerTile;
import com.thecowking.wrought.util.MultiBlockHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nullable;


public class HCCokeOvenControllerBlock extends MultiBlockControllerBlock implements INameableTile {
    private static final Logger LOGGER = LogManager.getLogger();

    String tileName;

    public HCCokeOvenControllerBlock() {
        super();
    }

    // creates the tile entity
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new HCCokeOvenControllerTile();
    }



    @Override
    public void onBlockHarvested(World worldIn, BlockPos posIn, BlockState state, PlayerEntity player) {
        MultiBlockHelper.destroyMultiBlock(worldIn, posIn, new HCCokeOvenData());
    }

    @Override
    public void setTileName(String name) {
        this.tileName = name;
    }
}