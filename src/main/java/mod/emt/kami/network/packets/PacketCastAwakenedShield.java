package mod.emt.kami.network.packets;

import io.netty.buffer.ByteBuf;
import mod.emt.kami.items.armor.ItemAwakenedShield;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCastAwakenedShield implements IMessage {
    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class MessageHandler implements IMessageHandler<PacketCastAwakenedShield, PacketCastAwakenedShieldClient> {
        @Override
        public PacketCastAwakenedShieldClient onMessage(PacketCastAwakenedShield message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> ItemAwakenedShield.castWithShield(ctx.getServerHandler().player));
            return new PacketCastAwakenedShieldClient();
        }
    }
}
