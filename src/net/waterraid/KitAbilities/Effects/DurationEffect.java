package net.waterraid.KitAbilities.Effects;

import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static net.waterraid.KitAbilities.Abilities.Clone.secondsToTicks;
import static net.waterraid.KitAbilities.Main.getPlugin;

public abstract class DurationEffect extends Effect {
    protected int duration;
    protected long cdTimeStamp = System.currentTimeMillis();
    protected boolean _isSubEffect = false;
    protected List<DurationEffect> subEffectList = new ArrayList<>();
    protected Map<BukkitRunnable, BukkitTask> taskList = new HashMap<>();
    protected Set<PotionEffect> potions = new HashSet<>();

    public DurationEffect(Main instance, Player target, double duration){
        this(instance, target, instance.getPDManager().getOrNewData(target.getUniqueId()), duration);
    }
    public DurationEffect(Main instance, Player target, PlayerData targetData, double duration){
        super(instance, target, targetData);
        this.duration = (int)(duration*1000);
        cdTimeStamp = System.currentTimeMillis() + this.duration;
        _target = target;
        _targetData = targetData;
    }

    // ===================Duration Control=============

    public void setNewTimeStamp(long TimeStamp) {
        duration = (int)(TimeStamp-System.currentTimeMillis());
        cdTimeStamp = TimeStamp;
        subEffectList.forEach((s)->{s.setNewTimeStamp(TimeStamp);});
    }
    public long getTimeStamp() {return cdTimeStamp;}

    public double getRemainingTime() {
        return (getTimeStamp()-System.currentTimeMillis())/1000d;
    }
    public boolean isExpired() {
        return System.currentTimeMillis() > cdTimeStamp;
    }

    //=====================Getters and Setters===================
    public void setDuration(double duration){
        this.duration = (int)(duration * 1000);
    }
    public double getDuration() {
        return this.duration / 1000d;
    }

    public void copyFrom(DurationEffect effects){
        super.copyFrom(effects);
        setDuration(effects.getRemainingTime());
    }
    // ====================On Start Runs================
    public void applyEffect() {
        if (_target == null || !_target.isOnline()){
            return;
        }
        if (_targetData.addEffects(this))
            setUpEffect();
        cdTimeStamp = System.currentTimeMillis() + duration;
    }
    public void registerAsSubEffect(DurationEffect effects){
        _isSubEffect = true;
        effects.copyFrom(this);
        effects.applyEffect();
        subEffectList.add(effects);
    }
    protected void registerPotionEffects(PotionEffect pe){
        getTargetData().addPotionEffect(pe, getTarget());
        potions.add(pe);
    }
    protected void registerTasks(BukkitRunnable br){
        taskList.put(br, br.runTaskLater(getPlugin(), secondsToTicks(getRemainingTime())));
    }
    protected void reapplyEffects(){
        for(Map.Entry<BukkitRunnable, BukkitTask> entry: taskList.entrySet()){
            entry.getValue().cancel();
            entry.setValue(entry.getKey().runTaskLater(getPlugin(), secondsToTicks(getRemainingTime())));
        }
        potions.forEach((pe)-> getTargetData().addPotionEffect(new PotionEffect(pe.getType(), secondsToTicks(getRemainingTime()), pe.getAmplifier(), false, false), getTarget()));
        subEffectList.forEach(DurationEffect::reapplyEffects);
    }
    public void removeEffect(){
        taskList.forEach((br, bt)->{
            bt.cancel();
            br.runTask(getPlugin());
        });
        potions.forEach((pe)->{
            _target.removePotionEffect(pe.getType());
        });
        subEffectList.forEach(DurationEffect::removeEffect);

    }


}
