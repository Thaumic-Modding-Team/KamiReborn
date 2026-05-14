package mod.emt.kami.registry;

import mod.emt.kami.Kami;
import mod.emt.kami.api.item.IOreDictProvider;
import mod.emt.kami.recipe.infusion.InfusionEnchantmentRecipeEternal;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.IngredientNBTTC;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.items.consumables.ItemPhial;
import thaumcraft.common.lib.crafting.InfusionEnchantmentRecipe;

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
                "KAMI_ICHORWEAVE_FABRIC",
                50,
                new AspectList().add(Aspect.AIR, 2).add(Aspect.EARTH, 2).add(Aspect.WATER, 2).add(Aspect.FIRE, 2).add(Aspect.ORDER, 2).add(Aspect.ENTROPY, 2),
                new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC, 3),
                " F ",
                "FIF",
                'F', new ItemStack(ItemsTC.fabric),
                'I', new ItemStack(ModItemsKAMI.ICHOR)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorweave_hood"), new ShapedArcaneRecipe(
                defaultGroup,
                "KAMI_ICHORWEAVE_ARMOR",
                250,
                new AspectList().add(Aspect.WATER, 8),
                new ItemStack(ModItemsKAMI.ICHORWEAVE_HOOD, 1),
                "FFF",
                "FGF",
                'F', new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC),
                'G', new ItemStack(ItemsTC.goggles, 1, OreDictionary.WILDCARD_VALUE)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorweave_robe"), new ShapedArcaneRecipe(
                defaultGroup,
                "KAMI_ICHORWEAVE_ARMOR",
                250,
                new AspectList().add(Aspect.AIR, 8),
                new ItemStack(ModItemsKAMI.ICHORWEAVE_ROBE, 1),
                "F F",
                "FFF",
                "FFF",
                'F', new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorweave_leggings"), new ShapedArcaneRecipe(
                defaultGroup,
                "KAMI_ICHORWEAVE_ARMOR",
                250,
                new AspectList().add(Aspect.FIRE, 8),
                new ItemStack(ModItemsKAMI.ICHORWEAVE_LEGGINGS, 1),
                "FFF",
                "F F",
                "F F",
                'F', new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorweave_boots"), new ShapedArcaneRecipe(
                defaultGroup,
                "KAMI_ICHORWEAVE_ARMOR",
                250,
                new AspectList().add(Aspect.EARTH, 8),
                new ItemStack(ModItemsKAMI.ICHORWEAVE_BOOTS, 1),
                "F F",
                "F F",
                'F', new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorium_sword"), new ShapedArcaneRecipe(
                defaultGroup,
                "KAMI_ICHORIUM_TOOLS",
                250,
                new AspectList().add(Aspect.AIR, 8),
                new ItemStack(ModItemsKAMI.ICHORIUM_SWORD, 1),
                "I",
                "I",
                "B",
                'I', new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                'B', new ItemStack(ModItemsKAMI.BLESSED_SILVERWOOD_ROD)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorium_shovel"), new ShapedArcaneRecipe(
                defaultGroup,
                "KAMI_ICHORIUM_TOOLS",
                250,
                new AspectList().add(Aspect.EARTH, 8),
                new ItemStack(ModItemsKAMI.ICHORIUM_SHOVEL, 1),
                "I",
                "B",
                "B",
                'I', new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                'B', new ItemStack(ModItemsKAMI.BLESSED_SILVERWOOD_ROD)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorium_pickaxe"), new ShapedArcaneRecipe(
                defaultGroup,
                "KAMI_ICHORIUM_TOOLS",
                250,
                new AspectList().add(Aspect.FIRE, 8),
                new ItemStack(ModItemsKAMI.ICHORIUM_PICKAXE, 1),
                "III",
                " B ",
                " B ",
                'I', new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                'B', new ItemStack(ModItemsKAMI.BLESSED_SILVERWOOD_ROD)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorium_axe"), new ShapedArcaneRecipe(
                defaultGroup,
                "KAMI_ICHORIUM_TOOLS",
                250,
                new AspectList().add(Aspect.WATER, 8),
                new ItemStack(ModItemsKAMI.ICHORIUM_AXE, 1),
                "II",
                "IB",
                " B",
                'I', new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                'B', new ItemStack(ModItemsKAMI.BLESSED_SILVERWOOD_ROD)));
    }

    private static void initCrucibleRecipes() {

    }

    private static void initInfusionRecipes() {
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "awakened_ichorium_axe"),
                new InfusionRecipe("KAMI_AWAKENED_ICHORIUM_AXE", new ItemStack(ModItemsKAMI.AWAKENED_ICHORIUM_AXE), 10,
                        new AspectList().add(Aspect.ENERGY, 500).add(Aspect.FLIGHT, 500).add(Aspect.PLANT, 500).add(Aspect.TOOL, 500).add(Aspect.WATER, 500),
                        ItemsTC.primordialPearl,
                        new ItemStack(ModItemsKAMI.ICHORIUM_AXE),
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "awakened_ichorium_pickaxe"),
                new InfusionRecipe("KAMI_AWAKENED_ICHORIUM_PICKAXE", new ItemStack(ModItemsKAMI.AWAKENED_ICHORIUM_PICKAXE), 10,
                        new AspectList().add(Aspect.DESIRE, 500).add(Aspect.ENTROPY, 500).add(Aspect.FIRE, 500).add(Aspect.METAL, 500).add(Aspect.TOOL, 500),
                        ItemsTC.primordialPearl,
                        new ItemStack(ModItemsKAMI.ICHORIUM_PICKAXE),
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "awakened_ichorium_shovel"),
                new InfusionRecipe("KAMI_AWAKENED_ICHORIUM_SHOVEL", new ItemStack(ModItemsKAMI.AWAKENED_ICHORIUM_SHOVEL), 10,
                        new AspectList().add(Aspect.DEATH, 500).add(Aspect.EARTH, 500).add(Aspect.TOOL, 500).add(Aspect.TRAP, 500).add(Aspect.UNDEAD, 500),
                        ItemsTC.primordialPearl,
                        new ItemStack(ModItemsKAMI.ICHORIUM_SHOVEL),
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "awakened_ichorium_sword"),
                new InfusionRecipe("KAMI_AWAKENED_ICHORIUM_SWORD", new ItemStack(ModItemsKAMI.AWAKENED_ICHORIUM_SWORD), 10,
                        new AspectList().add(Aspect.AIR, 500).add(Aspect.AVERSION, 500).add(Aspect.MIND, 500).add(Aspect.ORDER, 500).add(Aspect.SOUL, 500),
                        ItemsTC.primordialPearl,
                        new ItemStack(ModItemsKAMI.ICHORIUM_SWORD),
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "awakened_ichorweave_boots"),
                new InfusionRecipe("KAMI_AWAKENED_ICHORWEAVE_BOOTS", new ItemStack(ModItemsKAMI.AWAKENED_ICHORWEAVE_BOOTS), 10,
                        new AspectList().add(Aspect.EARTH, 500).add(Aspect.ENERGY, 500).add(Aspect.ORDER, 500).add(Aspect.MOTION, 500).add(Aspect.PROTECT, 500),
                        new ItemStack(ModItemsKAMI.ICHORWEAVE_BOOTS),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "awakened_ichorweave_hood"),
                new InfusionRecipe("KAMI_AWAKENED_ICHORWEAVE_HOOD", new ItemStack(ModItemsKAMI.AWAKENED_ICHORWEAVE_HOOD), 10,
                        new AspectList().add(Aspect.COLD, 500).add(Aspect.DARKNESS, 500).add(Aspect.PROTECT, 500).add(Aspect.SENSES, 500).add(Aspect.WATER, 500),
                        new ItemStack(ModItemsKAMI.ICHORWEAVE_HOOD),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "awakened_ichorweave_leggings"),
                new InfusionRecipe("KAMI_AWAKENED_ICHORWEAVE_LEGGINGS", new ItemStack(ModItemsKAMI.AWAKENED_ICHORWEAVE_LEGGINGS), 10,
                        new AspectList().add(Aspect.FIRE, 500).add(Aspect.MOTION, 500).add(Aspect.LIFE, 500).add(Aspect.LIGHT, 500).add(Aspect.PROTECT, 500),
                        new ItemStack(ModItemsKAMI.ICHORWEAVE_LEGGINGS),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "awakened_ichorweave_robe"),
                new InfusionRecipe("KAMI_AWAKENED_ICHORWEAVE_ROBE", new ItemStack(ModItemsKAMI.AWAKENED_ICHORWEAVE_ROBE), 10,
                        new AspectList().add(Aspect.AIR, 500).add(Aspect.FLIGHT, 500).add(Aspect.MAGIC, 500).add(Aspect.MOTION, 500).add(Aspect.PROTECT, 500),
                        new ItemStack(ModItemsKAMI.ICHORWEAVE_ROBE),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.NETHER_STAR)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "blessed_silverwood_rod"),
                new InfusionRecipe("KAMI_ICHORIUM_TOOLS", new ItemStack(ModItemsKAMI.BLESSED_SILVERWOOD_ROD), 10,
                        new AspectList().add(Aspect.BEAST, 100).add(Aspect.MAN, 100).add(Aspect.MIND, 100).add(Aspect.PLANT, 100).add(Aspect.SENSES, 100),
                        new ItemStack(BlocksTC.saplingSilverwood),
                        new ItemStack(ItemsTC.nuggets, 1, 10),
                        new ItemStack(BlocksTC.logSilverwood),
                        "quicksilver",
                        new ItemStack(BlocksTC.shimmerleaf),
                        new ItemStack(ModItemsKAMI.ICHORIUM_NUGGET),
                        new ItemStack(ModItemsKAMI.ICHORIUM_NUGGET)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "bottomless_pouch"),
                new InfusionRecipe("KAMI_BOTTOMLESS_POUCH", new ItemStack(ModItemsKAMI.FOCUS_POUCH), 10,
                        new AspectList().add(Aspect.AIR, 150).add(Aspect.CRAFT, 150).add(Aspect.ELDRITCH, 150).add(Aspect.MAN, 150).add(Aspect.VOID, 150),
                        new ItemStack(ItemsTC.focusPouch),
                        new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                        new ItemStack(ItemsTC.primordialPearl),
                        new ItemStack(Items.NETHER_STAR),
                        new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC),
                        new ItemStack(BlocksTC.hungryChest),
                        new ItemStack(BlocksTC.jarVoid)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichor"),
                new InfusionRecipe("KAMI_ICHOR", new ItemStack(ModItemsKAMI.ICHOR), 10,
                        new AspectList().add(Aspect.AURA, 75).add(Aspect.CRYSTAL, 75).add(Aspect.DARKNESS, 75)
                                .add(Aspect.ENERGY, 75).add(Aspect.LIFE, 75).add(Aspect.LIGHT, 75).add(Aspect.SOUL, 75),
                        "gemDiamond",
                        new ItemStack(Items.ENDER_EYE),
                        ItemsTC.primordialPearl,
                        new ItemStack(Items.GHAST_TEAR)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorium_caster"),
                new InfusionRecipe("KAMI_ICHORIUM_CASTER", new ItemStack(ModItemsKAMI.ICHORIUM_CASTER), 10,
                        new AspectList().add(Aspect.AURA, 250).add(Aspect.ELDRITCH, 250).add(Aspect.ENERGY, 250).add(Aspect.DARKNESS, 250).add(Aspect.LIGHT, 250),
                        ItemsTC.primordialPearl,
                        new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                        new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                        new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC),
                        new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC),
                        new ItemStack(Items.NETHER_STAR),
                        new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC),
                        new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC),
                        new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                        new ItemStack(ModItemsKAMI.ICHORIUM_INGOT)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorium_ingot"),
                new InfusionRecipe("KAMI_ICHORIUM", new ItemStack(ModItemsKAMI.ICHORIUM_INGOT), 10,
                        new AspectList().add(Aspect.AIR, 100).add(Aspect.EARTH, 100).add(Aspect.FIRE, 100).add(Aspect.METAL, 100).add(Aspect.WATER, 100),
                        "ingotVoid",
                        new ItemStack(ModItemsKAMI.ICHOR),
                        ItemPhial.makeFilledPhial(Aspect.ENTROPY),
                        new ItemStack(ItemsTC.salisMundus),
                        ItemPhial.makeFilledPhial(Aspect.ORDER)));

        InfusionEnchantmentRecipeEternal eternalInfusion = new InfusionEnchantmentRecipeEternal(
                ModEnchantsKAMI.ETERNAL,
                new AspectList().add(Aspect.AURA, 500).add(Aspect.ENERGY, 500).add(Aspect.LIFE, 500).add(Aspect.PROTECT, 500).add(Aspect.SOUL, 500),
                new IngredientNBTTC(new ItemStack(Items.ENCHANTED_BOOK)),
                new ItemStack(ModBlocksKAMI.ICHORIUM_BLOCK),
                new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "eternal_infusion"), eternalInfusion);
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "eternal_infusion_fake"), new InfusionEnchantmentRecipe(eternalInfusion, new ItemStack(ModItemsKAMI.ICHORIUM_SWORD)));
    }
}
