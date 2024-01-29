package net.waterraid.KitAbilities.Commands;

import com.Arhke.ArhkeLib.Lib.Base.CommandsBase;
import com.Arhke.ArhkeLib.Lib.FileIO.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WeaponCommand extends CommandsBase {

    public WeaponCommand(String command, ConfigManager dm) {
        super(command, dm);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
//        if(!isPlayer(sender)){
//            sender.sendMessage(tcm(_config.getString("NotPlayer")));
//            return true;
//        }
//        Player player = (Player)sender;
//        if (args.length >= 1){
//            if (args[0].equalsIgnoreCase("create")){
//                if(checkPerm(player, _config.getString("Create", "Permission"))){
//                    MultiArray ma;
//                    if((ma = verifyArgs(player, args,
//                            new Class[]{String.class, String.class}, "/weapon create <name> <upgrade>")) != null) {
//                        ItemStack is = player.getItemInHand();
//                        if (is == null || is.getType() == Material.AIR) {
//                            player.sendMessage(tcm(_config.getString("Create", "InvalidItem")));
//                            return true;
//                        }
//                        if (getPlugin().getWeaponManager().getWeapon(ma.getString(0).toLowerCase()) != null){
//                            player.sendMessage(tcm(_config.getString("Create", "AlreadyExists")));
//                            return true;
//                        }
//                        getPlugin().getWeaponManager().registerWeapon(ma.getString(0).toLowerCase(),
//                                new Weapons(ma.getString(0).toLowerCase(), is));
//                        player.sendMessage(tcm(_config.getString("Create", "Success"), ma.getString(0).toLowerCase()));
//                    }
//                }
//            }else if(args[0].equalsIgnoreCase("delete")){
//                if(checkPerm(player, _config.getString("Delete", "Permission"))){
//                    if (args.length < 2){
//                        player.sendMessage(tcm(_config.getString("Delete", "IncorrectArgs")));
//                        return true;
//                    }
//                    if(getPlugin().getWeaponManager().removeWeapon(args[1].toLowerCase())){
//                        player.sendMessage(tcm(_config.getString("Delete", "Success"), args[1].toLowerCase()));
//                    }else{
//                        player.sendMessage(tcm(_config.getString("Delete", "EntryNotFound")));
//                    }
//                }
//            }else if(args[0].equalsIgnoreCase("list")){
//                if(checkPerm(player, _config.getString("List", "Permission"))){
//                    player.sendMessage(tcm(_config.getString("List", "SuccessHeader")));
//                    for(String key:getPlugin().getWeaponManager().getWeaponKeys()){
//                        player.sendMessage(tcm(_config.getString("List", "SuccessContent"), key));
//                    }
//                }
//            }else if(args[0].equalsIgnoreCase("help")){
//                player.sendMessage(getHelp());
//            }
//            else{
//                Weapons weapon = getPlugin().getWeaponManager().getWeapon(args[0].toLowerCase());
//                if(weapon == null){
//                    player.sendMessage(tcm(_config.getString("EntryNotFound")));
//                    return true;
//                }
//                if(checkPermNoMsg(player, tcm(_config.getString("ClaimPermission"), weapon.getId()))){
//                    if(player.getInventory().firstEmpty() == -1) {
//                        player.sendMessage(tcm(_config.getString("NoInventorySpace")));
//                        return true;
//                    }
//                    player.sendMessage(tcm(_config.getString("Success"), weapon.getId()));
//                    player.getInventory().addItem(weapon.getWeapon());
//                }else{
//                    player.sendMessage(tcm(_config.getString("InsufficientPerms")));
//                }
//            }
//
//        }else{
//            player.sendMessage(getHelp());
//        }
        return true;

    }

    @Override
    public void setDefaults() {

    }
}
