package eu.ha3.matmos.core.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.FoodStats;

@Mixin(FoodStats.class)
public interface FoodStatsAccessor {
    @Accessor("foodExhaustionLevel")
    float matmos$getFoodExhaustionLevel();
}
