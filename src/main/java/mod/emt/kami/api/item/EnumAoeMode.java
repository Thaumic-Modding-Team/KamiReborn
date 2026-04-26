package mod.emt.kami.api.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.math.MathHelper;

public enum EnumAoeMode {
    ONE(1),
    THREE(3),
    FIVE(5),
    SEVEN(7);

    private final int breakAreaSize;

    EnumAoeMode(int breakAreaSize) {
        this.breakAreaSize = breakAreaSize;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public int getBreakAreaSize() {
        return this.breakAreaSize;
    }

    public EnumAoeMode nextMode() {
        EnumAoeMode[] values = EnumAoeMode.values();
        return values[(this.ordinal() + 1) % values.length];
    }

    public static EnumAoeMode getAoeMode(ItemStack stack) {
        EnumAoeMode[] values = EnumAoeMode.values();
        int ordinal = stack.getTagCompound() != null ? stack.getTagCompound().getInteger("mode") : 0;
        ordinal = MathHelper.clamp(ordinal, 0, values.length - 1);
        return values[ordinal];
    }

    public static void setAoeMode(ItemStack stack, EnumAoeMode mode) {
        stack.setTagInfo("mode", new NBTTagInt(mode.ordinal()));
    }
}
