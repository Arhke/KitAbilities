package net.waterraid.KitAbilities.Commands.AbilityCommands;

import com.Arhke.ArhkeLib.Lib.GUI.InventoryGui;
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
    }
    public SetupGUI() {
        super(5, "Setup");
    }

    @Override
    public void setItems(Player player) {
        setItem(11, new ItemStack(Material.SKULL_ITEM));
        setItem(29, new ItemStack(Material.IRON_SWORD));;
        setItem(30, getPlugin().getPDManager().getOrNewData(player.getUniqueId()).getAbilityKit().getItemStack(), (a)->{
            //open ability select GUI
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
