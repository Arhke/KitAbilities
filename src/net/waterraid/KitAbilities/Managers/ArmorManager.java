package net.waterraid.KitAbilities.Managers;

import com.Arhke.ArhkeLib.Lib.Base.MainBase;
import net.waterraid.KitAbilities.Armor.Armor;
import com.Arhke.ArhkeLib.Lib.FileIO.DataManager;
import com.Arhke.ArhkeLib.Lib.FileIO.FileManager;
import net.waterraid.KitAbilities.Main;
import java.util.HashMap;
import java.util.Map;

public class ArmorManager{
    public Map<String, Armor> armorKitsMap = new HashMap<>();
    DataManager _dm;
    FileManager _fm;


    public ArmorManager(FileManager fm, DataManager dm) {
        _dm = dm;
        _fm = fm;
        for (String key : dm.getConfig().getKeys(false)) {
            armorKitsMap.put(key, new Armor(key, dm.getDataManager(key)));
        }

    }
    //register kit
    public boolean registerKit(Armor ak) {
        if (ak == null || armorKitsMap.containsKey(ak.getId())) return false;
        armorKitsMap.put(ak.getId(), ak);
        ak.write(_dm.getDataManager(ak.getId()));
        _fm.save();
        return true;
    }

    /**
     * returns whether or not the kit exists
     */
    public boolean removeKit(String kitId) {
        if (armorKitsMap.remove(kitId) != null) {
            _dm.set(null, kitId);
            _fm.save();
            return true;
        }
        return false;
    }

    public Armor getArmorKit(String kitId) {
        return armorKitsMap.get(kitId);
    }

    public boolean isAKit(String kitId) {
        return armorKitsMap.containsKey(kitId);
    }


}
