package mod.emt.kami.inventory.handlers;

import mod.emt.kami.items.misc.ItemCelestialPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CelestialTranslocatorHandler extends ItemStackHandler implements ICapabilityProvider {
    public final ItemStack translocator;

    public CelestialTranslocatorHandler(ItemStack translocator) {
        super(10);
        this.translocator = translocator;
        if(translocator.getTagCompound() != null && translocator.getTagCompound().hasKey("inventory")) {
            this.deserializeNBT(translocator.getTagCompound().getCompoundTag("inventory"));
        }
    }

    public ItemStack getTranslocator() {
        return this.translocator;
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
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    protected int getStackLimit(int slot, @NotNull ItemStack stack) {
        return 1;
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.translocator.setTagInfo("inventory", this.serializeNBT());
    }

    public List<Tuple<ItemStack, Integer>> getPearlSlots() {
        List<Tuple<ItemStack, Integer>> pearlSlots = new ArrayList<>();
        for(int i = 0; i < this.getSlots(); i++) {
            ItemStack stack = this.getStackInSlot(i);
            if(ItemCelestialPearl.isPearlAndLinked(stack)) {
                pearlSlots.add(new Tuple<>(stack, i));
            }
        }
        return pearlSlots;
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
