package net.waterraid.KitAbilities.Effects.DurationEffects;

import net.waterraid.KitAbilities.Effects.DurationEffect;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class NoFallEffect extends DurationEffect {

    public NoFallEffect(Main instance, Player target, double duration) {
        super(instance, target, duration);
    }
    public NoFallEffect(Main instance, Player target, PlayerData targetData, double duration) {
        super(instance, target, targetData, duration);
    }

    @Override
    protected void setUpEffect() {
        _target.sendMessage("You will now take dmg from your falls");
        registerTasks(new BukkitRunnable() {
            @Override
            public void run() {
                getTarget().sendMessage("You will No Longer Take Fall Damage");
            }
        });
    }


    @Override
    public void removeEffect() {

    }

    @Override
    public void onEvent(EntityDamageEvent event) {
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL && !isExpired()){
            event.setDamage(event.getDamage()*_strength);
            event.setCancelled(false);
        }
    }
}
