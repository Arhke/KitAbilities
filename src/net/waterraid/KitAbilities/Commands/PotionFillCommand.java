package net.waterraid.KitAbilities.Commands;

import com.Arhke.ArhkeLib.Lib.Base.CommandsBase;
import com.Arhke.ArhkeLib.Lib.FileIO.ConfigManager;
import net.waterraid.KitAbilities.Managers.PlayerData;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import static com.Arhke.ArhkeLib.Lib.Base.Base.tcm;
import static net.waterraid.KitAbilities.Main.getPlugin;

public class PotionFillCommand extends CommandsBase implements CommandExecutor {
    public static ItemStack SplashHeal;

    static {
        Potion potion = new Potion(PotionType.INSTANT_HEAL);
        potion.setLevel(2);
        potion.setSplash(true);
        SplashHeal = potion.toItemStack(1);
    }


    public PotionFillCommand(String potionfill, ConfigManager config) {
        super(potionfill, config);
    }

    @Override
    public void setDefaults() {

    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (strings.length == 1 && strings[0].equalsIgnoreCase("clear")) {
                PlayerData pd = getPlugin().getPDManager().getOrNewData(player.getUniqueId());
                if ((player.isSneaking() && !pd._remove) || (pd._remove && !player.isSneaking())) {
                    for (int i = 0; i < player.getInventory().getSize(); i++) {
                        if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).isSimilar(SplashHeal)) {
                            player.getInventory().setItem(i, null);
                        }
                    }
                } else {
                    player.getInventory().clear();
                    player.getInventory().setArmorContents(new ItemStack[]{new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1)});
                }
                return true;
            } else if (strings.length == 1 && strings[0].equalsIgnoreCase("toggle")) {
                PlayerData pd = getPlugin().getPDManager().getOrNewData(player.getUniqueId());
                if (pd._remove) {
                    pd._remove = false;
                    player.sendMessage(tcm("&7You have turned Quick Claim &a&lOn." +
                            "\n&6Right Click to Quick Claim, Shift + Right CLick to Normal Claim"));
                } else {
                    pd._remove = true;
                    player.sendMessage(tcm("&7You have turned Quick Claim &l&cOff." +
                            "\n&6Right Click to Normal Claim, Shift + Right CLick to Quick Claim"));
                }
                return true;
            }
            Inventory inventory = player.getInventory();
            for (int i = 0; i < inventory.getSize(); i++) {
                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, SplashHeal);
                }
            }

        }


        return true;
    }
}
