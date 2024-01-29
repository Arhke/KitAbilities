package net.waterraid.KitAbilities.Commands;

import com.Arhke.ArhkeLib.Lib.Base.CommandsBase;
import com.Arhke.ArhkeLib.Lib.FileIO.ConfigManager;
import net.waterraid.KitAbilities.Abilities.Templates.Abilities;
import net.waterraid.KitAbilities.Commands.AbilityCommands.SetupGUI;
import net.waterraid.KitAbilities.Managers.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static net.waterraid.KitAbilities.Main.getPlugin;

public class SetupCommand extends CommandsBase {
    public SetupCommand(String command, ConfigManager dm) {
        super(command, dm);
    }

    @Override
    public void setDefaults() {

    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)){
            return false;
        }
        getPlugin().getGUIManager().openGUI((Player)commandSender, new SetupGUI());
        return true;
    }
}
