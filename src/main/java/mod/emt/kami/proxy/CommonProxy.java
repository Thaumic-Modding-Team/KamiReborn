package mod.emt.kami.proxy;

import mod.emt.kami.Kami;
import mod.emt.kami.compat.KamiCompatHandler;
import mod.emt.kami.handlers.GuiHandlerKami;
import mod.emt.kami.network.PacketHandler;
import mod.emt.kami.registry.ModRecipesKAMI;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;

public class CommonProxy {
    public void preInit() {
        PacketHandler.init();
        KamiCompatHandler.preInit();
    }

    public void init() {
        ModRecipesKAMI.registerOreDicts();
        registerResearch();
        KamiCompatHandler.init();
    }

    public void postInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(Kami.instance, new GuiHandlerKami());
    }

    private void registerResearch() {
        //TODO: Register research.
        //RegistrarKAMI.getAdditions().forEach(IAddition::registerResearchLocation);
        ResearchCategories.registerCategory(
                "ZENITH", "FIRSTSTEPS", new AspectList(),
                new ResourceLocation(Kami.MOD_ID, "textures/research/r_zenith.png"),
                new ResourceLocation(Kami.MOD_ID, "textures/gui/research_background.jpg"),
                new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_over.png"));

        ThaumcraftApi.registerResearchLocation(new ResourceLocation(Kami.MOD_ID, "research/basics"));
        ThaumcraftApi.registerResearchLocation(new ResourceLocation(Kami.MOD_ID, "research/zenith"));

        // TODO: Add config setting for this
        ThaumcraftApi.registerResearchLocation(new ResourceLocation(Kami.MOD_ID, "research/eternal_infusion"));
    }
}
