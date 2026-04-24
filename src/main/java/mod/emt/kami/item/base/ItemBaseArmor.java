package mod.emt.kami.item.base;

import mod.emt.kami.Kami;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ItemBaseArmor extends ItemArmor {
    public String textureName;

    public ItemBaseArmor(String unlocName, ArmorMaterial material, EntityEquipmentSlot equipmentSlot, String textureName) {
        super(material, 0, equipmentSlot);
        this.setRegistryName(Kami.MOD_ID, unlocName);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(Kami.tabKAMI);
        this.textureName = textureName;
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public String getArmorTexture(@NotNull ItemStack stack, @NotNull Entity entity, @NotNull EntityEquipmentSlot slot, @NotNull String type) {
        return Kami.MOD_ID + ":textures/models/armor/" + textureName;
    }
}
