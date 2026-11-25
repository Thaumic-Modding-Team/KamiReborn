package mod.emt.kami.inventory.containers;

import mod.emt.kami.inventory.handlers.CelestialTranslocatorHandler;
import mod.emt.kami.items.misc.ItemCelestialPearl;
import mod.emt.kami.items.misc.ItemCelestialTranslocator;
import mod.emt.kami.tiles.TileCelestialGateway;
import mod.emt.kami.utils.misc.SerializablePosition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.SoundsTC;

public class ContainerCelestialTeleport extends Container {
    private final ItemStack translocator;
    private final CelestialTranslocatorHandler stackHandler;
    private final EntityEquipmentSlot hand;

    public ContainerCelestialTeleport(ItemStack translocator, EntityEquipmentSlot hand) {
        this.translocator = translocator;
        this.stackHandler = ItemCelestialTranslocator.getTranslocatorHandler(translocator);
        this.hand = hand;
    }

    public ItemStack getTranslocator() {
        return translocator;
    }

    public CelestialTranslocatorHandler getStackHandler() {
        return stackHandler;
    }

    public EntityEquipmentSlot getHand() {
        return hand;
    }

    @Override
    public boolean enchantItem(@NotNull EntityPlayer playerIn, int id) {
        if(id >= 0 && id < this.stackHandler.getSlots()) {
            ItemStack stack = this.stackHandler.getStackInSlot(id);
            SerializablePosition targetPos = ItemCelestialPearl.getSerializedPosition(stack);
            World targetWorld = targetPos.getWorld(true);
            if(!targetPos.isEmpty() && targetWorld != null) {
                TileEntity tile = targetWorld.getTileEntity(targetPos.getPosition());
                if(tile instanceof TileCelestialGateway) {
                    ((TileCelestialGateway) tile).teleportToGateway(playerIn);
                } else {
                    playerIn.world.playSound(null, playerIn.getPosition(), SoundsTC.craftfail, SoundCategory.PLAYERS, 1.0f, 0.6f);
                    playerIn.sendStatusMessage(new TextComponentTranslation("chat.kami.celestial_pearl.missing_gateway").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)), false);
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
