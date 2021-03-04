package com.thecowking.wrought.data;

import com.thecowking.wrought.data.IMultiblockData;
import com.thecowking.wrought.inventory.containers.blast_furnace.BlastFurnaceContainerBuilder;
import com.thecowking.wrought.inventory.containers.blast_furnace.BlastFurnaceContainerMultiblock;
import com.thecowking.wrought.inventory.containers.honey_comb_coke_oven.HCCokeOvenContainerMultiblock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.thecowking.wrought.util.RegistryHandler.*;

public class BlastFurnaceData implements IMultiblockData {
    private static final Logger LOGGER = LogManager.getLogger();


    protected static Block frameBlock = BLAST_FURANCE_BRICK_FRAME.get();
    private static Block controllerBlock = BLAST_FURANCE_BRICK_CONTROLLER.get();
    private static Block airBlock = Blocks.AIR;




    private final Block[][][] posArray =  {
        {
            // bottom level - 11 wide circle
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null, null, null},
                {null, null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null, null},
                {null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null},
                {null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null},
                {null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null},
                {null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null},
                {null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null},
                {null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null},
                {null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null},
                {null, null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null, null},
                {null, null, null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
        },
        {
            // first level - 11 wide circle - hollow
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null, null, null},
                {null, null, null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null, null, null},
                {null, null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null, null},
                {null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                {null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                {null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                {null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                {null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                {null, null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null, null},
                {null, null, null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null, null, null},
                {null, null, null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
        },
            {
                    // second level - 11 wide circle - hollow
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, frameBlock, frameBlock, controllerBlock, frameBlock, frameBlock, null, null, null, null, null},
                    {null, null, null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null, null, null},
                    {null, null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null, null},
                    {null, null, null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null, null, null},
                    {null, null, null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            {
                    // third level - 11 wide circle - hollow
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null, null, null},
                    {null, null, null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null, null, null},
                    {null, null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null},
                    {null, null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null, null},
                    {null, null, null, null, frameBlock, airBlock, airBlock, airBlock, airBlock, airBlock, frameBlock, null, null, null, null},
                    {null, null, null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            {
                    // fourth level - 11 wide circle - temp roof
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null, null, null},
                    {null, null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null, null},
                    {null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null},
                    {null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null},
                    {null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null},
                    {null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null},
                    {null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null},
                    {null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null},
                    {null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null},
                    {null, null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null, null},
                    {null, null, null, null, null, frameBlock, frameBlock, frameBlock, frameBlock, frameBlock, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            }
    };

    public int getHeight()  {
        return posArray.length;
    }
    public int getWidth()  {
        return posArray[0][0].length;
    }
    public int getLength()  {
        return posArray[0].length;
    }
    public Block[][][] getPosArray() { return this.posArray; }
    public Block getBlockMember(int x, int y, int z)  {return this.posArray[x][y][z];}



    /*
West = -x
East = +X
North = -Z
South = +Z
this function will return the center most point based on the lengths of the mutli-blocks and the
direction that is fed in
*/
    public BlockPos calcCenterBlock(Direction inputDirection, BlockPos controllerPos)  {
        int xCoord = controllerPos.getX();
        int yCoord = controllerPos.getY();
        int zCoord = controllerPos.getZ();
        switch(inputDirection)  {
            case NORTH:
                return new BlockPos(xCoord, yCoord, zCoord + (getLength() / 2));
            case SOUTH:
                return new BlockPos(xCoord, yCoord, zCoord - (getLength() / 2));
            case WEST:
                return new BlockPos(xCoord  + (getLength() / 2), yCoord, zCoord);
            case EAST:
                return new BlockPos(xCoord  - (getLength() / 2), yCoord, zCoord);
        }
        return null;
    }


    public Direction getStairsDirection(Direction controllerDirection, int x, int z) {
        return null;
    }

    public INamedContainerProvider getContainerProvider(World world, BlockPos controllerPos, boolean isFormed) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("Honey Comb Coke Oven");
            }

            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                LOGGER.info(isFormed);
                if(isFormed)  {
                    return new BlastFurnaceContainerMultiblock(i, world, controllerPos, playerInventory);
                }
                return new BlastFurnaceContainerBuilder(i, world, controllerPos, playerInventory);
            }
        };
    }
}
