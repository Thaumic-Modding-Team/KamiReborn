package mod.emt.kami.inventory.containers;

import mod.emt.kami.inventory.handlers.CelestialTranslocatorHandler;
import mod.emt.kami.inventory.slots.SlotLocked;
import mod.emt.kami.items.misc.ItemCelestialPearl;
import mod.emt.kami.items.misc.ItemCelestialTranslocator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ContainerCelestialTranslocator extends Container {
    private final EntityPlayer player;
    private final CelestialTranslocatorHandler handler;

    public ContainerCelestialTranslocator(EntityPlayer player, ItemStack translocator) {
        this.player = player;
        this.handler = ItemCelestialTranslocator.getPearlHandler(translocator);
        this.bindTranslocatorInventory();
        this.bindPlayerInventory();
    }

    public void bindTranslocatorInventory() {
        for(int i = 0; i < this.handler.getSlots(); i++) {
            this.addSlotToContainer(new SlotItemHandler(this.handler, i, 30 + (i % 5) * 25, 20 + (i / 5) * 25));
        }
    }

    public void bindPlayerInventory() {
        InventoryPlayer inventory = player.inventory;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            if (i == inventory.currentItem) {
                this.addSlotToContainer(new SlotLocked(inventory, i, 8 + i * 18, 142));
            } else {
                this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
            }
        }
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();

            int translocatorSize = this.handler.getSlots();
            if(index < translocatorSize) {
                //Moving from attractor to player
                if(!this.mergeItemStack(slotStack, translocatorSize, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                //Move from player to attractor
                if(ItemCelestialPearl.isPearlAndLinked(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 0, translocatorSize, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if(index < this.inventorySlots.size() - 9) {
                    if(!this.mergeItemStack(slotStack, this.inventorySlots.size() - 9, this.inventorySlots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if(!this.mergeItemStack(slotStack, translocatorSize, this.inventorySlots.size() - 9, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if(slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return stack;
    }

    @Override
    public boolean canInteractWith(@NotNull EntityPlayer playerIn) {
        return true;
    }
}
