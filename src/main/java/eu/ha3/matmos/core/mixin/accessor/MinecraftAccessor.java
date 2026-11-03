package eu.ha3.matmos.core.mixin.accessor;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.resources.IResourcePack;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {
    @Accessor("defaultResourcePacks")
    List<IResourcePack> matmos$getDefaultResourcePacks();

    @Accessor("mcMusicTicker")
    MusicTicker matmos$getMusicTicker();
}
