package mod.emt.kami.client.element;

import mod.emt.kami.Kami;
import mod.emt.kami.items.misc.ItemCelestialPearl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiButtonCelestialPearl extends GuiButton {
    public static final ResourceLocation PEARL_TEXTURE = new ResourceLocation(Kami.MOD_ID, "textures/items/celestial_pearl.png");
    private ItemStack pearl;
    private int handlerSlot;

    public GuiButtonCelestialPearl(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    public GuiButtonCelestialPearl(int buttonId, int x, int y, ItemStack pearl, int handlerSlot) {
        super(buttonId, x, y, 16, 16, "");
        this.pearl = pearl;
        this.handlerSlot = handlerSlot;
    }

    public ItemStack getCelestialPearl() {
        return pearl;
    }

    public int getHandlerSlot() {
        return handlerSlot;
    }

    public void setCelestialPearl(ItemStack pearl, int handlerSlot) {
        this.pearl = pearl;
        this.handlerSlot = handlerSlot;
    }

    public void updateEnabled() {
        this.visible = !this.pearl.isEmpty() && this.handlerSlot >= 0;
        this.enabled = this.visible;
    }

    @Override
    public void drawButton(@NotNull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        this.updateEnabled();
        if(this.visible && this.enabled) {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

            mc.getTextureManager().bindTexture(PEARL_TEXTURE);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            mc.getRenderItem().renderItemAndEffectIntoGUI(this.pearl, this.x, this.y);

//            if(this.hovered) {
//                GlStateManager.pushMatrix();
//                GlStateManager.scale(1.125, 1.125, 1.125);
//                mc.getRenderItem().renderItemAndEffectIntoGUI(this.pearl, this.x - 1, this.y - 1);
//                GlStateManager.popMatrix();
//            } else {
//                mc.getRenderItem().renderItemAndEffectIntoGUI(this.pearl, this.x, this.y);
//            }

            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

    public List<String> getTooltip() {
        List<String> tooltip = new ArrayList<>();
        if(!this.pearl.isEmpty() && this.pearl.getItem() instanceof ItemCelestialPearl) {
            ((ItemCelestialPearl) this.pearl.getItem()).appendTooltipInfo(this.pearl, tooltip);
        }
        return tooltip;
    }
}
