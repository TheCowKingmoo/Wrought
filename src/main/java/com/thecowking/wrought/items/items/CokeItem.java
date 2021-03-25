package com.thecowking.wrought.items.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CokeItem extends ItemBase {
    public CokeItem() {
        super();
    }

    @Override
    public int getBurnTime(ItemStack itemStack)  {
        return 3200;
    }
}
