package mod.emt.kami.items.misc;

import mod.emt.kami.items.base.ItemBase;
import mod.emt.kami.tiles.TileCelestialGateway;
import mod.emt.kami.utils.helpers.WorldHelper;
import mod.emt.kami.utils.misc.SerializablePosition;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemCelestialPearl extends ItemBase {
    public ItemCelestialPearl() {
        super("celestial_pearl");
        this.setRarity(EnumRarity.EPIC);
        this.setMaxStackSize(1);
    }

    public static boolean isPearlAndLinked(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return !stack.isEmpty() && stack.getItem() instanceof ItemCelestialPearl && tag != null && tag.hasKey("gatewayPos");
    }

    @Nullable
    public static SerializablePosition getSerializedPosition(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (!stack.isEmpty() && stack.getItem() instanceof ItemCelestialPearl && tag != null && tag.hasKey("gatewayPos")) {
            return new SerializablePosition(tag.getCompoundTag("gatewayPos"));
        } else {
            return null;
        }
    }

    @Override
    public @NotNull EnumActionResult onItemUseFirst(EntityPlayer player, @NotNull World world, @NotNull BlockPos pos, @NotNull EnumFacing side, float hitX, float hitY, float hitZ, @NotNull EnumHand hand) {
        if(player.isSneaking()) {
            ItemStack heldStack = player.getHeldItem(hand);
            TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof TileCelestialGateway) {
                if(world.isRemote) {
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.6f, 1.0f);
                } else {
                    if(((TileCelestialGateway) tile).hasCustomName()) {
                        this.setGatewayName(heldStack, ((TileCelestialGateway) tile).getName());
                    } else {
                        this.setGatewayName(heldStack, null);
                    }
                    SerializablePosition position = new SerializablePosition(world, pos);
                    this.setSerializedPosition(heldStack, position);
                }
                return EnumActionResult.SUCCESS;
            }
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        this.appendTooltipInfo(stack, tooltip);
    }

    @SideOnly(Side.CLIENT)
    public void appendTooltipInfo(ItemStack stack, List<String> tooltip) {
        String gatewayName = this.getGatewayName(stack);
        if(gatewayName != null) {
            tooltip.add(I18n.format("tooltip.kami.celestial_pearl.gateway", gatewayName));
            tooltip.add("");
        }

        SerializablePosition position = getSerializedPosition(stack);
        if(position != null) {
            int dimId = position.getDimensionId();
            BlockPos pos = position.getPosition();

            tooltip.add(I18n.format("tooltip.kami.celestial_pearl.dimension", WorldHelper.getDimensionName(dimId)));
            tooltip.add(" " + I18n.format("tooltip.kami.celestial_pearl.posx", pos.getX()));
            tooltip.add(" " + I18n.format("tooltip.kami.celestial_pearl.posy", pos.getY()));
            tooltip.add(" " + I18n.format("tooltip.kami.celestial_pearl.posz", pos.getZ()));
        }
    }

    public void setSerializedPosition(ItemStack stack, SerializablePosition position) {
        stack.setTagInfo("gatewayPos", position.serializeNBT());
    }

    @Nullable
    public String getGatewayName(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.hasKey("gatewayName") ? tag.getString("gatewayName") : null;
    }

    public void setGatewayName(ItemStack stack, @Nullable String gatewayName) {
        if(gatewayName == null) {
            if(stack.getTagCompound() != null) {
                stack.getTagCompound().removeTag("gatewayName");
            }
        } else {
            stack.setTagInfo("gatewayName", new NBTTagString(gatewayName));
        }
    }
}
