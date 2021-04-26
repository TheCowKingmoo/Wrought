package com.thecowking.wrought.data;

import com.thecowking.wrought.init.RecipeSerializerInit;
import com.thecowking.wrought.inventory.containers.casting_machine.CastingMachineContainerBuilder;
import com.thecowking.wrought.inventory.containers.casting_machine.CastingMachineContainerMultiblock;
import com.thecowking.wrought.recipes.IWroughtRecipe;
import com.thecowking.wrought.util.RecipeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Set;

import static com.thecowking.wrought.init.RegistryHandler.*;

public class CastingMachineData implements IMultiblockData {

    protected static Block frameBlock = REFRACTORY_BRICK.get();


    private static Block controllerBlock = CASTING_MACHINE_CONTROLLER.get();
    private static Block airBlock = Blocks.AIR;

    private final Block[][][] posArray =  {
            {
                    {frameBlock, frameBlock, frameBlock},
                    {frameBlock, frameBlock, frameBlock},
                    {frameBlock, frameBlock, frameBlock}
            },
            {
                    {frameBlock, controllerBlock, frameBlock},
                    {frameBlock, airBlock, frameBlock},
                    {frameBlock, frameBlock, frameBlock}
            },
            {
                    {frameBlock, frameBlock, frameBlock},
                    {frameBlock, frameBlock, frameBlock},
                    {frameBlock, frameBlock, frameBlock}
            }
    };

    public int getHeight()  {
        return posArray.length;
    }
    public int getWidth()  {
        return posArray[0][0].length;
    }
    public int getNumberItemInputSlots() {
        return 0;
    }
    public int getNumberItemOutputSlots() {
        return 1;
    }
    public int getLength()  {
        return posArray[0].length;
    }
    public Block[][][] getPosArray() { return this.posArray; }
    public Block getBlockMember(int x, int y, int z)  {return this.posArray[x][y][z];}
    public int getControllerYIndex()  {
        return 2;
    }



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
                return new BlockPos(xCoord, yCoord, zCoord + 1);
            case SOUTH:
                return new BlockPos(xCoord, yCoord, zCoord - 1);
            case WEST:
                return new BlockPos(xCoord  + 1, yCoord, zCoord);
            case EAST:
                return new BlockPos(xCoord  - 1, yCoord, zCoord);
        }
        return null;
    }


    /*
        West = -x
        East = +X
        North = -Z
        South = +Z
     */
    public Direction getStairsDirection(BlockPos controllerPos, BlockPos blockPos, Direction controllerDirection, int x, int z)  {
        return null;
    }

    public SlabType getSlabDirection(int y)  {
        return null;
    }

    public INamedContainerProvider getContainerProvider(World world, BlockPos controllerPos, boolean isFormed) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("Casting Machine");
            }

            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                if(isFormed)  {
                    return new CastingMachineContainerMultiblock(i, world, controllerPos, playerInventory);
                }
                return new CastingMachineContainerBuilder(i, world, controllerPos, playerInventory);
            }
        };
    }


    /*
    West = -x
    East = +X
    North = -Z
    South = +Z
    this function will return the North-Western corner of the multi blocks to be formed
    this is the lowest int value of the multiblock which makes it easier to iterate over when forming
  */
    public BlockPos findLowsestValueCorner(BlockPos centerPos, Direction inputDirection, int longerSide, int shorterSide)  {
        if(centerPos == null)  return null;

        int xCoord = centerPos.getX();
        int yCoord = centerPos.getY();
        int zCoord = centerPos.getZ();

        switch(inputDirection)  {
            case NORTH:
                return new BlockPos(xCoord - (shorterSide / 2), yCoord - getControllerYIndex() , zCoord - (longerSide / 2));
            case SOUTH:
                return new BlockPos(xCoord  - (shorterSide / 2), yCoord  - getControllerYIndex(), zCoord - (longerSide / 2));
            case WEST:
                return new BlockPos(xCoord  - (longerSide / 2), yCoord  - getControllerYIndex(), zCoord  - (shorterSide / 2));
            case EAST:
                return new BlockPos(xCoord  - (longerSide / 2), yCoord  - getControllerYIndex(), zCoord  - (shorterSide / 2));
            default:
                return null;
        }
    }

    public BlockPos getRedstoneInBlockPos(BlockPos controllerPos) {
        return new BlockPos(controllerPos.getX(), controllerPos.getY()+1, controllerPos.getZ());
    }

    public BlockPos getRedstoneOutBlockPos(BlockPos controllerPos) {
        return new BlockPos(controllerPos.getX(), controllerPos.getY()-1, controllerPos.getZ());
    }

    public Set<IRecipe<?>> getRecipesByType(World world) {
        return RecipeUtil.findRecipesByType(this.getRecipeType(), world);
    }

    @Override
    public IRecipeType<IWroughtRecipe> getRecipeType() {
        return RecipeSerializerInit.CASTING_MACHINE_TYPE;
    }

}
