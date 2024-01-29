package net.waterraid.KitAbilities.Commands.AbilityCommands;

import com.Arhke.ArhkeLib.Lib.GUI.InventoryGui;
import net.waterraid.KitAbilities.Abilities.Templates.Abilities;
import net.waterraid.KitAbilities.Abilities.Templates.EnumAbilities;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Locale;

import static net.waterraid.KitAbilities.Main.getPlugin;

public class AbilitySelectGUI extends InventoryGui {
    public AbilitySelectGUI() {
        super(EnumAbilities.values().length / 9 + 1, "Setup");
    }

    @Override
    public void setItems(Player player) {
        int i = 0;
        for (EnumAbilities ea : EnumAbilities.values()) {
            Abilities ability = ea.get(player);
            if (player.hasPermission("kitabilities.ability." + ea.name().toLowerCase(Locale.ROOT))) {
                setItem(i++, ability.getItemStack(), (a) -> {
                    getPlugin().getPDManager().getOrNewData(player.getUniqueId()).setAbility(ea.get(player));
                });
            } else {
                setItem(i++, new ItemStack(Material.BARRIER));
            }
        }
        fillRest(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));
    }

    @Override
    public void onOpen(InventoryOpenEvent inventoryOpenEvent) {

    }

    @Override
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {

    }
}

