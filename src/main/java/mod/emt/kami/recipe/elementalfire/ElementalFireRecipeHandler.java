package mod.emt.kami.recipe.elementalfire;

import mod.emt.kami.Kami;
import mod.emt.kami.api.recipe.IElementalFireRecipe;
import mod.emt.kami.recipe.elementalfire.recipes.ElementalFireBlockTransformRecipe;
import mod.emt.kami.recipe.elementalfire.recipes.ElementalFireOreDictTransformRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElementalFireRecipeHandler {
    private static final Map<Aspect, List<IElementalFireRecipe>> FIRE_RECIPES = new HashMap<>();

    public static List<IElementalFireRecipe> getTransformationRecipes(Aspect fireAspect) {
        return FIRE_RECIPES.getOrDefault(fireAspect, new ArrayList<>());
    }

    @Nullable
    public static IElementalFireRecipe getTransformationRecipe(Aspect fireAspect, World world, BlockPos pos, IBlockState state) {
        for(IElementalFireRecipe recipe : getTransformationRecipes(fireAspect)) {
            if(recipe.isTransformationTarget(world, pos, state)) {
                return recipe;
            }
        }
        return null;
    }

    public static void addFireTransformation(Aspect fireAspect, IElementalFireRecipe recipe) {
        FIRE_RECIPES.putIfAbsent(fireAspect, new ArrayList<>());
        FIRE_RECIPES.get(fireAspect).add(recipe);
    }

    public static void addFireTransformations(Aspect fireAspect, Collection<IElementalFireRecipe> recipes) {
        FIRE_RECIPES.putIfAbsent(fireAspect, new ArrayList<>());
        FIRE_RECIPES.get(fireAspect).addAll(recipes);
    }

    public static void clearRecipes(Aspect fireAspect) {
        FIRE_RECIPES.remove(fireAspect);
    }

    public static void clearAllRecipes() {
        FIRE_RECIPES.clear();
    }

    @SuppressWarnings("deprecation")
    public static List<IElementalFireRecipe> getRecipesFromConfig(String[] configRecipes) {
        Pattern statePattern = Pattern.compile("^([^:\\s]+?:[^:\\s]+?)(?::(\\d+))?;([^:\\s]+?:[^:\\s]+?)(?::(\\d+))?;(\\d+)$");
        Pattern orePattern = Pattern.compile("^(\\w+);([^:\\s]+?:[^:\\s]+?)(?::(\\d+))?;(\\d+)$");
        List<IElementalFireRecipe> recipes = new ArrayList<>();

        for(String str : configRecipes) {
            try {
                Matcher matcher = statePattern.matcher(str);
                if(matcher.find()) {
                    Block target = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(matcher.group(1)));
                    Block result = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(matcher.group(3)));
                    if(target != null && target != Blocks.AIR && result != null && result != Blocks.AIR) {
                        IBlockState targetState = matcher.group(2) != null ? target.getStateFromMeta(Integer.parseInt(matcher.group(2))) : null;
                        IBlockState resultState = matcher.group(4) != null ? result.getStateFromMeta(Integer.parseInt(matcher.group(4))) : result.getDefaultState();
                        int destroyChance = MathHelper.clamp(Integer.parseInt(matcher.group(5)), 0, 100);
                        if(targetState != null) {
                            recipes.add(new ElementalFireBlockTransformRecipe(targetState, resultState, destroyChance));
                        } else {
                            recipes.add(new ElementalFireBlockTransformRecipe(target, resultState, destroyChance));
                        }
                        continue;
                    }
                } else {
                    matcher = orePattern.matcher(str);
                    if(matcher.find()) {
                        String oreDict = matcher.group(1);
                        Block result = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(matcher.group(2)));
                        if(result != null && result != Blocks.AIR) {
                            IBlockState resultState = matcher.group(3) != null ? result.getStateFromMeta(Integer.parseInt(matcher.group(3))) : result.getDefaultState();
                            int destroyChance = MathHelper.clamp(Integer.parseInt(matcher.group(4)), 0, 100);
                            recipes.add(new ElementalFireOreDictTransformRecipe(oreDict, resultState, destroyChance));
                            continue;
                        }
                    }
                }

                throw new IllegalArgumentException();
            } catch (Exception e) {
                Kami.LOGGER.warn("Invalid elemental fire configuration string: {}", str);
            }
        }

        return recipes;
    }
}
