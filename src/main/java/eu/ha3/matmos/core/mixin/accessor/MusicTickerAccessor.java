package eu.ha3.matmos.core.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.audio.MusicTicker;

@Mixin(MusicTicker.class)
public interface MusicTickerAccessor {
    /** timeUntilNextMusic, unnamed in these mappings: ticks until the next track, counted down while none plays. */
    @Accessor("field_147676_d")
    int matmos$getTimeUntilNextMusic();

    @Accessor("field_147676_d")
    void matmos$setTimeUntilNextMusic(int ticks);
}
