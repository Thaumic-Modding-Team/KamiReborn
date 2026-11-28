package mod.emt.kami.client.render;

import mod.emt.kami.Kami;
import mod.emt.kami.client.model.armor.ModelShieldKami;
import mod.emt.kami.registry.ModItemsKAMI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class KamiItemTEISR extends TileEntityItemStackRenderer {
    public static final ResourceLocation AWAKENED_ICHORWEAVE_SHIELD_TEXTURE = new ResourceLocation(Kami.MOD_ID, "textures/models/awakened_ichorweave_shield.png");
    public static final ResourceLocation ICHORWEAVE_SHIELD_TEXTURE = new ResourceLocation(Kami.MOD_ID, "textures/models/ichorweave_shield.png");

    private final ModelShieldKami modelShield = new ModelShieldKami();

    @Override
    public void renderByItem(@NotNull ItemStack stack, float partialTicks) {
        if(stack.getItem() == ModItemsKAMI.AWAKENED_ICHORWEAVE_SHIELD) {
            this.renderShield(stack, AWAKENED_ICHORWEAVE_SHIELD_TEXTURE);
        } else if(stack.getItem() == ModItemsKAMI.ICHORWEAVE_SHIELD) {
            this.renderShield(stack, ICHORWEAVE_SHIELD_TEXTURE);
        }
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack) {
        this.renderByItem(stack, 1.0f);
    }

    private void renderShield(ItemStack stack, ResourceLocation texture) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        this.modelShield.render(stack);
        GlStateManager.popMatrix();
    }
}
