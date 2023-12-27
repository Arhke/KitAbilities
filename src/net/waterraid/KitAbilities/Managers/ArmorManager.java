package net.waterraid.KitAbilities.Managers;

import net.waterraid.KitAbilities.Armor.Armor;
import net.waterraid.KitAbilities.FileIO.DataManager;
import net.waterraid.KitAbilities.FileIO.FileManager;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Utils.MainBase;

import java.util.HashMap;
import java.util.Map;

public class ArmorManager extends MainBase {
    public Map<String, Armor> armorKitsMap = new HashMap<>();
    DataManager _dm;
    FileManager _fm;


    public ArmorManager(Main Instance, FileManager fm, DataManager dm) {
        super(Instance);
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
