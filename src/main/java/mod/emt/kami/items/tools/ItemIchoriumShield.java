package mod.emt.kami.items.tools;

import mod.emt.kami.Kami;
import mod.emt.kami.registry.ModItemsKAMI;
import mod.emt.kami.utils.helpers.ItemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.items.IWarpingGear;

import java.util.Objects;

public class ItemIchoriumShield extends ItemShield implements IWarpingGear {
    public ItemIchoriumShield(String modId, String unlocName) {
        this.setRegistryName(modId, unlocName);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setMaxDamage(ModItemsKAMI.MATERIAL_ICHORIUM.getMaxUses());
    }

    protected ItemIchoriumShield(String unlocName) {
        this(Kami.MOD_ID, unlocName);
        this.setCreativeTab(Kami.tabKAMI);
    }

    public ItemIchoriumShield() {
        this("ichorium_shield");
    }

    @Override
    public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
        if(this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            ItemHelper.setUnbreakable(stack);
            items.add(stack);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull String getItemStackDisplayName(@NotNull ItemStack stack) {
        return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
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
    public int getWarp(ItemStack itemStack, EntityPlayer entityPlayer) {
        return 1;
    }

    //We don't want these enchantments since Ichor is completely unbreakable
    @Override
    public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack, @NotNull Enchantment enchantment) {
        if (enchantment == Enchantments.MENDING || enchantment == Enchantments.UNBREAKING) return false;
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean getIsRepairable(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
        ItemStack mat = ModItemsKAMI.MATERIAL_ICHORIUM.getRepairItemStack();
        return !mat.isEmpty() && OreDictionary.itemMatches(mat, repair, false);
    }
}
