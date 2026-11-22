package mod.emt.kami.biome;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import org.jetbrains.annotations.NotNull;
import thecodex6824.thaumicaugmentation.api.TABlocks;
import thecodex6824.thaumicaugmentation.api.block.property.ITAStoneType;

import java.util.Random;

public class BiomeWaysBetween extends Biome {
    public static BiomeProperties properties = new BiomeProperties("The Ways Between");

    static {
        properties.setTemperature(0.25F);
        properties.setRainDisabled();
        properties.setBaseHeight(0.0F);
        properties.setHeightVariation(0.0F);
        properties.setWaterColor(0xAA00AA);
    }

    public BiomeWaysBetween() {
        super(properties);
        this.topBlock = TABlocks.STONE.getDefaultState().withProperty(ITAStoneType.STONE_TYPE, ITAStoneType.StoneType.STONE_VOID);
        this.fillerBlock = TABlocks.STONE.getDefaultState().withProperty(ITAStoneType.STONE_TYPE, ITAStoneType.StoneType.STONE_VOID);
        this.flowers.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
    }

    @Override
    public boolean canRain() {
        return false;
    }

    @Override
    public int getSkyColorByTemp(float currentTemperature) {
        return 0;
    }

    @Override
    public void genTerrainBlocks(@NotNull World world, @NotNull Random rand, @NotNull ChunkPrimer chunkPrimer, int x, int z, double noiseVal) {
    }
}
