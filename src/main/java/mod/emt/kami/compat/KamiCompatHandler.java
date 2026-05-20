package mod.emt.kami.compat;

import mod.emt.kami.compat.tinkers.ConstructsArmory;
import mod.emt.kami.compat.tinkers.TinkersConstruct;
import mod.emt.kami.compat.tinkers.TinkersConstructClient;
import mod.emt.kami.config.ConfigHandlerKami;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

public class KamiCompatHandler {
    public static final boolean isConstructsArmoryLoaded = Loader.isModLoaded("conarm");
    public static final boolean isTinkersConstructLoaded = Loader.isModLoaded("tconstruct");

    public static void preInit() {
        if (isTinkersConstructLoaded && ConfigHandlerKami.integrations.tinkersConstruct) {
            TinkersConstruct.preInit();
            if (FMLLaunchHandler.side().isClient()) {
                MinecraftForge.EVENT_BUS.register(new TinkersConstructClient());
            }

            // Only load Construct's Armory if Tinkers' Construct is also loaded
            if (isConstructsArmoryLoaded && ConfigHandlerKami.integrations.constructsArmory) {
                ConstructsArmory.preInit();
            }
        }
    }

    public static void init()
    {
        if (isTinkersConstructLoaded && ConfigHandlerKami.integrations.tinkersConstruct) {
            TinkersConstruct.init();

            // Only load Construct's Armory if Tinkers' Construct is also loaded
            if (isConstructsArmoryLoaded && ConfigHandlerKami.integrations.constructsArmory) {
                ConstructsArmory.init();
            }
        }
    }

    public static void postInit()
    {
        if (isTinkersConstructLoaded && ConfigHandlerKami.integrations.tinkersConstruct)
        {
            TinkersConstruct.postInit();
        }
    }
}
