package mod.emt.kami.inventory.handlers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.items.casters.ItemFocusPouch;

public class PouchStackHandler extends ItemStackHandler implements ICapabilitySerializable<NBTTagCompound> {
    public ItemStack pouchStack;

    public PouchStackHandler(ItemStack pouchStack) {
        super(117);
        this.pouchStack = pouchStack;
        NBTTagCompound invTag = this.pouchStack.getTagCompound() != null ? this.pouchStack.getTagCompound().getCompoundTag("inventory") : new NBTTagCompound();
        this.deserializeNBT(invTag);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return !(stack.getItem() instanceof ItemFocusPouch);
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.pouchStack.setTagInfo("inventory", this.serializeNBT());
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
