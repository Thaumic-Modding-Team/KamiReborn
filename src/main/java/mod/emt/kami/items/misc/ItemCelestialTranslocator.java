package mod.emt.kami.items.misc;

import mod.emt.kami.Kami;
import mod.emt.kami.handlers.GuiHandlerKami;
import mod.emt.kami.inventory.handlers.CelestialTranslocatorHandler;
import mod.emt.kami.items.base.ItemBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemCelestialTranslocator extends ItemBase {
    public ItemCelestialTranslocator() {
        super("celestial_translocator");
        this.setRarity(EnumRarity.EPIC);
        this.setMaxStackSize(1);
    }

    public static CelestialTranslocatorHandler getTranslocatorHandler(ItemStack stack) {
        return new CelestialTranslocatorHandler(stack);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable NBTTagCompound nbt) {
        return getTranslocatorHandler(stack);
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        if (playerIn.isSneaking()) {
            playerIn.openGui(Kami.instance, handIn == EnumHand.MAIN_HAND ? GuiHandlerKami.ID_CELESTIAL_TRANSLOCATOR_MH : GuiHandlerKami.ID_CELESTIAL_TRANSLOCATOR_OH, worldIn, 0, 0, 0);
        } else {
            playerIn.openGui(Kami.instance, handIn == EnumHand.MAIN_HAND ? GuiHandlerKami.ID_CELESTIAL_TELEPORT_MH : GuiHandlerKami.ID_CELESTIAL_TELEPORT_OH, worldIn, 0, 0, 0);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltip.kami.celestial_translocator.info1"));
        tooltip.add(I18n.format("tooltip.kami.celestial_translocator.info2"));
    }
}
