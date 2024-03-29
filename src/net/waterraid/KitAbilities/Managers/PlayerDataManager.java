package net.waterraid.KitAbilities.Managers;

import com.Arhke.ArhkeLib.FileIO.DataManager;
import com.Arhke.ArhkeLib.FileIO.FileManager;
import net.waterraid.KitAbilities.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    public HashMap<UUID, PlayerData> dataMap = new HashMap<>();
    private final DataManager dm;
    private final FileManager fm;
    public PlayerDataManager(FileManager fm, DataManager dm){
        this.fm = fm;
        this.dm = dm;
        for (Player player:Bukkit.getOnlinePlayers()) {
            dataMap.put(player.getUniqueId(), new PlayerData(dm.getDataManager(player.getUniqueId().toString()), player));
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

    public void registerPlayer(Player player){
        dataMap.putIfAbsent(player.getUniqueId(), new PlayerData(dm.getDataManager(player.getUniqueId().toString()), player));
    }
    public void unregisterPlayer(Player player){
        PlayerData pd = dataMap.remove(player.getUniqueId());
        if (pd == null) return;
        pd.write(dm.getDataManager(player.getUniqueId().toString()));
        pd.getAbilityKit().refreshCoolDown();
        fm.save();
    }
    public void unregisterAll() {
        for(Map.Entry<UUID, PlayerData> dataEntry: dataMap.entrySet()){
            dataEntry.getValue().write(dm.getDataManager(dataEntry.getKey().toString()));
        }
        fm.save();
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
