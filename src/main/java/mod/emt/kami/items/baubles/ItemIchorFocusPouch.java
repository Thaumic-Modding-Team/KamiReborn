package mod.emt.kami.items.baubles;

import mod.emt.kami.Kami;
import mod.emt.kami.client.KeyBindingsKami;
import mod.emt.kami.handlers.GuiHandlerKami;
import mod.emt.kami.inventory.handlers.PouchStackHandler;
import mod.emt.kami.registry.ModSoundsKAMI;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
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

    //This is necessary, otherwise it won't show up on the creative tab
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == Kami.tabKAMI || tab == CreativeTabs.SEARCH) {
            items.add(new ItemStack(this, 1, 0));
        }
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
        PouchStackHandler handler = new PouchStackHandler(stack);
        return handler.getInventory();
    }

    @Override
    public void setInventory(ItemStack stack, NonNullList<ItemStack> stackList) {
        PouchStackHandler handler = new PouchStackHandler(stack);
        handler.setInventory(stackList);
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

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase player)
    {
        player.world.playSound(null, player.posX, player.posY, player.posZ, ModSoundsKAMI.EQUIP_BAUBLE.getSoundEvent(), SoundCategory.PLAYERS, 0.8F, 1.0F);
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase player)
    {
        player.world.playSound(null, player.posX, player.posY, player.posZ, ModSoundsKAMI.UNEQUIP_BAUBLE.getSoundEvent(), SoundCategory.PLAYERS, 0.8F, 1.0F);
    }
}
