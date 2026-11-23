package mod.emt.kami.items.misc;

import mod.emt.kami.Kami;
import mod.emt.kami.tiles.TileCelestialGateway;
import mod.emt.kami.utils.helpers.WorldHelper;
import mod.emt.kami.utils.misc.SerializablePosition;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import java.util.Objects;

public class ItemCelestialPearl extends Item {
    public ItemCelestialPearl() {
        this.setRegistryName(Kami.MOD_ID, "celestial_pearl");
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(Kami.tabKAMI);
        this.setMaxStackSize(1);
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
        SerializablePosition position = this.getSerializedPosition(stack);
        if(position != null) {
            int dimId = position.getDimensionId();
            BlockPos pos = position.getPosition();

            tooltip.add(I18n.format("tooltip.kami.celestial_pearl.dimension", WorldHelper.getDimensionName(dimId)));
            tooltip.add(" " + I18n.format("tooltip.kami.celestial_pearl.posx", pos.getX()));
            tooltip.add(" " + I18n.format("tooltip.kami.celestial_pearl.posy", pos.getY()));
            tooltip.add(" " + I18n.format("tooltip.kami.celestial_pearl.posz", pos.getZ()));
        }
    }

    public static boolean isPearlAndLinked(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return !stack.isEmpty() && stack.getItem() instanceof ItemCelestialPearl && tag != null && tag.hasKey("gatewayPos");
    }

    @Nullable
    public SerializablePosition getSerializedPosition(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.hasKey("gatewayPos") ? new SerializablePosition(tag.getCompoundTag("gatewayPos")) : null;
    }

    public void setSerializedPosition(ItemStack stack, SerializablePosition position) {
        stack.setTagInfo("gatewayPos", position.serializeNBT());
    }
}
