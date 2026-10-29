package eu.ha3.matmos.core.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("isInWeb")
    boolean matmos$isInWeb();
}
