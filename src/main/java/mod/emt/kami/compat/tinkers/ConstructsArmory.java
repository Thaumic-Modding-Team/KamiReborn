package mod.emt.kami.compat.tinkers;

import c4.conarm.lib.book.ArmoryBook;
import c4.conarm.lib.materials.ArmorMaterials;
import c4.conarm.lib.materials.CoreMaterialStats;
import c4.conarm.lib.materials.PlatesMaterialStats;
import c4.conarm.lib.materials.TrimMaterialStats;
import mod.emt.kami.Kami;
import net.minecraft.util.ResourceLocation;
import slimeknights.mantle.client.book.repository.ModuleFileRepository;
import slimeknights.tconstruct.library.TinkerRegistry;

public class ConstructsArmory {
    // Materials are already registered in the tools class, we are just registering support for armor sets here
    public static void preInit()
    {
        TinkerRegistry.addMaterialStats(TinkersConstruct.ICHORIUM,
                new CoreMaterialStats(24.0F, 22.0F),
                new PlatesMaterialStats(1.8F, 0.0F, 2.5F),
                new TrimMaterialStats(1.0F));
        ArmorMaterials.addArmorTrait(TinkersConstruct.ICHORIUM, TinkersConstruct.GOD_COMPLEX);
    }

    public static void init()
    {
        // We'd need this if we're also adding into Construct Armory's guidebook
        ArmoryBook.INSTANCE.addRepository(new ModuleFileRepository(new ResourceLocation(Kami.MOD_ID, "book").toString()));
    }
}
