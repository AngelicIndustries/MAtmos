package eu.ha3.matmos.core.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;

@Mixin(SoundHandler.class)
public interface SoundHandlerAccessor {
    @Accessor("sndManager")
    SoundManager matmos$getSndManager();
}
