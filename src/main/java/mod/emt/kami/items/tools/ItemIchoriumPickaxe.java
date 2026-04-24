package mod.emt.kami.items.tools;

import mod.emt.kami.Kami;
import mod.emt.kami.registry.ModItemsKAMI;
import mod.emt.kami.utils.helpers.ItemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ItemIchoriumPickaxe extends ItemPickaxe {
    public ItemIchoriumPickaxe() {
        this("ichorium_pickaxe");
    }

    protected ItemIchoriumPickaxe(String unlocName) {
        super(ModItemsKAMI.MATERIAL_ICHORIUM);
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
}
