package net.waterraid.KitAbilities.Managers;

import com.Arhke.ArhkeLib.Lib.FileIO.DataManager;
import com.Arhke.ArhkeLib.Lib.FileIO.FileManager;
import net.waterraid.KitAbilities.Armor.Weapons;
import net.waterraid.KitAbilities.Main;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WeaponManager  {
    DataManager _dm;
    FileManager _fm;
    Map<String, Weapons> _weapons = new HashMap<String, Weapons>();
    public WeaponManager(Main instance, FileManager fm, DataManager dm){
        _dm = dm;
        _fm = fm;
        for(String key:dm.getConfig().getKeys(false)){
            _weapons.put(key, new Weapons(key, dm.getDataManager(key)));
        }
    }
    public Weapons getWeapon(String id){
        return _weapons.get(id);
    }
    public void registerWeapon(String id, Weapons weapon){
        _weapons.putIfAbsent(id, weapon);
        weapon.write(_dm.getDataManager(id));
        _fm.save();
    }
    public boolean removeWeapon(String id){
        if (_weapons.remove(id) != null){
            _dm.getDataManager(id).delete(_fm);
            _fm.save();
            return true;
        }
        return false;
    }
    public Collection<String> getWeaponKeys(){
        return _weapons.keySet();
    }

}
