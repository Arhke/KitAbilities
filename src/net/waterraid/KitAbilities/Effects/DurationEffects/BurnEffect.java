package net.waterraid.KitAbilities.Effects.DurationEffects;

import net.waterraid.KitAbilities.Effects.DurationEffect;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import org.bukkit.entity.Player;

import static net.waterraid.KitAbilities.Abilities.Clone.secondsToTicks;

public class  BurnEffect extends DurationEffect {

    public BurnEffect(Main instance, Player target, double duration) {
        super(instance, target, duration);
    }
    public BurnEffect(Main instance, Player target, PlayerData targetData, double duration) {
        super(instance, target, targetData, duration);
    }

    @Override
    protected void setUpEffect() {
        getTarget().setFireTicks(secondsToTicks(getDuration()));
    }

    @Override
    public void removeEffect() {
        _target.setFireTicks(0);
    }
}
