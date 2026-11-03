package eu.ha3.matmos.core.sound;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import eu.ha3.matmos.ConfigManager;
import eu.ha3.matmos.core.mixin.accessor.MinecraftAccessor;
import eu.ha3.matmos.core.mixin.accessor.MusicTickerAccessor;
import eu.ha3.matmos.core.mixin.accessor.SoundHandlerAccessor;
import eu.ha3.matmos.core.mixin.accessor.SoundManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;

/**
 * Has soundpack music and vanilla music take turns, unless music.overlap is set. Neither cuts the other off: while
 * soundpack music can be heard, vanilla's MusicTicker holds its countdown, so its next track waits instead of being
 * skipped; while vanilla music plays, a soundpack track that starts waits for it to end.
 */
public class MusicArbiter {
    /** Ticks of quiet before a vanilla track whose turn came while soundpack music played. */
    private static final int HELD_TRACK_DELAY = 100;

    private final LoopingStreamedSoundManager streams;
    /** The last music another source started: vanilla's MusicTicker, or a mod replacing its track. */
    private ISound other;

    public MusicArbiter(LoopingStreamedSoundManager streams) {
        this.streams = streams;
    }

    // Lowest priority, so this sees the track that will actually play after other mods (Et Futurum's Nether music).
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlaySound(PlaySoundEvent17 event) {
        if (event.result != null && event.category == SoundCategory.MUSIC && !(event.result instanceof StreamingSound)
                && !(event.result instanceof NoAttenuationSound)) {
            other = event.result;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        // SoundManager keeps a track listed until it's done; isSoundPlaying can miss a stream that's still starting.
        SoundManager manager = ((SoundHandlerAccessor) mc.getSoundHandler()).matmos$getSndManager();
        if (other != null && !((SoundManagerAccessor) manager).matmos$getInvPlayingSounds().containsKey(other)) {
            other = null;
        }

        boolean takeTurns = !ConfigManager.getConfig().getBoolean("music.overlap");
        streams.setOtherMusicPlaying(takeTurns && other != null);
        if (takeTurns && streams.isMusicAudible()) {
            MusicTickerAccessor ticker = (MusicTickerAccessor) ((MinecraftAccessor) mc).matmos$getMusicTicker();
            if (ticker.matmos$getTimeUntilNextMusic() < HELD_TRACK_DELAY) {
                ticker.matmos$setTimeUntilNextMusic(HELD_TRACK_DELAY);
            }
        }
    }
}
