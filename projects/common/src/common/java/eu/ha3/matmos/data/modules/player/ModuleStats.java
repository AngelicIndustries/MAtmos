package eu.ha3.matmos.data.modules.player;

import eu.ha3.matmos.core.mixin.accessor.FoodStatsAccessor;
import eu.ha3.matmos.core.sheet.DataPackage;
import eu.ha3.matmos.data.modules.Module;
import eu.ha3.matmos.data.modules.ModuleProcessor;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Processing module for deeper player statistics. Health, Oxygen, Food, etc.
 */
public class ModuleStats extends ModuleProcessor implements Module {
    public ModuleStats(DataPackage data) {
        super(data, "ply_stats");
    }

    @Override
    protected void doProcess() {
        EntityPlayer player = getPlayer();

        setValue("health1k", (int) (player.getHealth() * 1000));
        setValue("oxygen", player.getAir());
        setValue("armor", player.getTotalArmorValue());
        setValue("food", player.getFoodStats().getFoodLevel());
        setValue("saturation1k", (int) (player.getFoodStats().getSaturationLevel() * 1000));
        setValue("exhaustion1k", (int) (((FoodStatsAccessor) player.getFoodStats()).matmos$getFoodExhaustionLevel() * 1000));
        setValue("experience1k", (int) (player.experience * 1000));
        setValue("experience_level", player.experienceLevel);
        setValue("experience_total", player.experienceTotal);
    }
}
