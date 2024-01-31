package net.waterraid.KitAbilities.Commands.AbilityCommands;

import com.Arhke.ArhkeLib.GUI.InventoryGui;
import com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags;
import com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes;
import net.waterraid.KitAbilities.Armor.Armor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import static net.waterraid.KitAbilities.Main.getPlugin;

public class SetupGUI extends InventoryGui {
    static ItemStack cp = new ItemStack(Material.LEATHER_CHESTPLATE),lg = new ItemStack(Material.LEATHER_LEGGINGS), bt =  new ItemStack(Material.LEATHER_BOOTS);
    static{
        setColor(cp, Color.WHITE);
        setColor(lg, Color.WHITE);
        setColor(bt, Color.WHITE);
        ArmorTags at = new ArmorTags(cp);
        at.applyTag(Attributes.MCAttributes.ARMOR, ArmorTags.Slot.CHESTPLATE, ArmorTags.Operations.ADD, 5);
        cp = at.getItem();
        at = new ArmorTags(lg);
        at.applyTag(Attributes.MCAttributes.ARMOR, ArmorTags.Slot.LEGGINGS, ArmorTags.Operations.ADD, 5);
        lg = at.getItem();
        at = new ArmorTags(bt);
        at.applyTag(Attributes.MCAttributes.ARMOR, ArmorTags.Slot.BOOTS, ArmorTags.Operations.ADD, 5);
        bt = at.getItem();
    }
    public SetupGUI(Player player) {

        super(5, "Setup", player);
    }

    @Override
    public void setItems(Player player) {
        setItem(11, new ItemStack(Material.SKULL_ITEM));
        setItem(29, new ItemStack(Material.IRON_SWORD));;
        setItem(30, getPlugin().getPDManager().getOrNewData(player.getUniqueId()).getAbilityKit().getItemStack(), (a)->{
            //open ability select GUI
            getPlugin().getGUIManager().openGUI(player, new AbilitySelectGUI(player));
        });
        setItem(16, new ItemStack(cp));
        setItem(25, new ItemStack(lg));
        setItem(34, new ItemStack(bt));
        fillRest(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7));
    }
    public static void setColor(ItemStack is, Color c){
        LeatherArmorMeta meta2 = (LeatherArmorMeta) is.getItemMeta();
        meta2.setColor(c);
        is.setItemMeta(meta2);
    }

    @Override
    public void onOpen(InventoryOpenEvent inventoryOpenEvent) {

    }

    @Override
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {

    }
}
