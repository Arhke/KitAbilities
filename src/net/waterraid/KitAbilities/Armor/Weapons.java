package net.waterraid.KitAbilities.Armor;

import com.Arhke.ArhkeLib.Lib.FileIO.DataManager;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public class Weapons {
    String _id;
    ItemStack _weapon;
    public static final String NBTIWeapon = "WeaponId";
    public static final String WeaponKey = "weapons", UpgradeKey = "upgrades";
    public Weapons(String Id, ItemStack is){
        _id = Id;
        NBTItem nbti = new NBTItem(is);
        nbti.setString(NBTIWeapon, Id);
        _weapon = nbti.getItem();
    }

    public Weapons(String Id, DataManager dm){
        _id = Id;
        _weapon = dm.getConfig().getItemStack(WeaponKey);
    }
    public String getId(){
        return _id;
    }
    public ItemStack getWeapon(){
        return new ItemStack(_weapon);
    }
    public void write(DataManager dm){
        dm.set(_weapon, WeaponKey);
    }



}
