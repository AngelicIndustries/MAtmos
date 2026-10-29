package eu.ha3.matmos.core.mixin.accessor;

import java.net.URL;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.util.ResourceLocation;

@Mixin(SoundManager.class)
public interface SoundManagerAccessor {
    @Accessor("loaded")
    boolean matmos$isLoaded();

    /** Sound system channel names by playing sound. */
    @Accessor("invPlayingSounds")
    Map<ISound, String> matmos$getInvPlayingSounds();

    @Invoker("getSoundCategoryVolume")
    float matmos$getSoundCategoryVolume(SoundCategory category);

    @Invoker("getURLForSoundResource")
    static URL matmos$getURLForSoundResource(ResourceLocation location) {
        throw new AssertionError();
    }
}
