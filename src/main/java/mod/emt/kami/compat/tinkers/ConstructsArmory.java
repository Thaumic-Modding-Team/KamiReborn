package mod.emt.kami.compat.tinkers;

import c4.conarm.lib.book.ArmoryBook;
import c4.conarm.lib.materials.ArmorMaterials;
import c4.conarm.lib.materials.CoreMaterialStats;
import c4.conarm.lib.materials.PlatesMaterialStats;
import c4.conarm.lib.materials.TrimMaterialStats;
import mod.emt.kami.Kami;
import mod.emt.kami.api.IProxy;
import mod.emt.kami.config.ConfigHandlerKami;
import net.minecraft.util.ResourceLocation;
import slimeknights.mantle.client.book.repository.ModuleFileRepository;
import slimeknights.tconstruct.library.TinkerRegistry;

public class ConstructsArmory implements IProxy {
    // Materials are already registered in the tools class, we are just registering support for armor sets here
    @Override
    public void preInit() {
        if (ConfigHandlerKami.integrations.overpoweredIchorium) {
            TinkerRegistry.addMaterialStats(TinkersConstruct.ICHORIUM,
                    new CoreMaterialStats(50.0F, 30.0F),
                    new PlatesMaterialStats(40.0F, 5.0F, 7.0F),
                    new TrimMaterialStats(30.0F));
        } else {
            TinkerRegistry.addMaterialStats(TinkersConstruct.ICHORIUM,
                    new CoreMaterialStats(24.0F, 22.0F),
                    new PlatesMaterialStats(1.8F, 0.0F, 2.5F),
                    new TrimMaterialStats(1.0F));
        }
        ArmorMaterials.addArmorTrait(TinkersConstruct.ICHORIUM, TinkersConstruct.GOD_COMPLEX);
    }

    @Override
    public void initClient() {
        // We'd need this if we're also adding into Construct Armory's guidebook
        ArmoryBook.INSTANCE.addRepository(new ModuleFileRepository(new ResourceLocation(Kami.MOD_ID, "book").toString()));
    }
}
