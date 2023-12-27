package net.waterraid.KitAbilities.Armor;

import com.Arhke.ArhkeLib.Lib.CustomEvents.ArmorEquipEvent;
import com.Arhke.ArhkeLib.Lib.CustomEvents.ArmorType;
import com.Arhke.ArhkeLib.Lib.FileIO.DataManager;
import de.tr7zw.nbtapi.NBTItem;
import net.waterraid.KitAbilities.Utils.Base;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Armor extends Base {
    String _id;
    ItemStack[] _armor = new ItemStack[4];

    public static final String NBTIArmor = "ArmorId";
    public static final String ArmorKey = "armors";
    //-----------------<Constructors>-------------------
    public Armor(String Id, Player player){
        _id = Id;
        ItemStack[] Armor = player.getInventory().getArmorContents();
        _armor = new ItemStack[Armor.length];
        for(int i = 0; i< Armor.length; i++){
            if (Armor[i] == null || Armor[i].getType() == Material.AIR){
                continue;
            }
            _armor[i] = this.applyItemNBT(Armor[i]);
        }

    }
    public Armor(String Id, DataManager dm){
        _id = Id;
        DataManager armor = dm.getDataManager(ArmorKey);
        for(int i = 0; i < 4 ; i++){
            _armor[i] = armor.getConfig().getItemStack(i+"");
        }
     }

    //-------------<Getters and Setters>-------------

    public String getId() {
        return _id;
    }
    public ItemStack[] getArmor() {
        return _armor;
    }

    public void write(DataManager dm) {
        for (int i = 0; i < _armor.length; i++) {
            dm.set(getArmor()[i], ArmorKey, i+"");
        }
    }

    //---------------<Override Methods>---------------

    @Override
    public int hashCode() {
        return _id.hashCode();
    }
    @Override
    public boolean equals(Object o) {
        return o instanceof Armor && _id.equals(((Armor) o).getId());
    }

    //----------------<Utility Methods>------------------
    /**
     * Apply isKit Boolean Value; Apply kitId String Value;
     */
    public ItemStack applyItemNBT(ItemStack is) {
        if(is == null){
            return null;
        }
        NBTItem nbti = new NBTItem(is);
        nbti.setString(NBTIArmor, this.getId());
        return nbti.getItem();
    }
    public void putArmorOnPlayer(Player player) {
        for(int i = 0; i < _armor.length; i++){

            ArmorEquipEvent aee = new ArmorEquipEvent(player, ArmorEquipEvent.EquipMethod.DRAG, ArmorType.matchSlot(i), player.getInventory().getArmorContents()[i], _armor[i]);
            Bukkit.getServer().getPluginManager().callEvent(aee);
            if(aee.isCancelled()){
                return;
            }
        }
        player.getInventory().setArmorContents(_armor);
    }
    public void claimKit(Player player){
        if (player == null){
            return;
        }
        for (ItemStack is: player.getInventory().getArmorContents()){
            if(is == null || is.getType() == Material.AIR){
                continue;
            }
            addItemtoPlayer(player, is);
        }
        putArmorOnPlayer(player);

    }
}
