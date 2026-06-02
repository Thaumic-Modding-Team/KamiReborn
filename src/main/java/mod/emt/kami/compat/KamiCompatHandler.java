package mod.emt.kami.compat;

import mod.emt.kami.api.IProxy;
import mod.emt.kami.compat.tinkers.ConstructsArmory;
import mod.emt.kami.compat.tinkers.TinkersConstruct;
import mod.emt.kami.config.ConfigHandlerKami;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class KamiCompatHandler {
    public static final boolean isConstructsArmoryLoaded = Loader.isModLoaded("conarm");
    public static final boolean isTinkersConstructLoaded = Loader.isModLoaded("tconstruct");

    private static final List<IProxy> MODULES = new ArrayList<>();

    private static void initCompatModules() {
        if(isTinkersConstructLoaded && ConfigHandlerKami.integrations.tinkersConstruct) {
            MODULES.add(new TinkersConstruct());
            if (isConstructsArmoryLoaded && ConfigHandlerKami.integrations.constructsArmory) {
                MODULES.add(new ConstructsArmory());
            }
        }
    }

    public static void preInit() {
        initCompatModules();
        MODULES.forEach(IProxy::preInit);
    }

    public static void init() {
        MODULES.forEach(IProxy::init);
    }

    public static void postInit() {
        MODULES.forEach(IProxy::postInit);
    }

    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        MODULES.forEach(IProxy::preInitClient);
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        MODULES.forEach(IProxy::initClient);
    }

    @SideOnly(Side.CLIENT)
    public static void postInitClient() {
        MODULES.forEach(IProxy::postInitClient);
    }
}
