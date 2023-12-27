package net.waterraid.KitAbilities.Commands.AbilityCommands;

import com.Arhke.ArhkeLib.Lib.Base.SubCommandsBase;
import com.Arhke.ArhkeLib.Lib.FileIO.DataManager;
import com.Arhke.ArhkeLib.Lib.Utils.HelpMessage;
import net.waterraid.KitAbilities.ConfigFiles;
import net.waterraid.KitAbilities.Main;
import org.bukkit.entity.Player;

public class ReloadCommand extends SubCommandsBase<Main> {
    public ReloadCommand(Main instance, DataManager dm) {
        super(instance, "reload", dm);
    }

    @Override
    public CommandType getType() {
        return CommandType.PLAYER;
    }

    @Override
    public HelpMessage[] getHelpMessage() {
        return new HelpMessage[0];
    }

    @Override
    public boolean run(String[] strings, Player player) {
        if(!player.hasPermission(dm.getString(Perm))) {
            player.sendMessage(dm.getTCM(NoPerm));
            return true;
        }
        try {
            getPlugin().getConfig(ConfigFiles.AbilityConfig).reloadConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.sendMessage(dm.getTCM(ReloadSuccess));
        return true;
    }
    public static final String Perm = "PermissionNode", NoPerm = "NoPermission", ReloadSuccess = "Success";
    @Override
    public void setDefaults() {

        dm.isOrDefault("abilityadmin.reload", Perm);
        dm.isOrDefault("&cYou do not have permission to do that.", NoPerm);
        dm.isOrDefault("&aAbilityConfig Reloaded.", ReloadSuccess);
    }
}
