package mod.emt.kami.client.model.armor;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

public class ModelIchorArmor extends ModelCustomArmor {
    public ModelIchorArmor(float modelSize) {
        super(modelSize, 64, 32);
    }

    @Override
    public void render(@NotNull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.disableLighting();
        GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(1.0F, 1.0F, 1.0F, 1.0F));

        float pulse = (MathHelper.sin(ageInTicks * 0.1F) + 1.0F) / 2.0F;
        float red = 1.0F;
        float green = 0.8F + (pulse * 0.2F);
        float blue = 0.1F + (pulse * 0.9F);

        GlStateManager.color(red, green, blue, 1.0F);

        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        if (GL11.glIsEnabled(GL11.GL_LIGHTING)) GlStateManager.enableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, OpenGlHelper.lastBrightnessX, OpenGlHelper.lastBrightnessY);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
