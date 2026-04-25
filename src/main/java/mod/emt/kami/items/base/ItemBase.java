package mod.emt.kami.items.base;

import mod.emt.kami.Kami;
import mod.emt.kami.api.item.IOreDictProvider;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ItemBase extends Item implements IOreDictProvider {
    private IRarity rarity;
    private String oreDict;

    public ItemBase(String unlocName, String oreDict) {
        this.setRegistryName(Kami.MOD_ID, unlocName);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(Kami.tabKAMI);
        this.setRarity(EnumRarity.EPIC);
        this.oreDict = oreDict;
    }

    public Item setRarity(@NotNull IRarity rarity) {
        this.rarity = rarity;
        return this;
    }

    @Override
    public void registerOreDicts() {
        if (oreDict != null) {
        }
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return this.rarity;
    }
}
