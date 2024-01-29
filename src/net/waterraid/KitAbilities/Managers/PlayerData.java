package net.waterraid.KitAbilities.Managers;

import net.waterraid.KitAbilities.Abilities.Templates.Abilities;
import net.waterraid.KitAbilities.Abilities.Templates.EnumAbilities;
import net.waterraid.KitAbilities.Effects.DurationEffect;
import com.Arhke.ArhkeLib.Lib.FileIO.DataManager;
import net.waterraid.KitAbilities.Main;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PlayerData {
    /* Nullable value */
    Abilities _aks;
    HashMap<Class<? extends DurationEffect>, DurationEffect> _effectList = new HashMap<>();
    HashMap<PotionEffectType, PotionQueue> potionQueueMap = new HashMap<>();
    public long shieldingCD = 0;
    private static final String AbilityKey = "abilitykit";
    public PlayerData(){}
    public PlayerData(DataManager dm, Player player){
        if (dm.getString(AbilityKey) == null) {
            return;
        }
        try{
            EnumAbilities eak = EnumAbilities.valueOf(dm.getString(AbilityKey));
            _aks = eak.get(player);
        }catch(IllegalArgumentException ignored){
        }
    }
    public void setAbility(Abilities aks) {
        if (_aks != null){
            aks.remove();
        }
        _aks = aks;
    }
    public void removeAbility() {
        if (_aks != null)
            _aks.remove();
    }
    public Abilities getAbilityKit() {
        return _aks;
    }
    public boolean addEffects(DurationEffect effect){
        DurationEffect e;
        if((e = _effectList.putIfAbsent(effect.getClass(), effect)) != null) {
            if(e.isExpired()){
                _effectList.put(effect.getClass(), effect);
                return true;
            }else{
                e.setNewTimeStamp((int)(effect.getDuration()/1000L) + System.currentTimeMillis());
                return false;
            }
        }
        return true;
    }
    public boolean hasEffect(Class<? extends DurationEffect> effect){
        return _effectList.get(effect) != null;
    }
    public Iterator<DurationEffect> getEffectIterator() {
        return _effectList.values().iterator();
    }
    public void write(DataManager dm){
        dm.set(_aks != null? _aks.getID():null, AbilityKey);
    }
    public void addPotionEffect(PotionEffect pe, Player player) {
        PotionQueue pq = potionQueueMap.get(pe.getType());
        if (pq == null) {
            potionQueueMap.put(pe.getType(), pq = new PotionQueue());
        }
        pq.addPotionQueue(new PotionData(pe.getType(), pe.getDuration()*50L, pe.getAmplifier()), player);
    }
    public void removePotionQueue(){
        for (PotionQueue pq: potionQueueMap.values()){
            pq.removeQueue();
        }
    }
    public void removeEffects() {
        for(DurationEffect effects:_effectList.values()) {
            effects.removeEffect();
        }
        _effectList.clear();
    }
    class PotionQueue {
        ArrayList<PotionData> dataQueue = new ArrayList<>();
        BukkitTask _bt;
        public void addPotionQueue(PotionData potionData, Player player) {

            for (int i = 0; i < dataQueue.size(); i++){
                if (potionData.getPotionStrength() > dataQueue.get(i).getPotionStrength()){
                    dataQueue.add(i, potionData);
                    if (i == 0){
                        potionData.applyPotion(player);
                        if(_bt != null )_bt.cancel();
                        _bt = potionRunnable(player, (potionData.getTimeStamp() - System.currentTimeMillis()) / 50);
                    }
                    return;
                }else if(potionData.getPotionStrength() == dataQueue.get(i).getPotionStrength() && potionData.getTimeStamp() > dataQueue.get(i).getTimeStamp()) {
                    dataQueue.get(i).setTimeStamp(potionData.getTimeStamp());
                    if (i == 0){
                        dataQueue.get(0).applyPotion(player);
                        if(_bt != null )_bt.cancel();
                        _bt = potionRunnable(player, (potionData.getTimeStamp() - System.currentTimeMillis()) / 50);
                    }
                    return;
                }else if (potionData.getTimeStamp() < dataQueue.get(i).getTimeStamp()){
                    return;
                }
            }
            dataQueue.add(potionData);
            if (dataQueue.size() == 1){
                potionData.applyPotion(player);
                if(_bt != null )_bt.cancel();
                _bt = potionRunnable(player, (potionData.getTimeStamp()-System.currentTimeMillis())/50);
            }
        }
        public BukkitTask potionRunnable(Player player, long delay){
            return new BukkitRunnable() {
                final Player _player = player;
                @Override
                public void run() {
                    if (dataQueue.size() == 0){
                        return;
                    }
                    dataQueue.remove(0);
                    while (dataQueue.size() != 0) {
                        PotionData pd = dataQueue.get(0);
                        if (pd.isExpired()){
                            dataQueue.remove(0);
                        }else {
                            pd.applyPotion(_player);
                            _bt = potionRunnable(player, (pd.getTimeStamp()-System.currentTimeMillis())/50);
                            return;
                        }
                    }
                }
            }.runTaskLater(Main.getPlugin(), delay);
        }
        public void removeQueue() {
            if(_bt != null) _bt.cancel();
        }
    }
    private class PotionData {
        PotionEffectType _pet;
        long _timeStamp;
        int _potionStrength;
        private PotionData(PotionEffectType pet, long duration, int PotionStrength){
            _pet = pet;
            _timeStamp = duration + System.currentTimeMillis();
            _potionStrength = PotionStrength;
        }
        private void setTimeStamp(long RemainingTime){
            _timeStamp = RemainingTime;
        }
        private long getTimeStamp(){
            return _timeStamp;
        }
        private int getPotionStrength() {
            return _potionStrength;
        }
        private boolean isExpired() {
            return _timeStamp < System.currentTimeMillis();
        }
        private void applyPotion(Player player) {
            player.addPotionEffect(new PotionEffect(_pet, (int)(_timeStamp-System.currentTimeMillis())/50, _potionStrength, true, true), true);
        }
    }

}
