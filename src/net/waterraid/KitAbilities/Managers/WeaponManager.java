package net.waterraid.KitAbilities.Managers;

import com.Arhke.ArhkeLib.FileIO.DataManager;
import com.Arhke.ArhkeLib.FileIO.FileManager;
import net.waterraid.KitAbilities.Armor.Weapons;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WeaponManager  {
    DataManager _dm;
    FileManager _fm;
    Map<String, Weapons> weapons = new HashMap<>();
    public WeaponManager(FileManager fm, DataManager dm){
        _dm = dm;
        _fm = fm;
        for(String key:dm.getConfig().getKeys(false)){
            weapons.put(key, new Weapons(key, dm.getDataManager(key)));
        }
    }
    public Weapons getWeapon(String id){
        return weapons.get(id);
    }
    public void registerWeapon(String id, Weapons weapon){
        weapons.putIfAbsent(id, weapon);
        weapon.write(_dm.getDataManager(id));
        _fm.save();
    }
    public boolean removeWeapon(String id){
        if (weapons.remove(id) != null){
            _dm.getDataManager(id).delete(_fm);
            _fm.save();
            return true;
        }
        return false;
    }
    public Collection<String> getWeaponKeys(){
        return weapons.keySet();
    }

}
