package net.waterraid.KitAbilities.Commands.AbilityCommands;

import com.Arhke.ArhkeLib.Base.Base;
import com.Arhke.ArhkeLib.GUI.InventoryGui;
import net.waterraid.KitAbilities.Abilities.Templates.Abilities;
import net.waterraid.KitAbilities.Abilities.Templates.EnumAbilities;
import net.waterraid.KitAbilities.Managers.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

import static net.waterraid.KitAbilities.Main.getPlugin;

public class AbilitySelectGUI extends InventoryGui {
    public AbilitySelectGUI(Player player) {
        super(EnumAbilities.values().length / 9 + 1, "Setup",player);
    }

    @Override
    public void setItems(Player player) {
        int i = 0;
        for (EnumAbilities ea : EnumAbilities.values()) {
            Abilities ability = ea.get(player);
            if (player.hasPermission("kitabilities.ability." + ea.name().toLowerCase(Locale.ROOT))) {
                ItemStack is = ability.getItemStack();
                PlayerData pd = getPlugin().getPDManager().getOrNewData(player.getUniqueId());
                if(ability.getID().equals(pd.getAbilityKit().getID())){
                    Base.highlightItem(is);
                    setItem(i++, is, (a) -> {
                        player.sendMessage(ChatColor.GREEN + "You already have the " + Base.capitalize(ea.name()) + " ability selected.");
                    });
                }else{
                    setItem(i++, is, (a) -> {
                        player.sendMessage(ChatColor.GREEN + "You have selected the " + Base.capitalize(ea.name()) + " ability.");
                        getPlugin().getPDManager().getOrNewData(player.getUniqueId()).setAbility(ability);
                        getPlugin().getGUIManager().openGUI(player, new SetupGUI(player));
                    });
                }
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
        getPlugin().getGUIManager().openGUI((Player)inventoryCloseEvent.getPlayer(), new SetupGUI((Player)inventoryCloseEvent.getPlayer()));
    }
}

