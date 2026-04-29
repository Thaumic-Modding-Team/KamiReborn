package mod.emt.kami.items.base;

import mod.emt.kami.api.item.IOreDictProvider;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public class ItemBaseEnchanted extends ItemBase implements IOreDictProvider {
    public ItemBaseEnchanted(String unlocName, String oreDict) {
        super(unlocName, oreDict);
        this.setRarity(EnumRarity.EPIC);
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(@NotNull ItemStack stack) {
        return true;
    }
}
