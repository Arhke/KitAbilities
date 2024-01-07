package net.waterraid.KitAbilities.Utils;

import com.Arhke.ArhkeLib.Lib.Base.Base;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static com.Arhke.ArhkeLib.Lib.Base.Base.tcm;

public class ArmorAccessories {


    ItemStack is;
    List<String> otherLore = new ArrayList<>();
    Map<Attributes.MCAttributes, Integer> mcAttr = new HashMap<>();
    Map<Attributes.CustomAttributes, Integer> customAttr = new HashMap<>();
    Set<SetBonus> setBonus = new HashSet<>();
    List<String> accessories = new ArrayList<>();
    int slots = 0;
    static String filterString(String a){
        return a;
    }
    public ArmorAccessories(ItemStack is) {
        this.is = is;
        List<String> lore = is.getItemMeta().getLore();
        if (lore == null || lore.size() < 1) {
            return;
        }
        for (int i = 0; i < lore.size(); i++) {
            String lineLore = lore.get(i).toUpperCase();
            if(lineLore.length() == 0){
                continue;
            }
            if(lore.get(i).contains("⬤ ")){
                accessories.add(lore.get(i).replace("⬤ ",""));
                continue;
            }
            if (filterString(lineLore).contains("SETBONUS")) {
                try {
                    setBonus.add(SetBonus.valueOf(filterString(lineLore).replace("SETBONUS", "")));
                }catch(IllegalArgumentException ignored){}
                continue;
            }
            if(filterString(lineLore).contains("EMPTYSLOT")){
                slots++;
                continue;
            }


            int last = lineLore.lastIndexOf(' ');
            if (last == -1) {
                otherLore.add(lore.get(i));
                continue;
            }
            Attributes attr = Attributes.getAttribute(filterString(lineLore.substring(0, last).toUpperCase()));
            if (attr == null) {
                otherLore.add(lore.get(i));
                continue;
            }
            int modifier;
            try {
                modifier = Integer.parseInt(filterString(lineLore.substring(last + 1)));
            } catch (NumberFormatException e) {
                otherLore.add(lore.get(i));
                continue;
            }

            if (attr instanceof Attributes.MCAttributes) {
                mcAttr.put((Attributes.MCAttributes) attr, modifier);
            } else if (attr instanceof Attributes.CustomAttributes) {
                customAttr.put((Attributes.CustomAttributes) attr, modifier);
            }
        }


    }

    private ArmorAccessories() {
    }

    public static ArmorAccessories parseCustom(ItemStack is) {
        List<String> lore = is.getItemMeta().getLore();

        ArmorAccessories ret = new ArmorAccessories();
        if (lore == null) {
            return ret;
        }
        for (int i = 0; i < lore.size(); i++) {
            String lineLore = lore.get(i).toUpperCase();
            if (filterString(lineLore).contains("SETBONUS")) {
                try{
                    ret.getSetBonus().add(SetBonus.valueOf(filterString(lineLore).replace("SETBONUS", "")));
                }catch(IllegalArgumentException ignored){}
                continue;
            }
            int last = lineLore.lastIndexOf(' ');
            if (last == -1) continue;
            Attributes attr = Attributes.CustomAttributes.getAttribute(filterString(lineLore.substring(0, last).toUpperCase()));
            if (attr == null) {
                continue;
            }
            int modifier;
            try {
                modifier = Integer.parseInt(filterString(lineLore.substring(last + 1)));
            } catch (NumberFormatException e) {
                continue;
            }
            ret.getCustomAttr().put((Attributes.CustomAttributes) attr, modifier);
        }
        return ret;
    }
    public static ArmorAccessories parseSetBonus(ItemStack is){

        ArmorAccessories ret = new ArmorAccessories();
        if(is.getItemMeta() == null || is.getItemMeta().getLore() == null) {
            return ret;
        }
        List<String> lore = is.getItemMeta().getLore();

        for (int i = 0; i < lore.size(); i++) {
            String lineLore = lore.get(i).toUpperCase();
            if (filterString(lineLore).contains("SETBONUS")) {
                ret.getSetBonus().add(SetBonus.valueOf(filterString(lineLore).replace("SETBONUS", "")));
            }
        }
        return ret;
    }

