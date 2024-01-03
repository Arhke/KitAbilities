package net.waterraid.KitAbilities.Commands;

import net.waterraid.KitAbilities.Armor.Armor;
import com.Arhke.ArhkeLib.Lib.FileIO.DataManager;
import net.waterraid.KitAbilities.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

import static com.Arhke.ArhkeLib.Lib.Base.Base.tcm;
import static net.waterraid.KitAbilities.Main.getPlugin;

public class ArmorCommand extends CommandsBase implements CommandExecutor {
    DataManager _config;
    public static boolean checkPerm(CommandSender player, String string){
        return player.hasPermission(string);
    }
    public static boolean checkPermNoMsg(CommandSender player, String string){
        return checkPerm(player, string);
    }
    public ArmorCommand(Main instance){
        super(instance, new String[] {
                "/armor - Displays this msg",
                "/armor create <Name> <Upgrade> - Displays this msg",
                "/armor delete <Name> - Displays this msg",
                "/armor list - Lists all of the valid abilities",
                "/armor upgrades - Lists all of the valid upgrades",
                "/armor help - Displays this Help msg"
        });
        _config = getPlugin().getConfigManager().getDataManager("Commands", "Armor");
        List<String> helpList = _config.getStringList("Help");
        String[] helpString = helpList.toArray(new String[helpList.size()]);
        if(helpString.length == 0){
            _helpString = tcm(helpString);
        }
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command is only allowed to be used by players");
            return true;
        }
        Player player = (Player)commandSender;
        if((args.length >= 1)) {
            if (args[0].equalsIgnoreCase("create")) {
                MultiArray ma;
                if((ma = verifyArgs(commandSender, args, new Class[]{String.class}, "/armor create <name>")) != null){
                    if(checkPerm(player, _config.getString("Create", "Permission"))){

                        Armor aks = new Armor(args[1].toLowerCase(), player);
                        player.sendMessage(getPlugin().getArmorManager().registerKit(aks) ? ChatColor.GREEN + "Kit Created" : ChatColor.RED + "Specified Kit Already Exists");
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("delete")) {
                if(checkPerm(player, _config.getString("Delete", "Permission"))) {
                    player.sendMessage(!getPlugin().getArmorManager().removeKit(args[1].toLowerCase()) ? ChatColor.RED + "Kit Not Found" : ChatColor.GREEN + "Kit Removed");
                }
            }
            else if (args[0].equalsIgnoreCase("list")) {
                if(checkPerm(player, _config.getString("List", "Permission"))) {
                    Collection<Armor> aks = getPlugin().getArmorManager().armorKitsMap.values();
                    player.sendMessage(tcm(_config.getString("List", "SuccessHeader")));
                    for(Armor ak: aks){
                        player.sendMessage(tcm(_config.getString("List", "SuccessContent"), ak.getId()));
                    }
                }
            }
            else if(args[0].equalsIgnoreCase("help")) {
                player.sendMessage(getHelp());
            }
            else{
                Armor aks = getPlugin().getArmorManager().getArmorKit(args[0].toLowerCase());
                if (aks == null) {
                    player.sendMessage(tcm(_config.getString("NoArmorFound"), args[0].toLowerCase()));
                    return true;
                }
                if (!checkPermNoMsg(commandSender, tcm(_config.getString("Permission"), aks.getId()))) {
                    player.sendMessage(tcm(_config.getString("NoArmorPerm"), aks.getId()));
                    return true;
                }
                aks.claimKit(player);
                player.sendMessage(tcm(_config.getString("Success"), aks.getId()));
            }
        }else{
            player.sendMessage(getHelp());
        }
        return true;
    }
}
