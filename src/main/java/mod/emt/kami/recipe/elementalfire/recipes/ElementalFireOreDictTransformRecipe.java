package mod.emt.kami.recipe.elementalfire.recipes;

import mod.emt.kami.api.recipe.IElementalFireRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ElementalFireOreDictTransformRecipe implements IElementalFireRecipe {
    private final String oreDict;
    private final IBlockState transformedState;
    private final int destroyChance;

    public ElementalFireOreDictTransformRecipe(String initialOreDict, IBlockState transformed, int destroyChance) {
        this.oreDict = initialOreDict;
        this.transformedState = transformed;
        this.destroyChance = destroyChance;
    }

    public ElementalFireOreDictTransformRecipe(String initialOreDict, IBlockState transformed) {
        this(initialOreDict, transformed, 0);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isTransformationTarget(World world, BlockPos pos, IBlockState state) {
        ItemStack stack = state.getBlock().getItem(world, pos, state);
        if(!stack.isEmpty()) {
            for(int oreId : OreDictionary.getOreIDs(stack)) {
                if(OreDictionary.getOreName(oreId).equals(this.oreDict)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void transformTarget(World world, BlockPos pos, IBlockState state) {
        if(this.destroyChance > 0 && world.rand.nextInt(100) < this.destroyChance) {
            world.setBlockToAir(pos);
        } else {
            world.setBlockState(pos, this.transformedState);
        }
    }
}
