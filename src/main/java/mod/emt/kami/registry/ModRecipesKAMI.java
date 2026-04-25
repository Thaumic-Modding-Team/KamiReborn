package mod.emt.kami.registry;

import mod.emt.kami.Kami;
import mod.emt.kami.api.item.IOreDictProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.items.ItemsTC;

public class ModRecipesKAMI {
    private static ResourceLocation defaultGroup = new ResourceLocation("");

    public static void initRecipes(RegistryEvent.Register<IRecipe> event) {
        initArcaneWorkbenchRecipes();
        initCrucibleRecipes();
        initInfusionRecipes();
    }

    public static void registerOreDicts() {
        ModBlocksKAMI.MOD_BLOCKS.stream().filter(block -> block instanceof IOreDictProvider).forEach(block -> ((IOreDictProvider) block).registerOreDicts());
        ModItemsKAMI.MOD_ITEMS.stream().filter(item -> item instanceof IOreDictProvider).forEach(item -> ((IOreDictProvider) item).registerOreDicts());
    }

    private static void initArcaneWorkbenchRecipes() {
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorweave_fabric"), new ShapedArcaneRecipe(
                defaultGroup,
                "FIRSTSTEPS", // TODO: Research
                50,
                new AspectList().add(Aspect.AIR, 2).add(Aspect.EARTH, 2).add(Aspect.WATER, 2).add(Aspect.FIRE, 2).add(Aspect.ORDER, 2).add(Aspect.ENTROPY, 2),
                new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC, 2),
                " F ",
                "SIS",
                " F ",
                'F', new ItemStack(ItemsTC.fabric),
                'S', "string",
                'I', new ItemStack(ModItemsKAMI.ICHOR)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorweave_hood"), new ShapedArcaneRecipe(
                defaultGroup,
                "FIRSTSTEPS", // TODO: Research
                250,
                new AspectList().add(Aspect.WATER, 4),
                new ItemStack(ModItemsKAMI.ICHORWEAVE_HOOD, 1),
                "FFF",
                "FGF",
                'F', new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC),
                'G', new ItemStack(ItemsTC.goggles, 1, OreDictionary.WILDCARD_VALUE)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorweave_robe"), new ShapedArcaneRecipe(
                defaultGroup,
                "FIRSTSTEPS", // TODO: Research
                250,
                new AspectList().add(Aspect.AIR, 4),
                new ItemStack(ModItemsKAMI.ICHORWEAVE_ROBE, 1),
                "F F",
                "FFF",
                "FFF",
                'F', new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorweave_leggings"), new ShapedArcaneRecipe(
                defaultGroup,
                "FIRSTSTEPS", // TODO: Research
                250,
                new AspectList().add(Aspect.FIRE, 4),
                new ItemStack(ModItemsKAMI.ICHORWEAVE_LEGGINGS, 1),
                "FFF",
                "F F",
                "F F",
                'F', new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorweave_boots"), new ShapedArcaneRecipe(
                defaultGroup,
                "FIRSTSTEPS", // TODO: Research
                250,
                new AspectList().add(Aspect.EARTH, 4),
                new ItemStack(ModItemsKAMI.ICHORWEAVE_BOOTS, 1),
                "F F",
                "F F",
                'F', new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorium_sword"), new ShapedArcaneRecipe(
                defaultGroup,
                "FIRSTSTEPS", // TODO: Research
                250,
                new AspectList().add(Aspect.AIR, 4),
                new ItemStack(ModItemsKAMI.ICHORIUM_SWORD, 1),
                "I",
                "I",
                "B",
                'I', new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                'B', "bone"));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorium_shovel"), new ShapedArcaneRecipe(
                defaultGroup,
                "FIRSTSTEPS", // TODO: Research
                250,
                new AspectList().add(Aspect.EARTH, 4),
                new ItemStack(ModItemsKAMI.ICHORIUM_SHOVEL, 1),
                "I",
                "B",
                "B",
                'I', new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                'B', "bone"));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorium_pickaxe"), new ShapedArcaneRecipe(
                defaultGroup,
                "FIRSTSTEPS", // TODO: Research
                250,
                new AspectList().add(Aspect.FIRE, 4),
                new ItemStack(ModItemsKAMI.ICHORIUM_PICKAXE, 1),
                "III",
                " B ",
                " B ",
                'I', new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                'B', "bone"));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorium_axe"), new ShapedArcaneRecipe(
                defaultGroup,
                "FIRSTSTEPS", // TODO: Research
                250,
                new AspectList().add(Aspect.WATER, 4),
                new ItemStack(ModItemsKAMI.ICHORIUM_AXE, 1),
                "II",
                "IB",
                " B",
                'I', new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                'B', "bone"));
    }

    private static void initCrucibleRecipes() {

    }

    private static void initInfusionRecipes() {

    }
}
