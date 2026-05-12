package mod.emt.kami.compat.tinkers;

import mod.emt.kami.Kami;
import mod.emt.kami.compat.tinkers.modifiers.ModDivineMandate;
import mod.emt.kami.compat.tinkers.traits.TraitGodComplex;
import mod.emt.kami.registry.ModItemsKAMI;
import net.minecraft.util.ResourceLocation;
import slimeknights.mantle.client.book.repository.ModuleFileRepository;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.book.TinkerBook;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TinkersConstruct {
    // These materials are used universally between tools and armor
    public static final Material ICHORIUM = new Material("kami_ichorium", 0xFF910C);

    // These traits are for tools and not armor
    public static final AbstractTrait GOD_COMPLEX = new TraitGodComplex();

    public static Modifier modDivineMandate;

    public static void preInit()
    {
        TinkerRegistry.addMaterialStats(ICHORIUM,
                new HeadMaterialStats(1375, 9.0F, 7.0F, 5),
                new HandleMaterialStats(1.3F, 10),
                new ExtraMaterialStats(100),
                new BowMaterialStats(1.2F, 1.0F, 8.0F));
        ICHORIUM.addTrait(GOD_COMPLEX);
        TinkerRegistry.integrate(ICHORIUM, null, "Ichorium").preInit();

        modDivineMandate = new ModDivineMandate();
    }

    public static void init()
    {
        TinkerBook.INSTANCE.addRepository(new ModuleFileRepository(new ResourceLocation(Kami.MOD_ID, "book").toString()));

        ICHORIUM.addCommonItems("Ichorium");
        ICHORIUM.setRepresentativeItem(ModItemsKAMI.ICHORIUM_INGOT);
        ICHORIUM.setCraftable(true).setCastable(false);

        modDivineMandate.addItem(ModItemsKAMI.ICHORIUM_INGOT, 1, 1);
    }
}
