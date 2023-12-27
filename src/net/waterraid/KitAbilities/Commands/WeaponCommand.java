package net.waterraid.KitAbilities.Commands;

import net.waterraid.KitAbilities.Armor.Weapons;
import net.waterraid.KitAbilities.FileIO.DataManager;
import net.waterraid.KitAbilities.Main;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WeaponCommand extends CommandsBase implements CommandExecutor {
    DataManager _config;
    public WeaponCommand(Main instance){
        super(instance, new String[] {
                "/weapon - Displays this Help Message",
                "/weapon create <name> <upgrade> - Creates a new Weapon Entry",
                "/weapon delete <name> - Deletes a weapon entry",
                "/weapon list - Lists all of the possible weaon entries",
                "/weapon help - displays this help Message"

        });
        _config = getPlugin().getConfigManager().getDataManager("Commands", "Weapon");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!isPlayer(sender)){
            sender.sendMessage(tcm(_config.getString("NotPlayer")));
            return true;
        }
        Player player = (Player)sender;
        if (args.length >= 1){
            if (args[0].equalsIgnoreCase("create")){
                if(checkPerm(player, _config.getString("Create", "Permission"))){
                    MultiArray ma;
                    if((ma = verifyArgs(player, args,
                            new Class[]{String.class, String.class}, "/weapon create <name> <upgrade>")) != null) {
                        ItemStack is = player.getItemInHand();
                        if (is == null || is.getType() == Material.AIR) {
                            player.sendMessage(tcm(_config.getString("Create", "InvalidItem")));
                            return true;
                        }
                        if (getPlugin().getWeaponManager().getWeapon(ma.getString(0).toLowerCase()) != null){
                            player.sendMessage(tcm(_config.getString("Create", "AlreadyExists")));
                            return true;
                        }
                        getPlugin().getWeaponManager().registerWeapon(ma.getString(0).toLowerCase(),
                                new Weapons(ma.getString(0).toLowerCase(), is));
                        player.sendMessage(tcm(_config.getString("Create", "Success"), ma.getString(0).toLowerCase()));
                    }
                }
            }else if(args[0].equalsIgnoreCase("delete")){
                if(checkPerm(player, _config.getString("Delete", "Permission"))){
                    if (args.length < 2){
                        player.sendMessage(tcm(_config.getString("Delete", "IncorrectArgs")));
                        return true;
                    }
                    if(getPlugin().getWeaponManager().removeWeapon(args[1].toLowerCase())){
                        player.sendMessage(tcm(_config.getString("Delete", "Success"), args[1].toLowerCase()));
                    }else{
                        player.sendMessage(tcm(_config.getString("Delete", "EntryNotFound")));
                    }
                }
            }else if(args[0].equalsIgnoreCase("list")){
                if(checkPerm(player, _config.getString("List", "Permission"))){
                    player.sendMessage(tcm(_config.getString("List", "SuccessHeader")));
                    for(String key:getPlugin().getWeaponManager().getWeaponKeys()){
                        player.sendMessage(tcm(_config.getString("List", "SuccessContent"), key));
                    }
                }
            }else if(args[0].equalsIgnoreCase("help")){
                player.sendMessage(getHelp());
            }
            else{
                Weapons weapon = getPlugin().getWeaponManager().getWeapon(args[0].toLowerCase());
                if(weapon == null){
                    player.sendMessage(tcm(_config.getString("EntryNotFound")));
                    return true;
                }
                if(checkPermNoMsg(player, tcm(_config.getString("ClaimPermission"), weapon.getId()))){
                    if(player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(tcm(_config.getString("NoInventorySpace")));
                        return true;
                    }
                    player.sendMessage(tcm(_config.getString("Success"), weapon.getId()));
                    player.getInventory().addItem(weapon.getWeapon());
                }else{
                    player.sendMessage(tcm(_config.getString("InsufficientPerms")));
                }
            }

        }else{
            player.sendMessage(getHelp());
        }
        return true;

    }
}
