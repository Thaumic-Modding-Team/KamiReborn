package mod.emt.kami.dimension.ambience;

import java.util.Random;
import java.util.function.Predicate;

import mod.emt.kami.registry.ModDimensionsKAMI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.ISound.AttenuationType;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.VanillaResourceType;
import org.jetbrains.annotations.NotNull;
import thecodex6824.thaumicaugmentation.api.TASounds;
import thecodex6824.thaumicaugmentation.client.sound.EmptinessSoundTicker;

// Needed to make the Emptiness ambience play on The Ways Between
public class WaysBetweenSoundTicker extends EmptinessSoundTicker {
    private static final ISound LOOP;
    private static final ISound AMBIENCE;
    private static final int MIN_TICKS;
    private static final int MAX_TICKS;
    private final Minecraft mc;
    private boolean playingMusic;
    private final Random rand;
    private int ticksLeftToPlaySound;

    public WaysBetweenSoundTicker(Minecraft mcIn) {
        super(mcIn);
        this.mc = mcIn;
        this.rand = new Random();
        this.ticksLeftToPlaySound = MathHelper.getInt(this.rand, MIN_TICKS, MAX_TICKS);
    }

    private void playMusic() {
        this.mc.getSoundHandler().playSound(LOOP);
        this.playingMusic = true;
    }

    private void playSound() {
        if (!this.mc.getSoundHandler().isSoundPlaying(AMBIENCE)) {
            this.mc.getSoundHandler().playSound(AMBIENCE);
        }

        this.ticksLeftToPlaySound = MathHelper.getInt(this.rand, MIN_TICKS, MAX_TICKS);
    }

    public void update() {
        if (this.mc.world == null && this.playingMusic) {
            this.mc.getSoundHandler().stopSound(LOOP);
            this.playingMusic = false;
        } else if (this.mc.world != null && (!this.mc.isSingleplayer() || !this.mc.isGamePaused())) {
            if (this.mc.world.provider.getDimension() != ModDimensionsKAMI.WAYS_BETWEEN.getId() && this.playingMusic) {
                this.mc.getSoundHandler().stopSound(LOOP);
                this.playingMusic = false;
            } else if (this.mc.world.provider.getDimension() == ModDimensionsKAMI.WAYS_BETWEEN.getId()) {
                --this.ticksLeftToPlaySound;
                if (this.ticksLeftToPlaySound <= 0) {
                    this.playSound();
                }

                if (!this.playingMusic) {
                    this.playMusic();
                }
            }
        }

    }

    public void onResourceManagerReload(@NotNull IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        if (resourcePredicate.test(VanillaResourceType.SOUNDS)) {
            this.mc.getSoundHandler().stopSound(LOOP);
            this.playingMusic = false;
        }

    }

    static {
        LOOP = new PositionedSoundRecord(TASounds.EMPTINESS_MUSIC.getSoundName(), SoundCategory.WEATHER, 1.0F, 1.0F, true, 0, AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
        AMBIENCE = new PositionedSoundRecord(TASounds.EMPTINESS_AMBIENCE.getSoundName(), SoundCategory.AMBIENT, 1.0F, 1.0F, false, 0, AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
        MIN_TICKS = 1200;
        MAX_TICKS = 3600;
    }
}
