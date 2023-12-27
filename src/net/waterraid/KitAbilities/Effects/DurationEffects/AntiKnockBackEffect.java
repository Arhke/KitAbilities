package net.waterraid.KitAbilities.Effects.DurationEffects;

import net.waterraid.KitAbilities.Effects.DurationEffect;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class AntiKnockBackEffect extends DurationEffect {
    public AntiKnockBackEffect(Main instance, Player target, double duration) {
        super(instance, target, duration);
    }

    public AntiKnockBackEffect(Main instance, Player target, PlayerData targetData, double duration) {
        super(instance, target, targetData, duration);
    }

    @Override
    protected void setUpEffect() {}

    @Override
    public void removeEffect() {

    }
    @Override
    public void onEvent(EntityDamageByEntityEvent event){
        new BukkitRunnable() {
            final Vector v = event.getEntity().getVelocity();
            final Player e = (Player)event.getEntity();
            @Override
            public void run() {if(!e.isDead() && e.isOnline()) e.setVelocity(v);}
        }.runTaskLater(Main.getPlugin(), 0L);
    }
}
