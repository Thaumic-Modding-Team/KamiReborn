package mod.emt.kami.items.misc;

import mod.emt.kami.Kami;
import mod.emt.kami.handlers.GuiHandlerKami;
import mod.emt.kami.inventory.handlers.CelestialTranslocatorHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ItemCelestialTranslocator extends Item {
    public ItemCelestialTranslocator() {
        this.setRegistryName(Kami.MOD_ID, "celestial_translocator");
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(Kami.tabKAMI);
        this.setMaxStackSize(1);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable NBTTagCompound nbt) {
        return super.initCapabilities(stack, nbt);
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        if(handIn == EnumHand.MAIN_HAND) {
            if (playerIn.isSneaking()) {
                playerIn.openGui(Kami.instance, GuiHandlerKami.ID_CELESTIAL_TRANSLOCATOR, worldIn, 0, 0, 0);
                return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
            } else {
                playerIn.openGui(Kami.instance, GuiHandlerKami.ID_CELESTIAL_TELEPORT, worldIn, 0, 0, 0);
                return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, heldStack);
    }

    public static CelestialTranslocatorHandler getPearlHandler(ItemStack stack) {
        return new CelestialTranslocatorHandler(stack);
    }
}
