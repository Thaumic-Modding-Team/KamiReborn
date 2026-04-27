package mod.emt.kami.network.packets;

import baubles.api.BaublesApi;
import io.netty.buffer.ByteBuf;
import mod.emt.kami.Kami;
import mod.emt.kami.handlers.GuiHandlerKami;
import mod.emt.kami.registry.ModItemsKAMI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenPouchGui implements IMessage {
    public PacketOpenPouchGui() {}

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class MessageHandler implements IMessageHandler<PacketOpenPouchGui, IMessage> {
        @Override
        public IMessage onMessage(PacketOpenPouchGui message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                EntityPlayer player = ctx.getServerHandler().player;
                if(player != null) {
                    int baubleSlot = BaublesApi.isBaubleEquipped(player, ModItemsKAMI.FOCUS_POUCH);
                    if(baubleSlot > -1) {
                        player.openGui(Kami.instance, GuiHandlerKami.ID_ICHORWEAVE_POUCH_BAUBLE, player.world, 0, 0, 0);
                    }
                }
            });
            return null;
        }
    }
}
