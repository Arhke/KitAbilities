package net.waterraid.KitAbilities.Commands.AbilityCommands;

import com.Arhke.ArhkeLib.Lib.Base.SubCommandsBase;
import com.Arhke.ArhkeLib.Lib.FileIO.DataManager;
import com.Arhke.ArhkeLib.Lib.Utils.HelpMessage;
import net.waterraid.KitAbilities.Abilities.Templates.EnumAbilities;
import net.waterraid.KitAbilities.Main;
import org.bukkit.entity.Player;

public class ListCommand extends SubCommandsBase<Main> {
    public ListCommand(Main instance, DataManager dm) {
        super(instance, "list", dm);
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
        if (player.hasPermission("ability.list")) {
            player.sendMessage(tcm("&6=====<Valid Abilities>====="));
            for (EnumAbilities ea : EnumAbilities.values()) {
                player.sendMessage(tcm("&6{0}", ea.name()));
            }
        }
        return true;
    }

    @Override
    public void setDefaults() {

    }
}
