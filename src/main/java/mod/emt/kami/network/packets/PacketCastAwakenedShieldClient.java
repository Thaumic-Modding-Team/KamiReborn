package mod.emt.kami.network.packets;

import io.netty.buffer.ByteBuf;
import mod.emt.kami.items.armor.ItemAwakenedShield;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketCastAwakenedShieldClient implements IMessage {
    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class MessageHandler implements IMessageHandler<PacketCastAwakenedShieldClient, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketCastAwakenedShieldClient message, MessageContext ctx) {
            FMLClientHandler.instance().getClient().addScheduledTask(() -> ItemAwakenedShield.castWithShield(Minecraft.getMinecraft().player));
            return null;
        }
    }
}
