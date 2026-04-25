package mod.emt.kami.items.tools;

import mod.emt.kami.Kami;
import mod.emt.kami.registry.ModItemsKAMI;
import mod.emt.kami.utils.helpers.ItemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.items.IWarpingGear;

import java.util.Objects;

public class ItemIchoriumAxe extends ItemAxe implements IWarpingGear {
    public ItemIchoriumAxe() {
        this("ichorium_axe");
    }

    protected ItemIchoriumAxe(String unlocName) {
        super(ModItemsKAMI.MATERIAL_ICHORIUM, 10.0f, -3.0f);
        this.setRegistryName(Kami.MOD_ID, unlocName);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(Kami.tabKAMI);
    }

    @Override
    public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
        if(this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            ItemHelper.setUnbreakable(stack);
            items.add(stack);
        }
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public int getWarp(ItemStack itemstack, EntityPlayer player) {
        return 1;
    }
}
