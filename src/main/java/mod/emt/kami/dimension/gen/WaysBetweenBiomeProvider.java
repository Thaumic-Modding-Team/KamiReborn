package mod.emt.kami.dimension.gen;

import mod.emt.kami.registry.ModBiomesKAMI;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WaysBetweenBiomeProvider extends BiomeProvider {
    private final BiomeCache biomeCache;
    private static final List<Biome> BIOMES = Collections.singletonList(ModBiomesKAMI.WAYS_BETWEEN);

    public WaysBetweenBiomeProvider() {
        biomeCache = new BiomeCache(this);
    }

    @Override
    public @NotNull List<Biome> getBiomesToSpawnIn() {
        return BIOMES;
    }

    @Override
    public @NotNull Biome getBiome(@NotNull BlockPos pos) {
        return ModBiomesKAMI.WAYS_BETWEEN;
    }

    @Override
    public @NotNull Biome getBiome(@NotNull BlockPos pos, @Nullable Biome defaultBiome) {
        return ModBiomesKAMI.WAYS_BETWEEN;
    }

    @Override
    public Biome @NotNull [] getBiomes(@Nullable Biome[] biomes, int x, int z, int width, int height) {
        if (biomes == null || biomes.length < width * height) {
            biomes = new Biome[width * height];
        }

        for (int i = 0; i < width * height; i++) {
            biomes[i] = ModBiomesKAMI.WAYS_BETWEEN;
        }

        return biomes;
    }

    @Override
    public Biome @NotNull [] getBiomesForGeneration(Biome @NotNull [] biomes, int x, int z, int width, int height) {
        return getBiomes(biomes, x, z, width, height);
    }

    @Override
    public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed) {
        return allowed.contains(ModBiomesKAMI.WAYS_BETWEEN);
    }

    @Override
    public BlockPos findBiomePosition(int x, int z, int radius, List<Biome> biomes, @NotNull Random random) {
        if (biomes.contains(ModBiomesKAMI.WAYS_BETWEEN)) {
            return new BlockPos(x, 0, z);
        }

        return null;
    }

    @Override
    public void cleanupCache() {
        biomeCache.cleanupCache();
    }
}
