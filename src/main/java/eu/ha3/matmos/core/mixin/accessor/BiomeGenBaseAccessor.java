package eu.ha3.matmos.core.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.biome.BiomeGenBase;

@Mixin(BiomeGenBase.class)
public interface BiomeGenBaseAccessor {
    @Accessor("enableRain")
    boolean matmos$getEnableRain();
}
