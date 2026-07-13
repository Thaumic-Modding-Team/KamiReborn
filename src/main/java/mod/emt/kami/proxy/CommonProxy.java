package mod.emt.kami.proxy;

import com.invadermonky.thaumicapi.utils.libs.ModIds;
import mod.emt.kami.Kami;
import mod.emt.kami.compat.KamiCompatHandler;
import mod.emt.kami.compat.datafixers.BlockDataFixer;
import mod.emt.kami.compat.datafixers.ItemDataFixer;
import mod.emt.kami.compat.datafixers.ItemMetaDataFixer;
import mod.emt.kami.config.ConfigHandlerKami;
import mod.emt.kami.handlers.GuiHandlerKami;
import mod.emt.kami.network.PacketHandler;
import mod.emt.kami.registry.ModRecipesKAMI;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;
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

        ModFixs modFixer = FMLCommonHandler.instance().getDataFixer().init(Kami.MOD_ID, 1);
        modFixer.registerFix(FixTypes.BLOCK_ENTITY, new BlockDataFixer());
        modFixer.registerFix(FixTypes.ITEM_INSTANCE, new ItemDataFixer());
        modFixer.registerFix(FixTypes.ITEM_INSTANCE, new ItemMetaDataFixer());
    }

    public void postInit() {
        NetworkRegistry.INSTANCE.registerGuiHandler(Kami.instance, new GuiHandlerKami());
        KamiCompatHandler.postInit();
    }

    private void registerResearch() {
        String key;
        String research;

        // If Thaumic Augmentation is loaded, have additional research requirements from that addon
        if(ModIds.thaumic_augmentation.isLoaded) {
            key = "IMPETUS";
            research = "research/compat/basics_thaumic_augmentation";
        } else {
            key = "BASEELDRITCH";
            research = "research/basics";
        }

        ResearchCategories.registerCategory(
                "ZENITH", key, new AspectList(),
                new ResourceLocation(Kami.MOD_ID, "textures/research/r_zenith.png"),
                new ResourceLocation(Kami.MOD_ID, ConfigHandlerKami.misc.alternateResearchBackground ? "textures/gui/research_background_alt.jpg" : "textures/gui/research_background.jpg"),
                new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_over.png"));

        ThaumcraftApi.registerResearchLocation(new ResourceLocation(Kami.MOD_ID, research));
        ThaumcraftApi.registerResearchLocation(new ResourceLocation(Kami.MOD_ID, "research/zenith"));

        if(ConfigHandlerKami.enchantments.eternal) {
            ThaumcraftApi.registerResearchLocation(new ResourceLocation(Kami.MOD_ID, "research/optional/eternal_infusion"));
        }
    }
}
