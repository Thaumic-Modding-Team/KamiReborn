package mod.emt.kami;

import mod.emt.kami.proxy.CommonProxy;
import mod.emt.kami.registry.CreativeTabsKAMI;
import mod.emt.kami.utils.helpers.LogHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = Kami.MOD_ID,
        name = Kami.MOD_NAME,
        version = Kami.MOD_VERSION,
        dependencies = Kami.DEPENDENCIES
)
public class Kami {
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String MOD_NAME = Tags.MOD_NAME;
    public static final String MOD_VERSION = Tags.VERSION;
    public static final String DEPENDENCIES = "required-after:thaumcraft;";

    public static final String CLIENT_PROXY = "mod.emt.kami.proxy.ClientProxy";
    public static final String COMMON_PROXY = "mod.emt.kami.proxy.CommonProxy";

    public static final CreativeTabs tabKAMI = new CreativeTabsKAMI(CreativeTabs.CREATIVE_TAB_ARRAY.length, "KamiTab");

    @Mod.Instance(MOD_ID)
    public static Kami instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LogHelper.info("Starting " + MOD_NAME);
        proxy.preInit();
        LogHelper.debug("Finished preInit phase.");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        LogHelper.debug("Finished init phase.");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
        LogHelper.debug("Finished postInit phase.");
    }
}
