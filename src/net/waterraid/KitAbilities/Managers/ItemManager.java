package net.waterraid.KitAbilities.Managers;

import com.Arhke.ArhkeLib.FileIO.ConfigManager;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemManager {
    public Map<String, ItemStack> itemMap = new HashMap<>();
    private final ConfigManager cm;

    private final String ItemNBTI = "ItemNBTI";
    public ItemManager(ConfigManager cm) {
        this.cm = cm;
        for (String key : cm.getConfig().getKeys(false)) {
            itemMap.put(key, cm.getConfig().getItemStack(key));
        }

    }
    //register kit
    public boolean registerKit(String id, ItemStack is) {
        if(itemMap.putIfAbsent(id, is) == null){
            NBTItem nbti = new NBTItem(is);
            nbti.setString(ItemNBTI, id);
            cm.set(is, id);
            cm.getFM().save();
            return true;
        }
        return false;
    }

    /**
     * returns whether or not the kit exists
     */
    public boolean removeKit(String kitId) {
        if (itemMap.remove(kitId) != null) {
            cm.set(null, kitId);
            cm.getFM().save();
            return true;
        }
        return false;
    }

    public ItemStack getItem(String id) {
        return itemMap.get(id);
    }

    public boolean isAKit(ItemStack is) {
        NBTItem nbti = new NBTItem(is);
        return nbti.hasKey(ItemNBTI) && itemMap.containsKey(nbti.getString(ItemNBTI));
    }


}
