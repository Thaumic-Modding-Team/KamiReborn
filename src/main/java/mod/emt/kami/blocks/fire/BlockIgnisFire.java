package mod.emt.kami.blocks.fire;

import net.minecraft.init.Blocks;
import thaumcraft.api.aspects.Aspect;

public class BlockIgnisFire extends BlockElementalFire {
    public BlockIgnisFire() {
        super("ignis_fire", Aspect.FIRE);
        addTransformation(Blocks.GRASS.getDefaultState(), Blocks.NETHERRACK.getDefaultState());
        addTransformation(Blocks.DIRT.getDefaultState(), Blocks.NETHERRACK.getDefaultState());
        addTransformation(Blocks.SAND.getDefaultState(), Blocks.SOUL_SAND.getDefaultState());
        addTransformation(Blocks.GRAVEL.getDefaultState(), Blocks.SOUL_SAND.getDefaultState());
        addTransformation(Blocks.CLAY.getDefaultState(), Blocks.GLOWSTONE.getDefaultState());
        addTransformation(Blocks.COAL_ORE.getDefaultState(), Blocks.QUARTZ_ORE.getDefaultState());
        addTransformation(Blocks.IRON_ORE.getDefaultState(), Blocks.QUARTZ_ORE.getDefaultState());
        addTransformation(Blocks.DIAMOND_ORE.getDefaultState(), Blocks.QUARTZ_ORE.getDefaultState());
        addTransformation(Blocks.EMERALD_ORE.getDefaultState(), Blocks.QUARTZ_ORE.getDefaultState());
        addTransformation(Blocks.GOLD_BLOCK.getDefaultState(), Blocks.QUARTZ_ORE.getDefaultState());
        addTransformation(Blocks.LAPIS_ORE.getDefaultState(), Blocks.QUARTZ_ORE.getDefaultState());
        addTransformation(Blocks.REDSTONE_ORE.getDefaultState(), Blocks.QUARTZ_ORE.getDefaultState());
        addTransformation(Blocks.LIT_REDSTONE_ORE.getDefaultState(), Blocks.QUARTZ_ORE.getDefaultState());
        addTransformation(Blocks.WATER.getDefaultState(), Blocks.LAVA.getDefaultState());
        addTransformation(Blocks.WHEAT.getDefaultState(), Blocks.NETHER_WART.getDefaultState());
        addTransformation(Blocks.POTATOES.getDefaultState(), Blocks.NETHER_WART.getDefaultState());
        addTransformation(Blocks.CARROTS.getDefaultState(), Blocks.NETHER_WART.getDefaultState());
        addTransformation(Blocks.RED_FLOWER.getDefaultState(), Blocks.BROWN_MUSHROOM.getDefaultState());
        addTransformation(Blocks.YELLOW_FLOWER.getDefaultState(), Blocks.YELLOW_FLOWER.getDefaultState());
    }
}