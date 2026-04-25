package mod.emt.kami.items.armor;

import mod.emt.kami.Kami;
import mod.emt.kami.api.item.IItemAddition;
import mod.emt.kami.client.model.armor.ModelIchorArmor;
import mod.emt.kami.items.IDyeableGear;
import mod.emt.kami.items.base.ItemBaseArmor;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemIchorArmor extends ItemBaseArmor implements IItemAddition, IDyeableGear {
    protected static final String TEXTURE_PATH_1 = new ResourceLocation(Kami.MOD_ID, "textures/models/armor/ichorweave_layer_1.png").toString();
    protected static final String TEXTURE_PATH_2 = new ResourceLocation(Kami.MOD_ID, "textures/models/armor/ichorweave_layer_2.png").toString();
    protected static final String TEXTURE_PATH_DYED_1 = new ResourceLocation(Kami.MOD_ID, "textures/models/armor/ichorweave_layer_1_dyed.png").toString();
    protected static final String TEXTURE_PATH_DYED_2 = new ResourceLocation(Kami.MOD_ID, "textures/models/armor/ichorweave_layer_2_dyed.png").toString();
    protected static final String TEXTURE_PATH_DYED_OVERLAY_1 = new ResourceLocation(Kami.MOD_ID, "textures/models/armor/ichorweave_layer_1_dyed_overlay.png").toString();
    protected static final String TEXTURE_PATH_DYED_OVERLAY_2 = new ResourceLocation(Kami.MOD_ID, "textures/models/armor/ichorweave_layer_2_dyed_overlay.png").toString();

    public static final ModelIchorArmor ARMOR_OUTER = new ModelIchorArmor(1.0F);
    public static final ModelIchorArmor ARMOR_INNER = new ModelIchorArmor(0.5F);

    public ItemIchorArmor(String unlocName, ArmorMaterial material, EntityEquipmentSlot equipmentSlot) {
        super(unlocName, material, equipmentSlot, null);
        this.addPropertyOverride(new ResourceLocation("dyed"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(@NotNull ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                if (getDyedColor(stack) != getDefaultDyedColorForMeta(stack.getMetadata())) {
                    return 1.0F;
                }

                return 0.0F;
            }
        });
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(@NotNull EntityLivingBase entity, @NotNull ItemStack stack, @NotNull EntityEquipmentSlot slot, @NotNull ModelBiped bipedModel) {
        ModelIchorArmor armorModel = (slot == EntityEquipmentSlot.LEGS) ? ARMOR_INNER : ARMOR_OUTER;

        if (armorModel != null) {
            return armorModel;
        }

        return bipedModel;
    }

    @Override
    public int getDyedColor(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        assert stack.getTagCompound() != null;
        if (!stack.getTagCompound().hasKey("color", Constants.NBT.TAG_INT)) {
            stack.getTagCompound().setInteger("color", getDefaultDyedColorForMeta(stack.getMetadata()));
        }

        return stack.getTagCompound().getInteger("color");
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
        return 12607488;
    }

    @Override
    public boolean hasColor(@NotNull ItemStack stack) {
        return this.getDyedColor(stack) != getDefaultDyedColorForMeta(stack.getMetadata()) ? true : false;
    }

    @Override
    public int getColor(@NotNull ItemStack stack) {
        return getDyedColor(stack);
    }

    @Override
    public void removeColor(@NotNull ItemStack stack) {
        setDyedColor(stack, getDefaultDyedColorForMeta(stack.getMetadata()));
    }

    @Override
    public void setColor(@NotNull ItemStack stack, int color) {
        setDyedColor(stack, color);
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

    @Override
    public String getArmorTexture(@NotNull ItemStack stack, @NotNull Entity entity, @NotNull EntityEquipmentSlot slot, @NotNull String type) {
        // If dye is never used on it, it'll use a dyeless texture instead
        if (this.getDyedColor(stack) != getDefaultDyedColorForMeta(stack.getMetadata())) {
            if (slot == EntityEquipmentSlot.LEGS) {
                return type == null ? TEXTURE_PATH_DYED_2 : TEXTURE_PATH_DYED_OVERLAY_2;
            } else {
                return type == null ? TEXTURE_PATH_DYED_1 : TEXTURE_PATH_DYED_OVERLAY_1;
            }
        } else {
            if (slot == EntityEquipmentSlot.LEGS) {
                return type == null ? TEXTURE_PATH_2 : TEXTURE_PATH_DYED_OVERLAY_2;
            }
        }

        return type == null ? TEXTURE_PATH_1 : TEXTURE_PATH_DYED_OVERLAY_1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> list, @NotNull ITooltipFlag tooltip) {
        int color = getDyedColor(stack);

        // If it's dyed, show it on the tooltip
        if (color != getDefaultDyedColorForMeta(stack.getMetadata())) {
            if (tooltip.isAdvanced())
                list.add(new TextComponentTranslation("item.color", TextFormatting.GRAY + String.format("#%06X", color)).getFormattedText());
            else {
                list.add(TextFormatting.ITALIC + new TextComponentTranslation("item.dyed").getFormattedText());
            }
        }
    }
}
