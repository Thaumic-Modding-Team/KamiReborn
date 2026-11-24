package mod.emt.kami.inventory.containers;

import mod.emt.kami.inventory.handlers.CelestialTranslocatorHandler;
import mod.emt.kami.items.misc.ItemCelestialPearl;
import mod.emt.kami.items.misc.ItemCelestialTranslocator;
import mod.emt.kami.tiles.TileCelestialGateway;
import mod.emt.kami.utils.misc.SerializablePosition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ContainerCelestialTeleport extends Container {
    private final ItemStack translocator;
    private final CelestialTranslocatorHandler stackHandler;

    public ContainerCelestialTeleport(ItemStack translocator) {
        this.translocator = translocator;
        this.stackHandler = ItemCelestialTranslocator.getTranslocatorHandler(translocator);
    }

    public ItemStack getTranslocator() {
        return translocator;
    }

    public CelestialTranslocatorHandler getStackHandler() {
        return stackHandler;
    }

    @Override
    public boolean enchantItem(@NotNull EntityPlayer playerIn, int id) {
        if(id >= 0 && id < this.stackHandler.getSlots()) {
            ItemStack stack = this.stackHandler.getStackInSlot(id);
            SerializablePosition position = ItemCelestialPearl.getSerializedPosition(stack);
            if(position != null) {
                World world = position.getWorld(true);
                if(world != null) {
                    TileEntity tile = world.getTileEntity(position.getPosition());
                    if(tile instanceof TileCelestialGateway) {
                        ((TileCelestialGateway) tile).teleportToSavedPosition(playerIn);
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean canInteractWith(@NotNull EntityPlayer playerIn) {
        return true;
    }
}
