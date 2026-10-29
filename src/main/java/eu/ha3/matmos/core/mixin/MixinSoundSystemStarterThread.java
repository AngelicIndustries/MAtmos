package eu.ha3.matmos.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import eu.ha3.matmos.core.sound.CurrentSoundSystem;
import paulscode.sound.SoundSystem;

/**
 * Hands each sound system SoundManager creates to {@link CurrentSoundSystem}. SoundManager.sndSystem's type is this
 * package-private class, which no accessor can name, so this takes its place.
 */
@Mixin(targets = "net.minecraft.client.audio.SoundManager$SoundSystemStarterThread")
abstract class MixinSoundSystemStarterThread {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void matmos$remember(CallbackInfo ci) {
        CurrentSoundSystem.set((SoundSystem) (Object) this);
    }
}
