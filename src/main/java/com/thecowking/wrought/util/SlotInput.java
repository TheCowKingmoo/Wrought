package com.thecowking.wrought.util;

import com.thecowking.wrought.blocks.MultiBlock.MultiBlockControllerTile;
import com.thecowking.wrought.blocks.MultiBlock.honey_comb_coke_oven.HCCokeOvenControllerTile;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class SlotInput extends SlotItemHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    public SlotInput(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
}
