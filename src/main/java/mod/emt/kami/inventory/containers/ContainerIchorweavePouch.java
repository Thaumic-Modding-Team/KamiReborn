package mod.emt.kami.inventory.containers;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import mod.emt.kami.inventory.handlers.PouchStackHandler;
import mod.emt.kami.inventory.slots.SlotLocked;
import mod.emt.kami.registry.ModItemsKAMI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ContainerIchorweavePouch extends Container {
    private final ItemStack pouchStack;

    public ContainerIchorweavePouch(EntityPlayer player, ItemStack pouchStack) {
        this.pouchStack = pouchStack;
        this.bindPouchInventory();
        this.bindPlayerInventory(player.inventory);
    }

    public ContainerIchorweavePouch(EntityPlayer player) {
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        int slot = BaublesApi.isBaubleEquipped(player, ModItemsKAMI.FOCUS_POUCH);
        if(slot > -1) {
            this.pouchStack = handler.getStackInSlot(slot);
            this.bindPouchInventory();
            this.bindPlayerInventory(player.inventory);
        } else {
            throw new RuntimeException("Attempted to access Ichorweave Focus Pouch Gui while pouch is not in baubles slot.");
        }
    }

    public void bindPouchInventory() {
        PouchStackHandler handler = new PouchStackHandler(this.pouchStack);
        for(int slot = 0; slot < handler.getSlots(); slot++) {
            int xIndex = slot % 13;
            int yIndex = slot / 13;
            this.addSlotToContainer(new SlotItemHandler(handler, slot, xIndex * 18 + 12, yIndex * 18 + 8));
        }
    }

    public void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 48 + j * 18, 177 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            if (i == inventoryPlayer.currentItem) {
                this.addSlotToContainer(new SlotLocked(inventoryPlayer, i, 48 + i * 18, 235));
            } else {
                this.addSlotToContainer(new Slot(inventoryPlayer, i, 48 + i * 18, 235));
            }
        }
    }

    @Override
    public boolean canInteractWith(@NotNull EntityPlayer playerIn) {
        return true;
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();

            if (index < 117) {
                if (!this.mergeItemStack(slotStack, 117, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(slotStack, 0, 117, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return stack;
    }
}
