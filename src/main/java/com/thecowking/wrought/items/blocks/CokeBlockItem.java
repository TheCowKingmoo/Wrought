package com.thecowking.wrought.items.blocks;

import com.thecowking.wrought.init.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

public class CokeBlockItem extends BlockItem {
    public CokeBlockItem(Block blockIn) {
        super(blockIn, new Item.Properties().group(ItemGroup.MATERIALS));
    }
    @Override
    public int getBurnTime(ItemStack itemStack)  {
        return (9 * ForgeHooks.getBurnTime(RegistryHandler.COKE.get().getDefaultInstance()));
    }
}
