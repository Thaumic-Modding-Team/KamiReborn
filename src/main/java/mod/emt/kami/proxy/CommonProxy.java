package mod.emt.kami.proxy;

import mod.emt.kami.Kami;
import mod.emt.kami.handlers.GuiHandlerKami;
import mod.emt.kami.network.PacketHandler;
import mod.emt.kami.registry.ModRecipesKAMI;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
    public void preInit() {
        PacketHandler.init();
    }

    public void init() {
        ModRecipesKAMI.registerOreDicts();
        registerResearch();
    }

    public void postInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(Kami.instance, new GuiHandlerKami());
    }

    private void registerResearch() {
        //TODO: Register research.
        //RegistrarKAMI.getAdditions().forEach(IAddition::registerResearchLocation);
    }
}
