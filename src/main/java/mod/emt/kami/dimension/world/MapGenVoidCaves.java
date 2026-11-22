package mod.emt.kami.dimension.world;

import mod.emt.kami.dimension.noise.NoiseGeneratorOpenSimplex2;
import mod.emt.kami.dimension.noise.NoiseGeneratorWorley;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import org.jetbrains.annotations.NotNull;
import thecodex6824.thaumicaugmentation.api.block.property.ITAStoneType;
import thecodex6824.thaumicaugmentation.api.block.property.ITAStoneType.StoneType;

public class MapGenVoidCaves extends MapGenBase {
    private static final IBlockState AIR = Blocks.AIR.getDefaultState();

    // Cave Rooms
    private static final float ROOM_SCALE_X = 0.0065F;
    private static final float ROOM_SCALE_Y = 0.010F;
    private static final float ROOM_SCALE_Z = 0.0065F;

    // Tunnel Gen
    private static final float TUNNEL_SCALE_1 = 0.0006F;
    private static final float TUNNEL_SCALE_2 = 0.004F;

    // Tunnel Chance
    private static final float TUNNEL_THRESHOLD_1 = 0.018F;
    private static final float TUNNEL_THRESHOLD_2 = 0.11F;

    // Worley Tunnel Chance
    private static final float WORLEY_THRESHOLD = 0.1F;

    // Height Limits
    private static final int MIN_CARVE_Y = 6;
    private static final int MAX_CARVE_Y = 249;

    // Tunnel Transition Limits
    private static final int BOTTOM_TRANSITION_HEIGHT = 48;
    private static final int TOP_TRANSITION_HEIGHT = 48;

    private World world;

    public MapGenVoidCaves() {
        range = 0;
    }

    protected boolean generateCaveRoom(int x, int y, int z) {
        float roomNoise = NoiseGeneratorOpenSimplex2.generate(world.getSeed(), 4, x, y, z, ROOM_SCALE_X, ROOM_SCALE_Y, ROOM_SCALE_Z);
        float bottomHeight = Math.min(1.0F, (y - MIN_CARVE_Y) / (float) BOTTOM_TRANSITION_HEIGHT);
        float topHeight = Math.min(1.0F, (MAX_CARVE_Y - y) / (float) TOP_TRANSITION_HEIGHT);
        float heightFactor = 0.45F + (1.0F - Math.min(bottomHeight, topHeight)) * 0.10F;
        return roomNoise > heightFactor;
    }

    protected boolean generateTunnel(int x, int y, int z) {
        float tunnelNoiseSimplex1 = NoiseGeneratorOpenSimplex2.generate(world.getSeed() + 1000, 4, x, y, z, TUNNEL_SCALE_1, TUNNEL_SCALE_1, TUNNEL_SCALE_1);
        float tunnelNoiseSimplex2 = NoiseGeneratorOpenSimplex2.generate(~world.getSeed(), 4, x, y, z, TUNNEL_SCALE_2, TUNNEL_SCALE_2, TUNNEL_SCALE_2);
        float tunnelNoiseWorley = NoiseGeneratorWorley.generate(world.getSeed(), x + 0.5, y + 0.5, z + 0.5);
        float tunnelFactor = getTunnelFactor(y);
        return ((tunnelNoiseSimplex1 > -TUNNEL_THRESHOLD_1 * tunnelFactor && tunnelNoiseSimplex1 < TUNNEL_THRESHOLD_1 * tunnelFactor)
                || (tunnelNoiseSimplex2 > -TUNNEL_THRESHOLD_2 * tunnelFactor && tunnelNoiseSimplex2 < TUNNEL_THRESHOLD_2 * tunnelFactor)) && tunnelNoiseWorley < WORLEY_THRESHOLD;
    }

    private float getTunnelFactor(int y) {
        float bottomFactor = (y - MIN_CARVE_Y) / (float) BOTTOM_TRANSITION_HEIGHT;
        float topFactor = (MAX_CARVE_Y - y) / (float) TOP_TRANSITION_HEIGHT;
        bottomFactor = Math.max(0.45F, Math.min(1.0F, bottomFactor));
        topFactor = Math.max(0.45F, Math.min(1.0F, topFactor));
        return Math.min(bottomFactor, topFactor);
    }

    @Override
    public void generate(@NotNull World world, int chunkX, int chunkZ, @NotNull ChunkPrimer primer) {
        this.world = world;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int realX = chunkX * 16 + x;
                int realZ = chunkZ * 16 + z;

                for (int y = MAX_CARVE_Y; y >= MIN_CARVE_Y; y--) {
                    IBlockState state = primer.getBlockState(x, y, z);

                    if (!canReplaceBlock(state)) {
                        continue;
                    }

                    if (generateCaveRoom(realX, y, realZ) || generateTunnel(realX, y, realZ)) {
                        primer.setBlockState(x, y, z, AIR);
                    }
                }
            }
        }
    }

    protected boolean canReplaceBlock(IBlockState state) {
        if (!state.getPropertyKeys().contains(ITAStoneType.STONE_TYPE)) {
            return false;
        }

        StoneType type = state.getValue(ITAStoneType.STONE_TYPE);
        return type == StoneType.STONE_VOID;
    }
}
