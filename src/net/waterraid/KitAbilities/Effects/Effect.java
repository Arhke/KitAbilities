package net.waterraid.KitAbilities.Effects;

import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import net.waterraid.KitAbilities.Utils.AbilityCastEvent;
import net.waterraid.KitAbilities.Utils.ArrowHitPlayerEvent;
import net.waterraid.KitAbilities.Utils.MainBase;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Effect extends MainBase {
    protected Player _target;
    protected Player _from;
    protected PlayerData _targetData;
    protected double _strength = 1.0;


    public Effect(Main instance, Player target){
        super(instance);
        _target = target;
        _targetData = getPlugin().getPDManager().getOrNewData(_target.getUniqueId());
    }
    public Effect(Main instance, Player target, PlayerData targetData){
        super(instance);
        _target = target;
        _targetData = targetData;
    }

    // =====================Events===================
    public void onEvent(PlayerDeathEvent event){}
    public void onEvent(EntityDamageByEntityEvent event){}
    public void onEvent(FoodLevelChangeEvent event){}
    public void onEvent(PlayerInteractEvent event){}
    public void onEvent(PlayerToggleFlightEvent event){}
    public void onEvent(EntityDamageEvent event){}
    public void onEvent(PlayerJoinEvent event){}
    public void onEvent(PlayerToggleSprintEvent event){}
    public void onEvent(AbilityCastEvent event){}
    public void onEvent(ArrowHitPlayerEvent event){}
    public void onEvent(ProjectileLaunchEvent event){}
    public void onEvent(PlayerFishEvent event){}

    //=====================Getters and Setters===================
    public void setTarget(Player player) {
        _target = player;
    }
    public Player getTarget(){
        return _target;
    }
    public void setFrom(Player player) {
        _from = player;
    }
    public Player getFrom() {
        return _from;
}
    public PlayerData getTargetData() {
        return _targetData;
    }
    public void setStrength(double strength){
        _strength = strength;
    }
    public double  getStrength() {
        return _strength;
    }

    public void copyFrom(Effect effects){
        setFrom(effects.getFrom());
        setStrength(effects.getStrength());
    }
    // ====================On Start Runs================
    public void applyEffect() {

        if (_target == null || !_target.isOnline()){
            return;
        }
        setUpEffect();


    }
    //====================Helper Functions========================
    protected abstract void setUpEffect();






}
