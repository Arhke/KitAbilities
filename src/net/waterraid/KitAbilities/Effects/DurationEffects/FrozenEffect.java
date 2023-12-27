//package net.waterraid.KitAbilities.Effects.DurationEffects;
//
//import net.waterraid.KitAbilities.Effects.DurationEffect;
//import net.waterraid.KitAbilities.Effects.Effect;
//import net.waterraid.KitAbilities.Effects.TaskEffects;
//import net.waterraid.KitAbilities.Main;
//import net.waterraid.KitAbilities.Managers.PlayerData;
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//import org.bukkit.event.player.PlayerToggleFlightEvent;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.util.Vector;
//
//import static net.waterraid.KitAbilities.Main.getPlugin;
//
//public class FrozenEffect extends DurationEffect {
//
//    public FrozenEffect(Main instance, Player target, double duration) {
//        super(instance, target, duration);
//    }
//
//    public FrozenEffect(Main instance, Player target, PlayerData targetData, double duration) {
//        super(instance, target, targetData, duration);
//    }
//
//    @Override
//    protected void setUpEffect() {
//
//        if (!getTarget().isOnGround()) {
//            getTarget().setVelocity(new Vector(0, 0, 0));
//            getTarget().setAllowFlight(true);
//            getTarget().setFlying(true);
//            taskList.add(new BukkitRunnable() {
//                @Override
//                public void run() {
//                    getTarget().setAllowFlight(false);
//                    getTarget().setFlying(false);
//                }
//            }.runTaskLater(getPlugin(), secondsToTicks(getDuration())));
//        }
//    }
//
//    @Override
//    protected void sendEffectMsg() {
//        getTarget().sendMessage("You Are Frozen");
//    }
//
//    @Override
//    public void removeEffect() {
//        getTarget().setAllowFlight(false);
//        getTarget().setFlying(false);
//        taskList.forEach((a)->a.cancel());
//    }
//
//    @Override
//    public void onEvent(PlayerToggleFlightEvent event) {
//
//        event.getPlayer().setVelocity(new Vector(0,0.85,0));
//        getTarget().setAllowFlight(true);
//        getTarget().setFlying(true);
//        event.setCancelled(true);
//    }
//
//    @Override
//    public void rescheduleTasks() {
//        taskList.forEach((a) -> {
//            a.cancel();
//        });
//        taskList.add(new BukkitRunnable() {
//            @Override
//            public void run() {
//                _target.setAllowFlight(false);
//                _target.setFlying(false);
//            }
//        }.runTaskLater(getPlugin(), getRemainingTime() / 50));
//    }
//}
