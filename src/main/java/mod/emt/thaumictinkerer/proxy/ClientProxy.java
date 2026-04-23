package mod.emt.thaumictinkerer.proxy;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();
        //RegistrarKAMI.getProxyAdditions().forEach(IProxy::preInitClient);
    }

    @Override
    public void init() {
        super.init();
        //RegistrarKAMI.getProxyAdditions().forEach(IProxy::initClient);
    }

    @Override
    public void postInit() {
        super.postInit();
        //RegistrarKAMI.getProxyAdditions().forEach(IProxy::postInitClient);
    }
}
