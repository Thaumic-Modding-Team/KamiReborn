package mod.emt.kami.items.tools;

import com.invadermonky.thaumicapi.api.item.AbstractItemCasterDyeable;
import mod.emt.kami.Kami;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.items.IWarpingGear;
import thaumcraft.common.items.casters.CasterManager;

import javax.annotation.Nullable;
import java.util.Objects;

public class ItemIchoriumCaster extends AbstractItemCasterDyeable implements IWarpingGear {
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
                if (getDyedColor(stack) != getDefaultDyedColor(stack))
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
    public void setDyedColor(ItemStack stack, int color) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        assert stack.getTagCompound() != null;
        stack.getTagCompound().setInteger("color", color);
    }

    @Override
    public int getDefaultDyedColor(ItemStack stack) {
        return 16748295;
    }

    @Override
    public int getWarp(ItemStack itemStack, EntityPlayer entityPlayer) {
        return 3;
    }
}
