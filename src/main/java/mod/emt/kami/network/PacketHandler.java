package mod.emt.kami.network;

import mod.emt.kami.Kami;
import mod.emt.kami.network.packets.PacketOpenPouchGui;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Kami.MOD_ID);

    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(PacketOpenPouchGui.MessageHandler.class, PacketOpenPouchGui.class, id++, Side.SERVER);
    }
}
