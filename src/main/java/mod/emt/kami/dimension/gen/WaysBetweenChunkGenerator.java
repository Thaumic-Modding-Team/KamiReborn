package mod.emt.kami.dimension.gen;

import mod.emt.kami.dimension.world.MapGenVoidCaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import org.jetbrains.annotations.NotNull;
import thecodex6824.thaumicaugmentation.api.TABlocks;
import thecodex6824.thaumicaugmentation.api.block.property.ITAStoneType;
import thecodex6824.thaumicaugmentation.api.block.property.ITAStoneType.StoneType;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class WaysBetweenChunkGenerator implements IChunkGenerator {
    private final World world;
    private static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
    private static final IBlockState CAVE_STONE = TABlocks.STONE.getDefaultState().withProperty(ITAStoneType.STONE_TYPE, StoneType.STONE_VOID);
    private final MapGenVoidCaves CAVES = new MapGenVoidCaves();

    public WaysBetweenChunkGenerator(World world) {
        this.world = world;
    }

    @Override
    public @NotNull Chunk generateChunk(int chunkX, int chunkZ) {
        ChunkPrimer primer = new ChunkPrimer();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                primer.setBlockState(x, 0, z, BEDROCK);

                for (int y = 1; y < 255; y++) {
                    primer.setBlockState(x, y, z, CAVE_STONE);
                }

                primer.setBlockState(x, 255, z, BEDROCK);
            }
        }

        CAVES.generate(world, chunkX, chunkZ, primer);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                primer.setBlockState(x, 0, z, BEDROCK);
                primer.setBlockState(x, 255, z, BEDROCK);
            }
        }

        Chunk chunk = new Chunk(world, primer, chunkX, chunkZ);
        Biome[] biomes = world.getBiomeProvider().getBiomes(null, chunkX * 16, chunkZ * 16, 16, 16);
        byte[] biomeArray = chunk.getBiomeArray();

        for (int i = 0; i < biomeArray.length; i++) {
            biomeArray[i] = (byte) Biome.getIdForBiome(biomes[i]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int chunkX, int chunkZ) {
    }

    @Override
    public boolean generateStructures(@NotNull Chunk chunk, int chunkX, int chunkZ) {
        return false;
    }

    @Override
    public @NotNull List<Biome.SpawnListEntry> getPossibleCreatures(@NotNull EnumCreatureType creatureType, @NotNull BlockPos pos) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(@NotNull World world, @NotNull String structureName, @NotNull BlockPos pos, boolean findUnexplored) {
        return null;
    }

    @Override
    public void recreateStructures(@NotNull Chunk chunk, int chunkX, int chunkZ) {
    }

    @Override
    public boolean isInsideStructure(@NotNull World world, @NotNull String structureName, @NotNull BlockPos pos) {
        return false;
    }
}
