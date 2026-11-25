package mod.emt.kami.items.misc;

import mod.emt.kami.items.base.ItemBase;
import mod.emt.kami.tiles.TileCelestialGateway;
import mod.emt.kami.utils.helpers.PlayerHelper;
import mod.emt.kami.utils.helpers.WorldHelper;
import mod.emt.kami.utils.misc.SerializablePosition;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
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
    public static final String TAG_GATEWAY_NAME = "gatewayName";
    public static final String TAG_GATEWAY_POS = "gatewayPos";
    public static final String TAG_IS_GATEWAY_LINK = "isGateway";

    public ItemCelestialPearl() {
        super("celestial_pearl");
        this.setRarity(EnumRarity.EPIC);
        this.setMaxStackSize(16);
    }

    public static boolean isPearlAndLinked(ItemStack stack, boolean requireGatewayLink) {
        NBTTagCompound tag = stack.getTagCompound();
        return !stack.isEmpty() && stack.getItem() instanceof ItemCelestialPearl
                && tag != null && tag.hasKey(TAG_GATEWAY_POS)
                && (!requireGatewayLink || tag.getBoolean(TAG_IS_GATEWAY_LINK));
    }

    public static SerializablePosition getSerializedPosition(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (!stack.isEmpty() && stack.getItem() instanceof ItemCelestialPearl && tag != null && tag.hasKey(TAG_GATEWAY_POS)) {
            return new SerializablePosition(tag.getCompoundTag(TAG_GATEWAY_POS));
        } else {
            return SerializablePosition.EMPTY;
        }
    }

    public static void setSerializedPosition(ItemStack stack, SerializablePosition position) {
        stack.setTagInfo(TAG_GATEWAY_POS, position.serializeNBT());
    }

    public static void clearSerializedPosition(ItemStack stack) {
        if(stack.getTagCompound() != null) {
            stack.getTagCompound().removeTag(TAG_GATEWAY_NAME);
            stack.getTagCompound().removeTag(TAG_GATEWAY_POS);
            stack.getTagCompound().removeTag(TAG_IS_GATEWAY_LINK);
            if(stack.getTagCompound().isEmpty()) {
                stack.setTagCompound(null);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getItemStackLimit(@NotNull ItemStack stack) {
        return isPearlAndLinked(stack, false) ? 1 : this.getItemStackLimit();
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, @NotNull EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        if(playerIn.isSneaking()) {
            if(isPearlAndLinked(heldStack, false)) {
                clearSerializedPosition(heldStack);
                if(worldIn.isRemote) {
                    playerIn.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.6f ,0.5f);
                }
                return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
            }
        } else {
            SerializablePosition targetPos = getSerializedPosition(heldStack);
            World targetWorld = targetPos.getWorld(true);
            if(!targetPos.isEmpty() && targetWorld != null) {
                TileEntity tile = targetWorld.getTileEntity(targetPos.getPosition());

                int warpChance;
                if(tile instanceof TileCelestialGateway) {
                    warpChance = 5;
                    ((TileCelestialGateway) tile).teleportToGateway(playerIn);
                } else {
                    warpChance = 2;
                    PlayerHelper.teleportPlayer(playerIn, targetPos);
                }

                if (!playerIn.isCreative()) {
                    heldStack.shrink(1);
                }
                if(!worldIn.isRemote && !playerIn.isCreative() && worldIn.rand.nextInt(warpChance) == 0) {
                    ThaumcraftApi.internalMethods.addWarpToPlayer(playerIn, 1 + worldIn.rand.nextInt(3), IPlayerWarp.EnumWarpType.TEMPORARY);
                }
                worldIn.playSound(null, playerIn.getPosition(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f);
                return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
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
                    this.setIsGatewayLink(pearlStack, true);
                    setSerializedPosition(pearlStack, position);
                    ItemHandlerHelper.giveItemToPlayer(player, pearlStack, player.inventory.currentItem);
                }
                return EnumActionResult.SUCCESS;
            } else {
                pearlStack = pearlStack.splitStack(1);
                if(world.isRemote) {
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.6f, 1.0f);
                } else {
                    SerializablePosition position = new SerializablePosition(world, pos.offset(side));
                    setSerializedPosition(pearlStack, position);
                    this.setIsGatewayLink(pearlStack, false);
                    this.setGatewayName(pearlStack, null);
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
        if(!position.isEmpty()) {
            int dimId = position.getDimensionId();
            BlockPos pos = position.getPosition();

            tooltip.add(I18n.format("tooltip.kami.celestial_pearl.dimension", WorldHelper.getDimensionName(dimId)));
            tooltip.add(" " + I18n.format("tooltip.kami.celestial_pearl.posx", pos.getX()));
            tooltip.add(" " + I18n.format("tooltip.kami.celestial_pearl.posy", pos.getY()));
            tooltip.add(" " + I18n.format("tooltip.kami.celestial_pearl.posz", pos.getZ()));
        }
    }

    public boolean getIsGatewayLink(ItemStack stack) {
        return stack.getTagCompound() != null && stack.getTagCompound().getBoolean(TAG_IS_GATEWAY_LINK);
    }

    public void setIsGatewayLink(ItemStack stack, boolean isGatewayLink) {
        stack.setTagInfo(TAG_IS_GATEWAY_LINK, new NBTTagByte((byte) (isGatewayLink ? 1 : 0)));
    }

    @Nullable
    public String getGatewayName(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.hasKey(TAG_GATEWAY_NAME) ? tag.getString(TAG_GATEWAY_NAME) : null;
    }

    public void setGatewayName(ItemStack stack, @Nullable String gatewayName) {
        if(gatewayName == null) {
            if(stack.getTagCompound() != null) {
                stack.getTagCompound().removeTag(TAG_GATEWAY_NAME);
            }
        } else {
            stack.setTagInfo(TAG_GATEWAY_NAME, new NBTTagString(gatewayName));
        }
    }
}
