package mod.emt.kami.proxy;

public class CommonProxy {
    public void preInit() {
        //RegistrarKAMI.getProxyAdditions().forEach(IProxy::preInit);
    }

    public void init() {
        //RegistrarKAMI.getProxyAdditions().forEach(IProxy::init);
        registerResearch();
    }

    public void postInit() {
        //RegistrarKAMI.getProxyAdditions().forEach(IProxy::postInit);
    }

    private void registerResearch() {
        //TODO: Register research.
        //RegistrarKAMI.getAdditions().forEach(IAddition::registerResearchLocation);
    }
}
