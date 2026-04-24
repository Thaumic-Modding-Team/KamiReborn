package mod.emt.kami.client.model.armor;

import mod.emt.kami.Kami;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public class ModelIchorArmor extends ModelCustomArmor {
    protected static final String TEXTURE_PATH_DYED_OVERLAY_1 = new ResourceLocation(Kami.MOD_ID, "textures/models/armor/ichorweave_layer_1_dyed_overlay.png").toString();
    protected static final String TEXTURE_PATH_DYED_OVERLAY_2 = new ResourceLocation(Kami.MOD_ID, "textures/models/armor/ichorweave_layer_2_dyed_overlay.png").toString();

    public ModelIchorArmor(float modelSize) {
        super(modelSize, 64, 32);
    }

    @Override
    public void render(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

        float pulse = (MathHelper.sin(ageInTicks * 0.1F) + 1.0F) / 2.0F;
        float red = 1.0F;
        float green = 0.8F + (pulse * 0.2F);
        float blue = 0.1F + (pulse * 0.9F);

        GlStateManager.color(red, green, blue, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(this.bipedLeftLeg.showModel ? TEXTURE_PATH_DYED_OVERLAY_2 : TEXTURE_PATH_DYED_OVERLAY_1));

        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, OpenGlHelper.lastBrightnessX, OpenGlHelper.lastBrightnessY);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
