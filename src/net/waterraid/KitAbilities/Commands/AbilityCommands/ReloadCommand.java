package net.waterraid.KitAbilities.Commands.AbilityCommands;

import com.Arhke.ArhkeLib.Lib.Base.CommandsBase;
import com.Arhke.ArhkeLib.Lib.Base.SubCommandsBase;
import net.waterraid.KitAbilities.ConfigFiles;
import org.bukkit.entity.Player;

import static net.waterraid.KitAbilities.Main.getPlugin;

public class ReloadCommand extends SubCommandsBase {
    public ReloadCommand(CommandsBase cb) {
        super(cb, "reload");
    }

    @Override
    public CommandType getType() {
        return CommandType.PLAYER;
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
