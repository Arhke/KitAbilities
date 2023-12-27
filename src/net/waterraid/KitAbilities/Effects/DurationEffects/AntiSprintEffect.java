package net.waterraid.KitAbilities.Effects.DurationEffects;

import net.waterraid.KitAbilities.Effects.DurationEffect;
import net.waterraid.KitAbilities.Effects.Effect;
import net.waterraid.KitAbilities.Effects.TaskEffects;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class AntiSprintEffect extends DurationEffect{
    private int _foodLevel = 20;

    public AntiSprintEffect(Main instance, Player target, double duration) {
        super(instance, target, duration);
    }

    public AntiSprintEffect(Main instance, Player target, PlayerData targetData, double duration) {
        super(instance, target, targetData, duration);
    }

    @Override
    public void removeEffect() {

    }


    @Override
    protected void setUpEffect() {
        _foodLevel = getTarget().getFoodLevel();
        getTarget().setFoodLevel(6);
        registerTasks(new BukkitRunnable(){
            @Override
            public void run() {
                _target.setFoodLevel(_foodLevel);
            }
        });

    }
}
