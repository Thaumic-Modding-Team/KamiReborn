package mod.emt.kami.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;

import java.awt.*;

public class PacketFXDrawBamf implements IMessage {
    private double posX;
    private double posY;
    private double posZ;
    private int color;
    private boolean doSound;
    private boolean doFlair;
    private EnumFacing side;

    public PacketFXDrawBamf(double posX, double posY, double posZ, int color, boolean doSound, boolean doFlair, EnumFacing side) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.color = color;
        this.doSound = doSound;
        this.doFlair = doFlair;
        this.side = side;
    }

    public PacketFXDrawBamf(BlockPos pos, int color, boolean doSound, boolean doFlair, EnumFacing side) {
        this(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, color, doSound, doFlair, side);
    }

    public PacketFXDrawBamf() {
        this(BlockPos.ORIGIN, 0xFFFFFF, false, false, EnumFacing.UP);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.posX = buf.readDouble();
        this.posY = buf.readDouble();
        this.posZ = buf.readDouble();
        this.color = buf.readInt();
        this.doSound = buf.readBoolean();
        this.doFlair = buf.readBoolean();
        this.side = EnumFacing.byIndex(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(this.posX);
        buf.writeDouble(this.posY);
        buf.writeDouble(this.posZ);
        buf.writeInt(this.color);
        buf.writeBoolean(this.doSound);
        buf.writeBoolean(this.doFlair);
        buf.writeInt(this.side.getIndex());
    }

    public static class MessageHandler implements IMessageHandler<PacketFXDrawBamf, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketFXDrawBamf message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Color color = new Color(message.color);
                float r = (float) color.getRed() / 255.0f;
                float g = (float) color.getGreen() / 255.0f;
                float b = (float) color.getBlue() / 255.0f;
                FXDispatcher.INSTANCE.drawBamf(message.posX, message.posY, message.posZ, r, g, b, message.doSound, message.doFlair, message.side);
            });
            return null;
        }
    }
}
