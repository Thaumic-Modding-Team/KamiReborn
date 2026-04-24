package mod.emt.kami.item.base;

import mod.emt.kami.api.item.AbstractItemAddition;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;

public class ItemBase extends AbstractItemAddition {
    String oreDict;

    public ItemBase(String unlocName, String oreDict) {
        super(unlocName);
        this.oreDict = oreDict;
    }

    @Override
    public void registerOreDicts() {
        if (oreDict != null) {
            OreDictionary.registerOre(oreDict, this);
        }
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
