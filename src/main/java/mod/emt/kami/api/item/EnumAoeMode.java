package mod.emt.kami.api.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

public enum EnumAoeMode {
    ONE(1, TextFormatting.WHITE),
    THREE(3, TextFormatting.BLUE),
    FIVE(5, TextFormatting.DARK_GREEN),
    SEVEN(7, TextFormatting.DARK_RED);

    private final int breakAreaSize;
    private final TextFormatting textColor;

    EnumAoeMode(int breakAreaSize, TextFormatting textColor) {
        this.breakAreaSize = breakAreaSize;
        this.textColor = textColor;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public int getBreakAreaSize() {
        return this.breakAreaSize;
    }

    public TextFormatting getTextColor() {
        return this.textColor;
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
