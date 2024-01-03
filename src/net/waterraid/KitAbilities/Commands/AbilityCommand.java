package net.waterraid.KitAbilities.Commands;

import com.Arhke.ArhkeLib.Lib.Base.CommandsBase;
import com.Arhke.ArhkeLib.Lib.FileIO.ConfigManager;
import de.tr7zw.nbtapi.NBTItem;
import net.waterraid.KitAbilities.Abilities.Templates.Abilities;
import net.waterraid.KitAbilities.Abilities.Templates.EnumAbilities;
import net.waterraid.KitAbilities.Commands.AbilityCommands.ListCommand;
import net.waterraid.KitAbilities.Commands.AbilityCommands.ReloadCommand;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.Arhke.ArhkeLib.Lib.Base.Base.tcm;
import static net.waterraid.KitAbilities.Main.getPlugin;

public class AbilityCommand extends CommandsBase{
    public AbilityCommand(Main instance, ConfigManager dm) {
        super(instance, "ability", dm);
        registerSubCommand(new ListCommand(this, "list"));
        registerSubCommand(new ReloadCommand(this));
//            "/ability - Displays this msg",
//                    "/ability list - Lists all of the valid abilities",
//                    "/ability help - Displays this Help Msg"
//        List<String> helpList = _config.getStringList("Help");
//        String[] helpString = helpList.toArray(new String[helpList.size()]);
//        if(helpString.length == 0){
//            _helpString = tcm(helpString);
//        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!processSubCommands(sender, args)) {
            Player player;
            if (args.length == 2) {
                if(!sender.isOp()){
                    sender.sendMessage("you need to be op or console for this");
                    return true;
                }
                player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(dm.getTCM(notFound));
                    return true;
                }
            } else {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(dm.getTCM(notPlayer));
                    return true;
                }
                player = (Player) sender;
            }
            EnumAbilities ea;
            try {
                ea = EnumAbilities.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException exception) {
                player.sendMessage(dm.getTCM(noAbility, args[0]));
                return true;
            }
            String abilityName = ea.name().toLowerCase();
            if (!sender.hasPermission(dm.getTCM(perm, abilityName))) {
                sender.sendMessage(dm.getTCM(noPerm, dm.getTCM(perm, abilityName)));
                return true;
            }
            Abilities ak = ea.get(getPlugin(), player);
            for (int i = 0; i < player.getInventory().getContents().length; i++) {
                if (player.getInventory().getItem(i) != null &&
                        player.getInventory().getItem(i).getType() != Material.AIR
                        && new NBTItem(player.getInventory().getItem(i)).hasKey(Abilities.NBTIAbility)) {
                    player.getInventory().setItem(i, null);
                }
            }
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(dm.getTCM(noSpace));
                return true;
            }
            assert ak != null;
            player.getInventory().addItem(ak.getItemStack());
            PlayerData pd = getPlugin().getPDManager().getOrNewData(player.getUniqueId());
            pd.setAbility(ak);
            player.sendMessage(tcm(dm.getString(success), abilityName));
        }

        return true;
    }


    public static final String notFound = "PlayerNotFound", notPlayer = "NotPlayer", perm = "Permission", noAbility = "AbilityNotFound",
            noPerm = "NoPerm", noSpace = "NoInventorySpace", success = "Success";
    @Override
    public void setDefaults() {
        dm.isOrDefault("&cPlayer {0} was not found.", notFound);
        dm.isOrDefault("&cThis command is only allowed to be used by players", notPlayer);
        dm.isOrDefault("ability.{0}", perm);
        dm.isOrDefault("&cThere isn't an ability called {0}", noAbility);
        dm.isOrDefault("&cSorry, you don't have permission to use that kit. You need {0} permission to claim that kit", noPerm);
        dm.isOrDefault("&cPlease free Up some space in your inventory to claim this ability.", noSpace);
        dm.isOrDefault("&aKit {0} claimed", success);
    }
}
