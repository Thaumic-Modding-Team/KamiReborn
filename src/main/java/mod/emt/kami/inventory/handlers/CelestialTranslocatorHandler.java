package mod.emt.kami.inventory.handlers;

import mod.emt.kami.items.misc.ItemCelestialPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CelestialTranslocatorHandler extends ItemStackHandler implements ICapabilityProvider {
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

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public @Nullable <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this);
        }
        return null;
    }
}
