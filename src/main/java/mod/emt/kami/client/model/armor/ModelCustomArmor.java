package mod.emt.kami.client.model.armor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class ModelCustomArmor extends ModelBiped {
    public ModelCustomArmor(float modelSize, int textureWidth, int textureHeight) {
        super(modelSize, 0.0F, textureWidth, textureHeight);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, @NotNull Entity entity) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);

        // Fixes custom armor sets from being glitched on armor stands
        if(entity instanceof EntityArmorStand) {
            EntityArmorStand stand = (EntityArmorStand) entity;

            float f = 0.017453292F;

            this.bipedHead.rotateAngleX = f * stand.getHeadRotation().getX();
            this.bipedHead.rotateAngleY = f * stand.getHeadRotation().getY();
            this.bipedHead.rotateAngleZ = f * stand.getHeadRotation().getZ();

            this.bipedBody.rotateAngleX = f * stand.getBodyRotation().getX();
            this.bipedBody.rotateAngleY = f * stand.getBodyRotation().getY();
            this.bipedBody.rotateAngleZ = f * stand.getBodyRotation().getZ();

            this.bipedLeftArm.rotateAngleX = f * stand.getLeftArmRotation().getX();
            this.bipedLeftArm.rotateAngleY = f * stand.getLeftArmRotation().getY();
            this.bipedLeftArm.rotateAngleZ = f * stand.getLeftArmRotation().getZ();

            this.bipedRightArm.rotateAngleX = f * stand.getRightArmRotation().getX();
            this.bipedRightArm.rotateAngleY = f * stand.getRightArmRotation().getY();
            this.bipedRightArm.rotateAngleZ = f * stand.getRightArmRotation().getZ();

            this.bipedLeftLeg.rotateAngleX = f * stand.getLeftLegRotation().getX();
            this.bipedLeftLeg.rotateAngleY = f * stand.getLeftLegRotation().getY();
            this.bipedLeftLeg.rotateAngleZ = f * stand.getLeftLegRotation().getZ();

            this.bipedRightLeg.rotateAngleX = f * stand.getRightLegRotation().getX();
            this.bipedRightLeg.rotateAngleY = f * stand.getRightLegRotation().getY();
            this.bipedRightLeg.rotateAngleZ = f * stand.getRightLegRotation().getZ();

            this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
            this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
            this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);

            copyModelAngles(this.bipedHead, this.bipedHeadwear);
        }
    }
}
