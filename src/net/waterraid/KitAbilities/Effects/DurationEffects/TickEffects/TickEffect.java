package net.waterraid.KitAbilities.Effects.DurationEffects.TickEffects;

import net.waterraid.KitAbilities.Effects.DurationEffect;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import org.bukkit.entity.Player;

public abstract class TickEffect extends DurationEffect {
    int _interval;

    public TickEffect(Main instance, Player target, double duration) {
        super(instance, target, duration);
    }
    public TickEffect(Main instance, Player target, PlayerData pd, double duration) {
        super(instance, target, pd, duration);
    }

    public void setInterval(double interval){
        _interval = (int)(interval*20);
        _interval = _interval == 0? 1:_interval;
    }
    public int getInterval(){
        return _interval;
    }
    public void copyFrom(TickEffect effects){
        super.copyFrom(effects);
        setInterval(effects.getInterval());
    }
}
