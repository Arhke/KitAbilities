package net.waterraid.KitAbilities.Effects.DurationEffects;

import net.waterraid.KitAbilities.Effects.DurationEffect;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import net.waterraid.KitAbilities.Utils.AbilityCastEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RootEffect extends DurationEffect {

    public RootEffect(Main instance, Player target, double duration) {
        super(instance, target, duration);
    }
    public RootEffect(Main instance, Player target, PlayerData targetData, int duration) {
        super(instance, target, targetData, duration);
    }

    @Override
    protected void setUpEffect() {
        _target.sendMessage(ChatColor.DARK_GREEN + "You Have Been Rooted");
        registerPotionEffects(new PotionEffect(PotionEffectType.JUMP, secondsToTicks(getRemainingTime()), 250, false, false));
        registerPotionEffects(new PotionEffect(PotionEffectType.SLOW, secondsToTicks(getRemainingTime()), 225, false, false));
        registerAsSubEffect(new AntiSprintEffect(getPlugin(), getTarget(), getTargetData(), getDuration()));

    }

    @Override
    public void removeEffect() {
        _target.removePotionEffect(PotionEffectType.JUMP);
        _target.removePotionEffect(PotionEffectType.SLOW);
    }

    @Override
    public void onEvent(FoodLevelChangeEvent event) {
        if (event.getEntity().getUniqueId().equals(_target.getUniqueId()) && !isExpired()){
            event.setFoodLevel(6);
            event.setCancelled(true);
        }
    }
    @Override
    public void onEvent(AbilityCastEvent event){
        if (event.getPlayer().getUniqueId().equals(_target.getUniqueId()) && !isExpired()){
            if (event.getAbility().moves()){
                _target.sendMessage(ChatColor.DARK_GREEN + "Sorry, You may not Use that while rooted");
                event.setCancelled(true);
            }
        }
    }
    @Override
    public void onEvent(EntityDamageByEntityEvent event){

        new BukkitRunnable() {
            final Player e = (Player)event.getEntity();
            @Override
            public void run() {
                if(!e.isDead() && e.isOnline()) e.setVelocity(new Vector(0, 0, 0));}
        }.runTaskLater(Main.getPlugin(), 0L);
    }


}
