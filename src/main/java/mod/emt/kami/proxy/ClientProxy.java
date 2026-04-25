package mod.emt.kami.proxy;

import mod.emt.kami.items.IDyeableGear;
import mod.emt.kami.registry.ModItemsKAMI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
        IItemColor itemColorHandler = (stack, tintIndex) -> {
            if (tintIndex == 1 && stack.getItem() instanceof IDyeableGear) {
                return ((IDyeableGear) stack.getItem()).getDyedColor(stack);
            } else {
                return -1;
            }
        };

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemColorHandler, ModItemsKAMI.ICHORWEAVE_BOOTS);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemColorHandler, ModItemsKAMI.ICHORWEAVE_HOOD);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemColorHandler, ModItemsKAMI.ICHORWEAVE_LEGGINGS);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemColorHandler, ModItemsKAMI.ICHORWEAVE_ROBE);
    }

    @Override
    public void postInit() {
        super.postInit();
    }
}