    public void regenerateLore() {
        List<String> lore = new ArrayList<>(otherLore);

        if(otherLore.size() != 0){
            lore.add("");
        }
        for (Map.Entry<Attributes.MCAttributes, Integer> entry : this.getMcAttr().entrySet()) {
            if(entry.getValue() == 0){
                continue;
            }
            lore.add(tcm("{0}{1} {2}%", (entry.getValue() > 0 ? ChatColor.BLUE : ChatColor.RED), entry.getKey().getVerbose(), (entry.getValue() > 0 ? "+":"-")+Math.abs(entry.getValue())));
        }
        for (Map.Entry<Attributes.CustomAttributes, Integer> entry : this.getCustomAttr().entrySet()) {
            if(entry.getValue() == 0){
                continue;
            }
            if (entry.getKey() == Attributes.CustomAttributes.DEFLECT || entry.getKey() == Attributes.CustomAttributes.DODGE ||
                    entry.getKey() == Attributes.CustomAttributes.HEALING || entry.getKey() == Attributes.CustomAttributes.DURABILITY ||
                    entry.getKey() == Attributes.CustomAttributes.RANGEDDAMAGE || entry.getKey() == Attributes.CustomAttributes.DEFENSE) {
                lore.add(tcm("{0}{1} {2}%", (entry.getValue() > 0 ? ChatColor.BLUE : ChatColor.RED), entry.getKey().getVerbose(), (entry.getValue() > 0 ? "+":"-")+Math.abs(entry.getValue())));
            } else {
                lore.add(tcm("{0}{1} {2}", (entry.getValue() > 0 ? ChatColor.BLUE : ChatColor.RED), entry.getKey().getVerbose(), (entry.getValue() > 0 ? "+":"-")+Math.abs(entry.getValue())));
            }

        }
        for (SetBonus sb : getSetBonus()) {
            lore.add(tcm("&6&lSet Bonus&r&7: &f{0}", sb.getVerbose()));
        }
        lore.add("");
        for(String s:accessories){
            lore.add(ChatColor.WHITE + "⬤ " + s);
        }
        for(int i = 0; i < slots;i++) {
            lore.add(tcm("&8{Empty Slot}"));
        }
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
    }

    public boolean addAccessory(ArmorAccessories aa) {
        if (slots <= 0) {
            return false;
        }
        if(aa.getIs().getItemMeta() != null && aa.getIs().getItemMeta().getDisplayName() != null) {
            accessories.add(aa.getIs().getItemMeta().getDisplayName());
        }else{
            accessories.add(Base.capitalize(aa.getIs().getType().name().replace("_"," ").toLowerCase()));
        }
        NBTItem nbti = new NBTItem(is);
        ArmorTags at = new ArmorTags(nbti);

        for (Map.Entry<Attributes.MCAttributes, Integer> entry : aa.getMcAttr().entrySet()) {
            Integer i;
            if ((i = mcAttr.putIfAbsent(entry.getKey(), Math.min(100, entry.getValue()))) != null) {
                mcAttr.put(entry.getKey(), i + entry.getValue());
            }
            if(entry.getKey() == Attributes.MCAttributes.SPEED){
                at.applyTag(entry.getKey(), ArmorTags.Operations.ADD, 0.001*entry.getValue());
            }else if(entry.getKey() == Attributes.MCAttributes.KNOCKBACKRESIST) {
                at.applyTag(entry.getKey(), ArmorTags.Operations.ADD, 0.01*entry.getValue());
            }else if(entry.getKey() == Attributes.MCAttributes.MAXHEALTH){
                at.applyTag(entry.getKey(), ArmorTags.Operations.MULTIPLY, entry.getValue()*0.01d);
            }

        }
        at.hideFlags(ArmorTags.Flag.ATTRIBUTEMODIFIERS);
        is.setItemMeta(at.getItem().getItemMeta());
        for (Map.Entry<Attributes.CustomAttributes, Integer> entry : aa.getCustomAttr().entrySet()) {
            Integer i;
            if ((i = customAttr.putIfAbsent(entry.getKey(), entry.getValue())) != null)
                customAttr.put(entry.getKey(), i + entry.getValue());
        }
        setBonus.addAll(aa.getSetBonus());
        slots--;
        return true;

    }

    public Map<Attributes.MCAttributes, Integer> getMcAttr() {
        return mcAttr;
    }

    public Map<Attributes.CustomAttributes, Integer> getCustomAttr() {
        return customAttr;
    }

    public Set<SetBonus> getSetBonus() {
        return setBonus;
    }

    public ItemStack getIs() {
        return is;
    }
}
