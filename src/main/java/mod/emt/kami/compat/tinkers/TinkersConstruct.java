package mod.emt.kami.compat.tinkers;

import mod.emt.kami.Kami;
import mod.emt.kami.compat.tinkers.modifiers.ModDivineMandate;
import mod.emt.kami.compat.tinkers.traits.TraitGodComplex;
import mod.emt.kami.registry.ModBlocksKAMI;
import mod.emt.kami.registry.ModItemsKAMI;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.mantle.client.book.repository.ModuleFileRepository;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.book.TinkerBook;
import slimeknights.tconstruct.library.fluid.FluidMolten;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.block.BlockMolten;

public class TinkersConstruct {
    // These materials are used universally between tools and armor
    public static final Material ICHORIUM = new Material("kami_ichorium", 0xFF910C);

    // Fluids
    public static final FluidMolten ICHORIUM_FLUID = new FluidMolten("ichorium", 0xD67B09);

    // Normally you'd need both tool and armor traits, but this one works with both just fine
    public static final AbstractTrait GOD_COMPLEX = new TraitGodComplex();

    // Modifiers
    public static Modifier modDivineMandate;

    // TODO: Return bucket when picked from JEI
    public static void registerFluid(Fluid fluid) {
        FluidRegistry.addBucketForFluid(fluid);
        BlockMolten blockMolten = (BlockMolten) new BlockMolten(fluid).setRegistryName(Kami.MOD_ID, "molten_" + fluid.getName());
        ItemBlock itemBlockMolten = (ItemBlock) new ItemBlock(blockMolten).setRegistryName(blockMolten.getRegistryName());
        ForgeRegistries.BLOCKS.register(blockMolten);
        ForgeRegistries.ITEMS.register(itemBlockMolten);
    }

    public static void preInit()
    {
        TinkerRegistry.addMaterialStats(ICHORIUM,
                new HeadMaterialStats(1375, 9.0F, 7.0F, 5),
                new HandleMaterialStats(1.3F, 10),
                new ExtraMaterialStats(100),
                new BowMaterialStats(1.2F, 1.0F, 8.0F));
        ICHORIUM.addTrait(GOD_COMPLEX);
        registerFluid(ICHORIUM_FLUID);
        ICHORIUM_FLUID.setTemperature(1500);
        TinkerRegistry.integrate(ICHORIUM, ICHORIUM_FLUID, "Ichorium").preInit();

        modDivineMandate = new ModDivineMandate();
    }

    public static void init()
    {
        // We'd need this if we're also adding into Tinkers' Construct's guidebook
        TinkerBook.INSTANCE.addRepository(new ModuleFileRepository(new ResourceLocation(Kami.MOD_ID, "book").toString()));

        ICHORIUM.addCommonItems("Ichorium");
        ICHORIUM.setRepresentativeItem(ModItemsKAMI.ICHORIUM_INGOT);
        ICHORIUM.setFluid(ICHORIUM_FLUID);
        ICHORIUM.setCraftable(false).setCastable(true);

        modDivineMandate.addItem(new ItemStack(ModBlocksKAMI.ICHORIUM_BLOCK), 1, 1);
    }

    public static void postInit()
    {
        // Smeltery stuff goes here
        TinkerSmeltery.registerToolpartMeltingCasting(ICHORIUM);
        // TODO: Check and see if ingots and blocks also need to be registered here or if it's already handled
    }
}
