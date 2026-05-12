package mod.emt.kami.compat.tinkers;

import mod.emt.kami.registry.RegistrarKAMI;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TinkersConstructClient {
    @SubscribeEvent
    public void onModelRegistry(ModelRegistryEvent event) {
        RegistrarKAMI.registerFluidRenderer(TinkersConstruct.ICHORIUM_FLUID);
    }
}
