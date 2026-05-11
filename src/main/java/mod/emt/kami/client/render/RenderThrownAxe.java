package mod.emt.kami.client.render;

import mod.emt.kami.entities.EntityThrownAxe;
import mod.emt.kami.registry.ModItemsKAMI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderThrownAxe extends Render<EntityThrownAxe> {
    private static final ItemStack AXE = new ItemStack(ModItemsKAMI.AWAKENED_ICHORIUM_AXE);
    private final RenderItem itemRenderer;

    public RenderThrownAxe(RenderManager renderManager, RenderItem itemRenderer)
    {
        super(renderManager);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void doRender(EntityThrownAxe entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + (entity.rotationYaw < 0 ? -90.0F : 90.0F), 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        if (renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(16748812);
        }

        itemRenderer.renderItem(AXE, ItemCameraTransforms.TransformType.GROUND);

        if (renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityThrownAxe entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    public static class Factory implements IRenderFactory<EntityThrownAxe>
    {
        @Override
        public Render<? super EntityThrownAxe> createRenderFor(RenderManager manager)
        {
            return new RenderThrownAxe(manager, Minecraft.getMinecraft().getRenderItem());
        }
    }
}
