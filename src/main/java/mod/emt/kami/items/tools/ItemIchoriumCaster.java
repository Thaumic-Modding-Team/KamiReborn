package mod.emt.kami.items.tools;

import com.invadermonky.thaumicapi.api.item.AbstractItemCaster;
import mod.emt.kami.Kami;
import mod.emt.kami.items.IDyeableGear;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.items.IWarpingGear;
import thaumcraft.common.items.casters.CasterManager;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemIchoriumCaster extends AbstractItemCaster implements IDyeableGear, IWarpingGear {
    public ItemIchoriumCaster() {
        super(5);
        this.setRegistryName(Kami.MOD_ID, "ichorium_caster");
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(Kami.tabKAMI);
        this.addPropertyOverride(new ResourceLocation("dyed"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(@NotNull ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entityIn)
            {
                if (getDyedColor(stack) != getDefaultDyedColorForMeta(stack.getMetadata()))
                {
                    return 1.0F;
                }

                return 0.0F;
            }
        });
    }

    @Override
    public int getChunkDrainRadius(EntityPlayer entityPlayer, ItemStack itemStack) {
        return 3;
    }

    @Override
    public float getConsumptionModifier(ItemStack itemStack, EntityPlayer player, boolean b) {
        float base = 0.6f;
        if (player != null) {
            base -= CasterManager.getTotalVisDiscount(player);
        }
        return Math.max(0, base);
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public int getDyedColor(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            }

        assert stack.getTagCompound() != null;
        if (stack.getTagCompound().hasKey("color", Constants.NBT.TAG_INT))
            return stack.getTagCompound().getInteger("color");
        else {
            stack.getTagCompound().setInteger("color", getDefaultDyedColorForMeta(stack.getMetadata()));
            return stack.getTagCompound().getInteger("color");
        }
    }

    @Override
    public void setDyedColor(ItemStack stack, int color) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        assert stack.getTagCompound() != null;
        stack.getTagCompound().setInteger("color", color);
    }

    @Override
    public int getDefaultDyedColorForMeta(int meta) {
        return 16748295;
    }

    @Override
    public int getWarp(ItemStack itemStack, EntityPlayer entityPlayer) {
        return 3;
    }

    @Override
    public @NotNull EnumActionResult onItemUseFirst(EntityPlayer player, World world, @NotNull BlockPos pos, @NotNull EnumFacing side, float hitX, float hitY, float hitZ, @NotNull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        IBlockState state = world.getBlockState(pos);

        // Right-clicking a filled cauldron with the dyed item will wash it out
        if (state.getBlock() == Blocks.CAULDRON && state.getValue(BlockCauldron.LEVEL) > 0 && getDyedColor(stack) != getDefaultDyedColorForMeta(stack.getMetadata())) {
            setDyedColor(stack, getDefaultDyedColorForMeta(stack.getMetadata()));
            world.setBlockState(pos, state.withProperty(BlockCauldron.LEVEL, state.getValue(BlockCauldron.LEVEL) - 1));
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 0.5F, 1.0F);
            return EnumActionResult.SUCCESS;
        }

        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> list, @NotNull ITooltipFlag tooltip) {
        int color = getDyedColor(stack);

        // If it's dyed, show it on the tooltip
        if (color != getDefaultDyedColorForMeta(stack.getMetadata())) {
            if (tooltip.isAdvanced())
                list.add(new TextComponentTranslation("item.color", TextFormatting.GRAY + String.format("#%06X", color)).getFormattedText());
            else {
                list.add(TextFormatting.ITALIC + new TextComponentTranslation("item.dyed").getFormattedText());
            }
        }

        super.addInformation(stack, world, list, tooltip);
    }
}
