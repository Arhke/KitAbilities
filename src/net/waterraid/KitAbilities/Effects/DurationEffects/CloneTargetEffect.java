package net.waterraid.KitAbilities.Effects.DurationEffects;

import net.citizensnpcs.api.npc.NPC;
import net.waterraid.KitAbilities.Effects.DurationEffect;
import net.waterraid.KitAbilities.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static net.waterraid.KitAbilities.Main.getPlugin;

public class CloneTargetEffect extends DurationEffect {
    NPC npc;

    public CloneTargetEffect(Main instance, Player target, NPC npc, double duration) {
        super(instance, target, duration);
        this.npc = npc;
    }

    @Override
    protected void setUpEffect() {
        if (getTarget() != null && getTarget().isOnline() && !getTarget().isDead()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (isExpired() || npc.getEntity() == null || npc.getEntity().isDead()) {
                        this.cancel();
                        return;
                    }
                    if (getTarget() != null && getTarget().isOnline() && !getTarget().isDead() && !isExpired()) {
                        if(getTargetData().hasEffect(NoTargetEffect.class)){
                            npc.getNavigator().setTarget(npc.getEntity().getLocation());
                            npc.getNavigator().setTarget(null, true);

                            return;
                        }
                        if (npc.getEntity().getLocation().distanceSquared(getTarget().getLocation()) >= 20) {
                            npc.getNavigator().setTarget(getTarget().getLocation());
                        } else {
                            npc.getNavigator().setTarget(getTarget(), true);
                        }
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(getPlugin(), 0L, 10L);
        }
    }
}
