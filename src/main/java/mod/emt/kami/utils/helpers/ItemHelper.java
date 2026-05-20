package mod.emt.kami.utils.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;

public class ItemHelper {
    public static ItemStack setUnbreakable(ItemStack stack) {
        stack.setTagInfo("Unbreakable", new NBTTagByte((byte) 1));
        return stack;
    }
}
