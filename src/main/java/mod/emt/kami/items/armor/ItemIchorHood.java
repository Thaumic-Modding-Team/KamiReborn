package mod.emt.kami.items.armor;

import mod.emt.kami.Kami;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.items.IGoggles;
import thaumcraft.api.items.IRevealer;

import javax.annotation.Nullable;
import java.util.List;

public class ItemIchorHood extends ItemIchorArmor implements IGoggles {
    public ItemIchorHood(String unlocName, ArmorMaterial material, EntityEquipmentSlot equipmentSlot) {
        super(unlocName, material, equipmentSlot);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> list, @NotNull ITooltipFlag tooltip) {
        super.addInformation(stack, world, list, tooltip);
        list.add(TextFormatting.DARK_PURPLE + new TextComponentTranslation("tooltip." + Kami.MOD_ID + ".revealing").getFormattedText());
    }

    @Override
    public boolean showIngamePopups(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return true;
    }
}
