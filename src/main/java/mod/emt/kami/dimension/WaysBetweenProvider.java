package mod.emt.kami.dimension;

import mod.emt.kami.dimension.ambience.WaysBetweenSoundEventHandler;
import mod.emt.kami.dimension.gen.WaysBetweenBiomeProvider;
import mod.emt.kami.dimension.gen.WaysBetweenChunkGenerator;
import mod.emt.kami.registry.ModDimensionsKAMI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import thecodex6824.thaumicaugmentation.client.renderer.RenderHandlerEmptinessSky;

import javax.annotation.Nullable;

public class WaysBetweenProvider extends WorldProvider {
    @Override
    protected void init() {
        this.hasSkyLight = false;
        this.biomeProvider = new WaysBetweenBiomeProvider();
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            this.setCloudRenderer(new RenderHandlerNull());
            this.setWeatherRenderer(new RenderHandlerNull());
            this.setSkyRenderer(new RenderHandlerEmptinessSky());
        }
    }

    @Override
    protected void generateLightBrightnessTable() {
        for (int i = 0; i < 16; ++i) {
            float b = 1.0F - i / 15.0F;
            this.lightBrightnessTable[i] = (1.0F - b) / (b * 3.0F + 1.0F) * 0.95F + 0.05F;
        }
    }

    @Override
    public @NotNull DimensionType getDimensionType() {
        return ModDimensionsKAMI.WAYS_BETWEEN;
    }

    @Override
    public @NotNull BiomeProvider getBiomeProvider() {
        return biomeProvider;
    }

    @Override
    public @NotNull IChunkGenerator createChunkGenerator() {
        return new WaysBetweenChunkGenerator(world);
    }

    @Override
    public boolean canDoLightning(@NotNull Chunk chunk) {
        return false;
    }

    @Override
    public boolean canDoRainSnowIce(@NotNull Chunk chunk) {
        return false;
    }

    @Override
    public float getSunBrightnessFactor(float par1) {
        return 0.0F;
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
        return null;
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return 0.25F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z) {
        return true;
    }

    @Override
    public double getVoidFogYFactor() {
        Minecraft mc = Minecraft.getMinecraft();
        Entity renderView = mc.getRenderViewEntity();
        double fog = (renderView.lastTickPosY + (renderView.posY - renderView.lastTickPosY) * mc.getRenderPartialTicks());
        return 1.0 / fog;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getSunBrightness(float par1) {
        return 0.0F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1) {
        return 0.0F;
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public @NotNull WorldSleepResult canSleepAt(@NotNull EntityPlayer player, @NotNull BlockPos pos) {
        return WorldSleepResult.DENY;
    }

    @Override
    public boolean canCoordinateBeSpawn(int x, int z) {
        return true;
    }

    @Override
    public float getCloudHeight() {
        return 128.0F;
    }

    @Override
    public boolean shouldMapSpin(@NotNull String entity, double x, double z, double rotation) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public MusicTicker.MusicType getMusicType() {
        return WaysBetweenSoundEventHandler.WAYS_BETWEEN_AMBIENCE;
    }
}