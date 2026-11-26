package mod.emt.kami.api.recipe;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IElementalFireRecipe {
    /**
     * Whether this recipe can transformed the specified block.
     *
     * @param world The world object
     * @param state The state of the block to be transformed
     * @param pos The position of the block to be transformed
     * @return True if the block can be transformed by this fire
     */
    boolean isTransformationTarget(World world, BlockPos pos, IBlockState state);

    /**
     * Performs the actual block transformation for this recipe.
     *
     * @param world The world object
     * @param state The state of the block to be transformed
     * @param pos The position of the block to be transformed
     */
    void transformTarget(World world, BlockPos pos, IBlockState state);
}
