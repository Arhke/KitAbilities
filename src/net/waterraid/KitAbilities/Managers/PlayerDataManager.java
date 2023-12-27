package net.waterraid.KitAbilities.Managers;

import net.waterraid.KitAbilities.FileIO.DataManager;
import net.waterraid.KitAbilities.FileIO.FileManager;
import net.waterraid.KitAbilities.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    public HashMap<UUID, PlayerData> dataMap = new HashMap<>();
    private DataManager _dm;
    private FileManager _fm;
    public PlayerDataManager(Main plugin, FileManager fm, DataManager dm){
        _fm = fm;
        _dm = dm;
        for (Player player:Bukkit.getOnlinePlayers()) {
            dataMap.put(player.getUniqueId(), new PlayerData(plugin, dm.getDataManager(player.getUniqueId().toString()), player));
        }
    }
    public PlayerData getData(UUID uuid){
        return dataMap.get(uuid);
    }
    public PlayerData getOrNewData(UUID uuid){
        PlayerData pd;
        if ((pd = dataMap.get(uuid)) == null){
            dataMap.put(uuid, pd = new PlayerData());
        }
        return pd;
    }

    public void registerPlayer(Main plugin, Player player){
        dataMap.putIfAbsent(player.getUniqueId(), new PlayerData(plugin, _dm.getDataManager(player.getUniqueId().toString()), player));
    }
    public void unregisterPlayer(Player player){
        PlayerData pd = dataMap.remove(player.getUniqueId());
        if (pd == null) return;
        pd.write(_dm.getDataManager(player.getUniqueId().toString()));
        pd.getAbilityKit().refreshCoolDown();
        _fm.save();
    }
    public void unregisterAll() {
        for(Map.Entry<UUID, PlayerData> dataEntry: dataMap.entrySet()){
            dataEntry.getValue().write(_dm.getDataManager(dataEntry.getKey().toString()));
        }
        _fm.save();
    }

    /**
     * Assumes that the specified kit exists
     */

    public void deathPlayer(UUID uuid){
        PlayerData pd = dataMap.get(uuid);
        if (pd == null) return;
        pd.removeAbility();
        pd.removeEffects();
        pd.removePotionQueue();
    }

}
