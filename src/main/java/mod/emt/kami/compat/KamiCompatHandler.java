package mod.emt.kami.compat;

import mod.emt.kami.Kami;
import mod.emt.kami.compat.tinkers.ConstructsArmory;
import mod.emt.kami.compat.tinkers.TinkersConstruct;
import mod.emt.kami.compat.tinkers.TinkersConstructClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

// TODO: Add config options to checks
@Mod.EventBusSubscriber(modid = Kami.MOD_ID)
public class KamiCompatHandler {
    public static void preInit()
    {
        if (Loader.isModLoaded("tconstruct"))
        {
            TinkersConstruct.preInit();
            if (FMLLaunchHandler.side().isClient()) {
                MinecraftForge.EVENT_BUS.register(new TinkersConstructClient());
            }

            // Only load Construct's Armory if Tinkers' Construct is also loaded
            if (Loader.isModLoaded("conarm"))
            {
                ConstructsArmory.preInit();
            }
        }
    }

    public static void init()
    {
        if (Loader.isModLoaded("tconstruct"))
        {
            TinkersConstruct.init();

            // Only load Construct's Armory if Tinkers' Construct is also loaded
            if (Loader.isModLoaded("conarm"))
            {
                ConstructsArmory.init();
            }
        }
    }

    public static void postInit()
    {
        if (Loader.isModLoaded("tconstruct"))
        {
            TinkersConstruct.postInit();
        }
    }
}
