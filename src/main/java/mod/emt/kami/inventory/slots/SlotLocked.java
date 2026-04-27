package mod.emt.kami.inventory.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotLocked extends Slot {
    public SlotLocked(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean canTakeStack(@NotNull EntityPlayer playerIn) {
        return false;
    }

    @Override
    public boolean isItemValid(@NotNull ItemStack stack) {
        return false;
    }
}
