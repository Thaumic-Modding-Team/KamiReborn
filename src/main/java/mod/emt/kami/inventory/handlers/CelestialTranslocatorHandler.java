package mod.emt.kami.inventory.handlers;

import mod.emt.kami.items.misc.ItemCelestialPearl;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class CelestialTranslocatorHandler extends ItemStackHandler {
    public final ItemStack stack;

    public CelestialTranslocatorHandler(ItemStack translocator) {
        super(10);
        this.stack = translocator;
        if(translocator.getTagCompound() != null && translocator.getTagCompound().hasKey("inventory")) {
            this.deserializeNBT(translocator.getTagCompound().getCompoundTag("inventory"));
        }
    }

    public ItemStack getTranslocator() {
        return this.stack;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return this.isItemValid(slot, stack) ? super.insertItem(slot, stack, simulate) : stack;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return ItemCelestialPearl.isPearlAndLinked(stack);
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.stack.setTagInfo("inventory", this.serializeNBT());
    }
}
