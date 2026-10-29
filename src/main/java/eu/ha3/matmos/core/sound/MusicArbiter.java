package eu.ha3.matmos.core.sound;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import eu.ha3.matmos.ConfigManager;
import eu.ha3.matmos.core.mixin.accessor.SoundHandlerAccessor;
import eu.ha3.matmos.core.mixin.accessor.SoundManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;
import paulscode.sound.SoundSystem;

/**
 * Keeps soundpack music and vanilla music from playing at once, unless music.overlap is set. Soundpack music wins:
 * while it plays, vanilla music doesn't start (MusicTicker then waits its usual pause), and a vanilla track that's
 * already playing fades out.
 */
public class MusicArbiter {
    private static final int FADE_TICKS = 40;

    private final LoopingStreamedSoundManager streams;
    /** The last music another source started: vanilla's MusicTicker, or a mod replacing its track. */
    private ISound other;
    private float fadeFrom = -1;
    private int fadeTick;

    public MusicArbiter(LoopingStreamedSoundManager streams) {
        this.streams = streams;
    }

    private static boolean enabled() {
        return !ConfigManager.getConfig().getBoolean("music.overlap");
    }

    // Lowest priority, so this sees the track that will actually play after other mods (Et Futurum's Nether music).
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlaySound(PlaySoundEvent17 event) {
        if (event.result == null || event.category != SoundCategory.MUSIC || event.result instanceof StreamingSound
                || event.result instanceof NoAttenuationSound || !enabled()) {
            return;
        }
        if (streams.isMusicPlaying()) {
            event.result = null;
            return;
        }
        other = event.result;
        fadeFrom = -1;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || other == null) {
            return;
        }
        SoundManager manager = ((SoundHandlerAccessor) Minecraft.getMinecraft().getSoundHandler()).matmos$getSndManager();
        SoundSystem system = CurrentSoundSystem.get();
        String channel = ((SoundManagerAccessor) manager).matmos$getInvPlayingSounds().get(other);
        if (channel == null) {
            other = null;
            return;
        }
        if (fadeFrom < 0) {
            if (!enabled() || !streams.isMusicPlaying()) {
                return;
            }
            fadeFrom = system.getVolume(channel);
            fadeTick = 0;
        }
        if (++fadeTick >= FADE_TICKS) {
            Minecraft.getMinecraft().getSoundHandler().stopSound(other);
            other = null;
        } else {
            system.setVolume(channel, fadeFrom * (1 - fadeTick / (float) FADE_TICKS));
        }
    }
}
