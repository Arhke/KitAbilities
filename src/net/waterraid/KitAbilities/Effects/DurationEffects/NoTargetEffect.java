package net.waterraid.KitAbilities.Effects.DurationEffects;

import net.waterraid.KitAbilities.Effects.DurationEffect;
import net.waterraid.KitAbilities.Effects.Effect;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class NoTargetEffect extends DurationEffect {

    public NoTargetEffect(Main instance, Player target, double duration) {
        super(instance, target, duration);
    }

    public NoTargetEffect(Main instance, Player target, PlayerData targetData, double duration) {
        super(instance, target, targetData, duration);
    }

    @Override
    protected void setUpEffect() {
        getTarget().sendMessage("You are now Untarget-able");
        for(Player p: Bukkit.getOnlinePlayers()){
            p.hidePlayer(getTarget());
        }
        registerTasks(new BukkitRunnable() {
            @Override
            public void run() {
                getTarget().sendMessage("You are now Target-able");
                for(Player p: Bukkit.getOnlinePlayers()){
                    p.showPlayer(getTarget());
                }
            }
        });
    }

    @Override
    public void removeEffect() {

    }

    @Override
    public void onEvent(PlayerJoinEvent event){
        if(!isExpired()) {
            event.getPlayer().hidePlayer(getTarget());
        }
    }
}
