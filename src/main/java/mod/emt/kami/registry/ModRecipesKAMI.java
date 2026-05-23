package mod.emt.kami.registry;

import com.invadermonky.thaumicapi.utils.libs.ModIds;
import mod.emt.kami.Kami;
import mod.emt.kami.api.item.IOreDictProvider;
import mod.emt.kami.config.ConfigHandlerKami;
import mod.emt.kami.recipe.infusion.InfusionEnchantmentRecipeEternal;
import mod.emt.kami.utils.helpers.ItemHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
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
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;
import thecodex6824.thaumicaugmentation.api.TAItems;

public class ModRecipesKAMI {
    private static final ResourceLocation defaultGroup = new ResourceLocation("");

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
                ItemHelper.setUnbreakable(new ItemStack(ModItemsKAMI.ICHORWEAVE_HOOD)),
                "FFF",
                "FGF",
                'F', new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC),
                'G', new ItemStack(ItemsTC.goggles, 1, OreDictionary.WILDCARD_VALUE)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorweave_robe"), new ShapedArcaneRecipe(
                defaultGroup,
                "KAMI_ICHORWEAVE_ARMOR",
                250,
                new AspectList().add(Aspect.AIR, 8),
                ItemHelper.setUnbreakable(new ItemStack(ModItemsKAMI.ICHORWEAVE_ROBE)),
                "F F",
                "FFF",
                "FFF",
                'F', new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorweave_leggings"), new ShapedArcaneRecipe(
                defaultGroup,
                "KAMI_ICHORWEAVE_ARMOR",
                250,
                new AspectList().add(Aspect.FIRE, 8),
                ItemHelper.setUnbreakable(new ItemStack(ModItemsKAMI.ICHORWEAVE_LEGGINGS)),
                "FFF",
                "F F",
                "F F",
                'F', new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorweave_boots"), new ShapedArcaneRecipe(
                defaultGroup,
                "KAMI_ICHORWEAVE_ARMOR",
                250,
                new AspectList().add(Aspect.EARTH, 8),
                ItemHelper.setUnbreakable(new ItemStack(ModItemsKAMI.ICHORWEAVE_BOOTS)),
                "F F",
                "F F",
                'F', new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC)));
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorium_sword"), new ShapedArcaneRecipe(
                defaultGroup,
                "KAMI_ICHORIUM_TOOLS",
                250,
                new AspectList().add(Aspect.AIR, 8),
                ItemHelper.setUnbreakable(new ItemStack(ModItemsKAMI.ICHORIUM_SWORD)),
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
                ItemHelper.setUnbreakable(new ItemStack(ModItemsKAMI.ICHORIUM_SHOVEL)),
                "I",
                "B",
                "B",
                'I', new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                'B', new ItemStack(ModItemsKAMI.BLESSED_SILVERWOOD_ROD)));
        ItemStack pickaxe = new ItemStack(ModItemsKAMI.ICHORIUM_PICKAXE);
        ItemHelper.setUnbreakable(pickaxe);
        EnumInfusionEnchantment.addInfusionEnchantment(pickaxe, EnumInfusionEnchantment.REFINING, 4);
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorium_pickaxe"), new ShapedArcaneRecipe(
                defaultGroup,
                "KAMI_ICHORIUM_TOOLS",
                250,
                new AspectList().add(Aspect.FIRE, 8),
                pickaxe,
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
                ItemHelper.setUnbreakable(new ItemStack(ModItemsKAMI.ICHORIUM_AXE)),
                "II",
                "IB",
                " B",
                'I', new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                'B', new ItemStack(ModItemsKAMI.BLESSED_SILVERWOOD_ROD)));
    }

    private static void initCrucibleRecipes() {

    }

    private static void initInfusionRecipes() {
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "awakened_ichorium_axe"), new InfusionRecipe(
                "KAMI_AWAKENED_ICHORIUM_AXE",
                ItemHelper.setUnbreakable(new ItemStack(ModItemsKAMI.AWAKENED_ICHORIUM_AXE)),
                10,
                new AspectList().add(Aspect.ENTROPY, 500).add(Aspect.MOTION, 500).add(Aspect.ORDER, 500).add(Aspect.PLANT, 500).add(Aspect.TOOL, 500),
                ModItemsKAMI.ICHORIUM_AXE,
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR)));

        ItemStack pickaxe = new ItemStack(ModItemsKAMI.AWAKENED_ICHORIUM_PICKAXE);
        ItemHelper.setUnbreakable(pickaxe);
        EnumInfusionEnchantment.addInfusionEnchantment(pickaxe, EnumInfusionEnchantment.SOUNDING, 2);
        EnumInfusionEnchantment.addInfusionEnchantment(pickaxe, EnumInfusionEnchantment.REFINING, 3);
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "awakened_ichorium_pickaxe"), new InfusionRecipe(
                "KAMI_AWAKENED_ICHORIUM_PICKAXE",
                pickaxe,
                10,
                new AspectList().add(Aspect.DESIRE, 500).add(Aspect.EARTH, 500).add(Aspect.ENTROPY, 500).add(Aspect.MOTION, 500).add(Aspect.TOOL, 500),
                ModItemsKAMI.ICHORIUM_PICKAXE,
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "awakened_ichorium_shovel"), new InfusionRecipe(
                "KAMI_AWAKENED_ICHORIUM_SHOVEL",
                ItemHelper.setUnbreakable(new ItemStack(ModItemsKAMI.AWAKENED_ICHORIUM_SHOVEL)),
                10,
                new AspectList().add(Aspect.DEATH, 500).add(Aspect.EARTH, 500).add(Aspect.MOTION, 500).add(Aspect.TOOL, 500).add(Aspect.TRAP, 500),
                ModItemsKAMI.ICHORIUM_SHOVEL,
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR)));

        ItemStack sword = new ItemStack(ModItemsKAMI.AWAKENED_ICHORIUM_SWORD);
        ItemHelper.setUnbreakable(sword);
        EnumInfusionEnchantment.addInfusionEnchantment(sword, EnumInfusionEnchantment.ARCING, 4);
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "awakened_ichorium_sword"), new InfusionRecipe(
                "KAMI_AWAKENED_ICHORIUM_SWORD",
                sword,
                10,
                new AspectList().add(Aspect.AVERSION, 500).add(Aspect.DEATH, 500).add(Aspect.ENTROPY, 500).add(Aspect.LIFE, 500).add(Aspect.MAGIC, 500),
                ModItemsKAMI.ICHORIUM_SWORD,
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "awakened_ichorweave_boots"), new InfusionRecipe(
                "KAMI_AWAKENED_ICHORWEAVE_BOOTS",
                ItemHelper.setUnbreakable(new ItemStack(ModItemsKAMI.AWAKENED_ICHORWEAVE_BOOTS)),
                10,
                new AspectList().add(Aspect.FLIGHT, 500).add(Aspect.MOTION, 500).add(Aspect.TRAP, 500),
                ModItemsKAMI.ICHORWEAVE_BOOTS,
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "awakened_ichorweave_hood"), new InfusionRecipe(
                "KAMI_AWAKENED_ICHORWEAVE_HOOD",
                ItemHelper.setUnbreakable(new ItemStack(ModItemsKAMI.AWAKENED_ICHORWEAVE_HOOD)),
                10,
                new AspectList().add(Aspect.DARKNESS, 500).add(Aspect.LIGHT, 500).add(Aspect.WATER, 500),
                ModItemsKAMI.ICHORWEAVE_HOOD,
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "awakened_ichorweave_leggings"), new InfusionRecipe(
                "KAMI_AWAKENED_ICHORWEAVE_LEGGINGS",
                ItemHelper.setUnbreakable(new ItemStack(ModItemsKAMI.AWAKENED_ICHORWEAVE_LEGGINGS)),
                10,
                new AspectList().add(Aspect.AIR, 500).add(Aspect.FIRE, 500).add(Aspect.EXCHANGE, 500),
                ModItemsKAMI.ICHORWEAVE_LEGGINGS,
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "awakened_ichorweave_robe"), new InfusionRecipe(
                "KAMI_AWAKENED_ICHORWEAVE_ROBE",
                ItemHelper.setUnbreakable(new ItemStack(ModItemsKAMI.AWAKENED_ICHORWEAVE_ROBE)),
                10,
                new AspectList().add(Aspect.AURA, 500).add(Aspect.FLIGHT, 500).add(Aspect.MOTION, 500),
                ModItemsKAMI.ICHORWEAVE_ROBE,
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(Items.NETHER_STAR)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "blessed_silverwood_rod"), new InfusionRecipe(
                "KAMI_ICHORIUM_TOOLS",
                new ItemStack(ModItemsKAMI.BLESSED_SILVERWOOD_ROD),
                10,
                new AspectList().add(Aspect.BEAST, 50).add(Aspect.MAN, 50).add(Aspect.MIND, 50).add(Aspect.PLANT, 50).add(Aspect.SENSES, 50),
                new ItemStack(BlocksTC.saplingSilverwood),
                new ItemStack(ItemsTC.nuggets, 1, 10),
                new ItemStack(BlocksTC.logSilverwood),
                "quicksilver",
                new ItemStack(BlocksTC.shimmerleaf)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "bottomless_pouch"), new InfusionRecipe(
                "KAMI_BOTTOMLESS_POUCH",
                new ItemStack(ModItemsKAMI.FOCUS_POUCH),
                10,
                new AspectList().add(Aspect.AIR, 150).add(Aspect.CRAFT, 150).add(Aspect.ELDRITCH, 150).add(Aspect.MAN, 150).add(Aspect.VOID, 150),
                new ItemStack(ItemsTC.focusPouch),
                "ingotIchorium",
                new ItemStack(ItemsTC.primordialPearl),
                new ItemStack(Items.NETHER_STAR),
                new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC),
                new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC),
                new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC),
                new ItemStack(BlocksTC.hungryChest),
                new ItemStack(Blocks.ENDER_CHEST)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichor"), new InfusionRecipe(
                "KAMI_ICHOR",
                new ItemStack(ModItemsKAMI.ICHOR),
                10,
                new AspectList().add(Aspect.AURA, 75).add(Aspect.CRYSTAL, 75).add(Aspect.DARKNESS, 75)
                        .add(Aspect.ENERGY, 75).add(Aspect.LIFE, 75).add(Aspect.LIGHT, 75).add(Aspect.SOUL, 75),
                "gemDiamond",
                new ItemStack(Items.ENDER_EYE),
                ItemsTC.primordialPearl,
                new ItemStack(Items.GHAST_TEAR)));
        ItemStack casterStack;
        if(ModIds.thaumic_augmentation.isLoaded) {
            casterStack = new ItemStack(TAItems.GAUNTLET, 1, 1);
        } else {
            casterStack = new ItemStack(ItemsTC.casterBasic);
        }
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorium_caster"), new InfusionRecipe(
                "KAMI_ICHORIUM_CASTER",
                new ItemStack(ModItemsKAMI.ICHORIUM_CASTER),
                10,
                new AspectList().add(Aspect.AURA, 250).add(Aspect.ELDRITCH, 250).add(Aspect.ENERGY, 250).add(Aspect.MAGIC, 250).add(Aspect.VOID, 250),
                casterStack,
                new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC),
                new ItemStack(Items.NETHER_STAR),
                ItemsTC.primordialPearl,
                new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC),
                new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC),
                new ItemStack(ModItemsKAMI.ICHORIUM_INGOT)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "ichorium_ingot"), new InfusionRecipe(
                "KAMI_ICHORIUM",
                new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                10,
                new AspectList().add(Aspect.AIR, 100).add(Aspect.EARTH, 100).add(Aspect.FIRE, 100).add(Aspect.METAL, 100).add(Aspect.WATER, 100),
                "ingotVoid",
                new ItemStack(ModItemsKAMI.ICHOR),
                ItemPhial.makeFilledPhial(Aspect.ENTROPY),
                new ItemStack(ItemsTC.salisMundus),
                ItemPhial.makeFilledPhial(Aspect.ORDER)));

        // Optional Research
        if (ConfigHandlerKami.enchantments.eternal) {
            InfusionEnchantmentRecipeEternal eternalInfusion = new InfusionEnchantmentRecipeEternal(
                    ModEnchantsKAMI.ETERNAL,
                    new AspectList().add(Aspect.AURA, 250).add(Aspect.ENERGY, 250).add(Aspect.MAGIC, 250).add(Aspect.METAL, 250).add(Aspect.PROTECT, 250),
                    new IngredientNBTTC(new ItemStack(Items.ENCHANTED_BOOK)),
                    new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                    new ItemStack(ModItemsKAMI.ICHORIUM_INGOT),
                    new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC),
                    new ItemStack(ModItemsKAMI.ICHORWEAVE_FABRIC));
            ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "eternal_infusion"), eternalInfusion);
            ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Kami.MOD_ID, "eternal_infusion_fake"), new InfusionEnchantmentRecipe(eternalInfusion, new ItemStack(ModItemsKAMI.ICHORIUM_SWORD)));
        }
    }
}