package mod.emt.kami.recipe.elementalfire.recipes;

import mod.emt.kami.api.recipe.IElementalFireRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ElementalFireBlockTransformRecipe implements IElementalFireRecipe {
    private final Block initialBlock;
    private final IBlockState initialState;
    private final IBlockState transformedState;
    private final int destroyChance;

    public ElementalFireBlockTransformRecipe(IBlockState initial, IBlockState transformed, int destroyChance) {
        this.initialBlock = Blocks.AIR;
        this.initialState = initial;
        this.transformedState = transformed;
        this.destroyChance = destroyChance;
    }

    public ElementalFireBlockTransformRecipe(IBlockState initial, IBlockState transformed) {
        this(initial, transformed, 0);
    }

    public ElementalFireBlockTransformRecipe(Block initial, IBlockState transformed, int destroyChance) {
        this.initialBlock = initial;
        this.initialState = Blocks.AIR.getDefaultState();
        this.transformedState = transformed;
        this.destroyChance = destroyChance;
    }

    public ElementalFireBlockTransformRecipe(Block initial, IBlockState transformed) {
        this(initial, transformed, 0);
    }

    @Override
    public boolean isTransformationTarget(World world, BlockPos pos, IBlockState state) {
        if(this.initialBlock != Blocks.AIR) {
            return this.initialBlock == state.getBlock();
        } else {
            return this.initialState.equals(state);
        }
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
