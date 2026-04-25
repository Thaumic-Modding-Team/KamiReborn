package mod.emt.kami.proxy;

import mod.emt.kami.registry.ModRecipesKAMI;

public class CommonProxy {
    public void preInit() {

    }

    public void init() {
        ModRecipesKAMI.registerOreDicts();
        registerResearch();
    }

    public void postInit() {

    }

    private void registerResearch() {
        //TODO: Register research.
        //RegistrarKAMI.getAdditions().forEach(IAddition::registerResearchLocation);
    }
}
