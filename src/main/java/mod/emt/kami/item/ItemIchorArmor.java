package mod.emt.kami.item;

import mod.emt.kami.api.item.IItemAddition;
import mod.emt.kami.client.model.armor.ModelIchorArmor;
import mod.emt.kami.item.base.ItemBaseArmor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public class ItemIchorArmor extends ItemBaseArmor implements IItemAddition {
    public static final ModelIchorArmor ARMOR_OUTER = new ModelIchorArmor(1.0F);
    public static final ModelIchorArmor ARMOR_INNER = new ModelIchorArmor(0.5F);

    public ItemIchorArmor(String unlocName, ArmorMaterial material, EntityEquipmentSlot equipmentSlot, String textureName) {
        super(unlocName, material, equipmentSlot, textureName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(@NotNull EntityLivingBase entity, @NotNull ItemStack stack, @NotNull EntityEquipmentSlot slot, @NotNull ModelBiped bipedModel) {
        ModelIchorArmor armorModel = (slot == EntityEquipmentSlot.LEGS) ? ARMOR_INNER : ARMOR_OUTER;

        if (armorModel != null) {
            armorModel.setModelAttributes(bipedModel);

            armorModel.bipedHead.showModel = (slot == EntityEquipmentSlot.HEAD);
            armorModel.bipedBody.showModel = (slot == EntityEquipmentSlot.CHEST);
            armorModel.bipedRightArm.showModel = (slot == EntityEquipmentSlot.CHEST);
            armorModel.bipedLeftArm.showModel = (slot == EntityEquipmentSlot.CHEST);

            armorModel.bipedRightLeg.showModel = (slot == EntityEquipmentSlot.LEGS || slot == EntityEquipmentSlot.FEET);
            armorModel.bipedLeftLeg.showModel = (slot == EntityEquipmentSlot.LEGS || slot == EntityEquipmentSlot.FEET);

            return armorModel;
        }

        return bipedModel;
    }
}
