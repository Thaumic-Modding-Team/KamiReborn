package mod.emt.kami.api.item;

import mod.emt.kami.Kami;
import net.minecraft.item.Item;

import java.util.Objects;

/**
 * A base class for adding items. Automatically handles item registration.
 */
public abstract class AbstractItemAddition extends Item implements IItemAddition {
    public AbstractItemAddition(String unlocName) {
        this.setRegistryName(Kami.MOD_ID, unlocName);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(Kami.tabKAMI);
    }
}
