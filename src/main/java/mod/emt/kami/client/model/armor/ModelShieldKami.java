package mod.emt.kami.client.model.armor;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.casters.ICaster;
import thaumcraft.common.items.casters.ItemFocus;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class ModelShieldKami extends ModelShield {
    public ModelRenderer focus;

    public ModelShieldKami() {
        super();
        this.focus = new ModelRenderer(this, 26, 12);
        this.focus.addBox(-1.5f, -8.5f, -1.5f, 3, 3, 1, 0.0f);
    }

    public void render(ItemStack shield) {
        super.render();
        if(shield.getItem() instanceof ICaster) {
            ItemStack focusStack = ((ICaster) shield.getItem()).getFocusStack(shield);
            if(!focusStack.isEmpty() && focusStack.getItem() instanceof ItemFocus) {
                Color color = new Color(((ItemFocus) focusStack.getItem()).getFocusColor(focusStack));
                GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
                this.focus.render(0.0625f);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }
}
