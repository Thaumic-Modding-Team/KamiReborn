package mod.emt.kami.client.render;

import mod.emt.kami.entities.EntityElementalFireBottle;
import mod.emt.kami.registry.ModItemsKAMI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class RenderElementalFireBottle extends RenderSnowball<EntityElementalFireBottle> {
    public RenderElementalFireBottle(RenderManager renderManagerIn) {
        super(renderManagerIn, ModItemsKAMI.ELEMENTAL_FIRE_BOTTLE, Minecraft.getMinecraft().getRenderItem());
    }

    @Override
    public @NotNull ItemStack getStackToRender(EntityElementalFireBottle entityIn) {
        return entityIn.getItem();
    }
}
