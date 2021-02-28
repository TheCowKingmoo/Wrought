package com.thecowking.wrought.items.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CokeBlockItem extends BlockItem {
    public CokeBlockItem(Block blockIn) {
        super(blockIn, new Item.Properties().group(ItemGroup.MATERIALS));
    }
    @Override
    public int getBurnTime(ItemStack itemStack)  {
        return 10000;
    }
}
