package com.thecowking.wrought.client.button;
import com.thecowking.wrought.network.Networking;
import com.thecowking.wrought.network.PacketFormMultiblock;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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
