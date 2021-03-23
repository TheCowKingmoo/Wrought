package com.thecowking.wrought.items.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CokeItem extends Item {
    public CokeItem() {
        super(new Item.Properties().group(ItemGroup.MATERIALS));
    }

    @Override
    public int getBurnTime(ItemStack itemStack)  {
        return 3200;
    }
}
