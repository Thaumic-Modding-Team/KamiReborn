package mod.emt.kami.items.baubles;

import mod.emt.kami.Kami;
import mod.emt.kami.client.KeyBindingsKami;
import mod.emt.kami.handlers.GuiHandlerKami;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;
import thaumcraft.common.items.casters.ItemFocusPouch;

import java.util.List;
import java.util.Objects;

public class ItemIchorFocusPouch extends ItemFocusPouch {
    public ItemIchorFocusPouch() {
        super();
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(Kami.tabKAMI);
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack heldStack = playerIn.getHeldItem(hand);
        if(!worldIn.isRemote && hand == EnumHand.MAIN_HAND) {
            playerIn.openGui(Kami.instance, GuiHandlerKami.ID_ICHORWEAVE_POUCH, worldIn, 0, 0, 0);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
    }

    @Override
    public NonNullList<ItemStack> getInventory(ItemStack stack) {
        NonNullList<ItemStack> inventory = NonNullList.withSize(117, ItemStack.EMPTY);
        if(stack.getTagCompound() != null) {
            ItemStackHelper.loadAllItems(stack.getTagCompound(), inventory);
        }
        return inventory;
    }

    @Override
    public void setInventory(ItemStack stack, NonNullList<ItemStack> stackList) {
        if(stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        ItemStackHelper.saveAllItems(stack.getTagCompound(), stackList);
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        int keyId = KeyBindingsKami.openFocusPouch.getKeyCode();
        if(keyId >= 0) {
            tooltip.add(I18n.format("tooltip.kami.focus_pouch.keybind", Keyboard.getKeyName(keyId)));
        }
    }
}
