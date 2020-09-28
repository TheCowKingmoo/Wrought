package com.thecowking.wrought.util;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;


// Default block item
public class BlockItemBase extends BlockItem {
    public BlockItemBase(Block blockIn) {
        super(blockIn, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS));
    }
}
