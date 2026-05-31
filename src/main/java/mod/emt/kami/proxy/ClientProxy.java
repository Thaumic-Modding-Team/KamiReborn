package mod.emt.kami.proxy;

import mod.emt.kami.client.KeyBindingsKami;
import mod.emt.kami.compat.KamiCompatHandler;
import mod.emt.kami.compat.tinkers.TinkersConstructClient;
import mod.emt.kami.config.ConfigHandlerKami;
import mod.emt.kami.items.IDyeableGear;
import mod.emt.kami.registry.ModItemsKAMI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import thaumcraft.api.casters.ICaster;
import thaumcraft.common.items.casters.ItemFocus;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();

        if (KamiCompatHandler.isTinkersConstructLoaded && ConfigHandlerKami.integrations.tinkersConstruct) {
            if (FMLLaunchHandler.side().isClient()) {
                MinecraftForge.EVENT_BUS.register(new TinkersConstructClient());
            }
        }
    }

    @Override
    public void init() {
        super.init();
        KeyBindingsKami.init();
        IItemColor itemColorHandler = (stack, tintIndex) -> {
            if (tintIndex == 1 && stack.getItem() instanceof IDyeableGear) {
                return ((IDyeableGear) stack.getItem()).getDyedColor(stack);
            } else {
                return -1;
            }
        };

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemColorHandler, ModItemsKAMI.AWAKENED_ICHORWEAVE_BOOTS);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemColorHandler, ModItemsKAMI.AWAKENED_ICHORWEAVE_HOOD);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemColorHandler, ModItemsKAMI.AWAKENED_ICHORWEAVE_LEGGINGS);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemColorHandler, ModItemsKAMI.AWAKENED_ICHORWEAVE_ROBE);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemColorHandler, ModItemsKAMI.ICHORWEAVE_BOOTS);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemColorHandler, ModItemsKAMI.ICHORWEAVE_HOOD);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemColorHandler, ModItemsKAMI.ICHORWEAVE_LEGGINGS);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemColorHandler, ModItemsKAMI.ICHORWEAVE_ROBE);

        IItemColor gauntletColorHandler = (stack, tintIndex) -> {
            if (tintIndex == 1 && stack.getItem() instanceof ICaster && ((ICaster) stack.getItem()).getFocus(stack) != null)
                return ((ItemFocus) ((ICaster) stack.getItem()).getFocus(stack)).getFocusColor(((ICaster) stack.getItem()).getFocusStack(stack));
            else if (tintIndex == 2 && stack.getItem() instanceof IDyeableGear) {
                return ((IDyeableGear) stack.getItem()).getDyedColor(stack);
            }

            return -1;
        };

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(gauntletColorHandler, ModItemsKAMI.ICHORIUM_CASTER);
        //ItemIchoriumCaster.initClient(ModItemsKAMI.ICHORIUM_CASTER);
    }

    @Override
    public void postInit() {
        super.postInit();
    }
}
