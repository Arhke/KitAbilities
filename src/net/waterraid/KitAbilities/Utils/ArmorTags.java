package net.waterraid.KitAbilities.Utils;

import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ArmorTags {
    NBTItem _nbti;
    public static final String attrModifiers = "AttributeModifiers";
    public static final String Amount = "Amount",
    AttributeName = "AttributeName", Name = "Name", Operation = "Operation", UUIDLeast = "UUIDLeast",
    UUIDMost = "UUIDMost";
    public static final String HideFlags = "HideFlags";
    public ArmorTags(ItemStack itemStack) {
        _nbti = new NBTItem(itemStack);
    }
    public ArmorTags(NBTItem nbti) {
        _nbti = nbti;
    }
    public void applyTag(Attributes.MCAttributes attribute, Operations operation, double strength){
        applyTag(attribute.getName()+operation, attribute, operation, strength);
    }
    public void applyTag(String name, Attributes.MCAttributes attribute, Operations operation, double strength){
        NBTCompoundList attributeModifiers = _nbti.getCompoundList(attrModifiers);
        for (NBTListCompound NLC : attributeModifiers) {
            if (NLC.getString(Name).equals(name)) {
                NLC.setDouble(Amount, NLC.getDouble(Amount) + strength);
                NLC.setInteger(Operation, operation.getID());
                return;
            }
        }

//        attr.setString("Slot", slot);

        NBTListCompound NLC = attributeModifiers.addCompound();
        NLC.setDouble(Amount, strength);
        NLC.setString(AttributeName,  attribute.getName());
        NLC.setString(Name, name);
        NLC.setInteger(Operation, operation.getID());
        NLC.setInteger(UUIDLeast, Base.randInt(999999));
        NLC.setInteger(UUIDMost, Base.randInt(999999));
    }
    public void hideFlags(Flag... flags){
        int count = 0;
        for(Flag flag:flags){
            count += flag.getID();
        }
        _nbti.setInteger(HideFlags,count);
    }
    public ItemStack getItem(){
        return _nbti.getItem();
    }

    public enum Operations {
        ADD(0), MULTIPLYBASE(1), MULTIPLY(2);
        final int _id;
        Operations(int id){
            _id = id;
        }
        public int getID() {
            return _id;
        }
    }
    public enum Flag {
        ENCHANTMENTS(1), ATTRIBUTEMODIFIERS(2), UNBREAKABLE(4), CANDESTROY(8), CANPLACEMOBS(16), OTHER(32), DYED(64);
        final int _id;
        Flag(int id){
            _id = id;
        }
        public int getID(){
            return _id;
        }
    }
}
