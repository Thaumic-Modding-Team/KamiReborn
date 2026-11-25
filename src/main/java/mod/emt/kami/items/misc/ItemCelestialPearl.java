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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerWarp;

import java.util.List;

public class ItemCelestialPearl extends ItemBase {
    public ItemCelestialPearl() {
        super("celestial_pearl");
        this.setRarity(EnumRarity.EPIC);
        this.setMaxStackSize(16);
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

    public static void clearSerializedPosition(ItemStack stack) {
        if(stack.getTagCompound() != null) {
            stack.getTagCompound().removeTag("gatewayName");
            stack.getTagCompound().removeTag("gatewayPos");
            if(stack.getTagCompound().isEmpty()) {
                stack.setTagCompound(null);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getItemStackLimit(@NotNull ItemStack stack) {
        return isPearlAndLinked(stack) ? 1 : this.getItemStackLimit();
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, @NotNull EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        if(playerIn.isSneaking()) {
            if(isPearlAndLinked(heldStack)) {
                clearSerializedPosition(heldStack);
                if(worldIn.isRemote) {
                    playerIn.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.6f ,0.5f);
                }
                return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
            }
        } else {
            SerializablePosition targetPos = getSerializedPosition(heldStack);
            if(targetPos != null) {
                World targetWorld = targetPos.getWorld(true);
                if(targetWorld != null) {
                    TileEntity tile = targetWorld.getTileEntity(targetPos.getPosition());
                    if(tile instanceof TileCelestialGateway) {
                        if (!playerIn.isCreative()) {
                            heldStack.shrink(1);
                        }
                        if(!worldIn.isRemote && !playerIn.isCreative() && worldIn.rand.nextInt(4) == 0) {
                            ThaumcraftApi.internalMethods.addWarpToPlayer(playerIn, 1 + worldIn.rand.nextInt(3), IPlayerWarp.EnumWarpType.TEMPORARY);
                        }
                        ((TileCelestialGateway) tile).teleportToGateway(playerIn);
                        worldIn.playSound(null, playerIn.getPosition(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    } else {
                        if(worldIn.isRemote) {
                            playerIn.sendStatusMessage(new TextComponentTranslation("chat.kami.celestial_pearl.missing_gateway").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)), false);
                        }
                        clearSerializedPosition(heldStack);
                    }
                    return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
                }
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public @NotNull EnumActionResult onItemUseFirst(EntityPlayer player, @NotNull World world, @NotNull BlockPos pos, @NotNull EnumFacing side, float hitX, float hitY, float hitZ, @NotNull EnumHand hand) {
        if(player.isSneaking()) {
            ItemStack pearlStack = player.getHeldItem(hand);
            TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof TileCelestialGateway) {
                pearlStack = pearlStack.splitStack(1);
                if(world.isRemote) {
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.6f, 1.0f);
                } else {
                    if(((TileCelestialGateway) tile).hasCustomName()) {
                        this.setGatewayName(pearlStack, ((TileCelestialGateway) tile).getName());
                    } else {
                        this.setGatewayName(pearlStack, null);
                    }
                    SerializablePosition position = new SerializablePosition(world, pos);
                    this.setSerializedPosition(pearlStack, position);
                    ItemHandlerHelper.giveItemToPlayer(player, pearlStack, player.inventory.currentItem);
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
