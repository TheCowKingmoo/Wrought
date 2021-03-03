package com.thecowking.wrought.client.button;

import com.thecowking.wrought.blocks.honey_comb_coke_oven.HCCokeOven;
import com.thecowking.wrought.network.Networking;
import com.thecowking.wrought.network.PacketFormMultiblock;
import com.thecowking.wrought.tileentity.honey_comb_coke_oven.HCStateData;
import com.thecowking.wrought.util.MultiBlockHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildClick implements Button.IPressable {
    private static final Logger LOGGER = LogManager.getLogger();
    private BlockPos controllerPos;

    public BuildClick(BlockPos controllerPos)  {
        this.controllerPos = controllerPos;

    }

    @Override
    public void onPress(Button b) {
        LOGGER.info("button send");
        Networking.sendToServer(new PacketFormMultiblock(this.controllerPos));
    }
}
