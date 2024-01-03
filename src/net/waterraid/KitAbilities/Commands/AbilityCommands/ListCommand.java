package net.waterraid.KitAbilities.Commands.AbilityCommands;

import com.Arhke.ArhkeLib.Lib.Base.CommandsBase;
import com.Arhke.ArhkeLib.Lib.Base.SubCommandsBase;
import net.waterraid.KitAbilities.Abilities.Templates.EnumAbilities;
import org.bukkit.entity.Player;

import static com.Arhke.ArhkeLib.Lib.Base.Base.tcm;

public class ListCommand extends SubCommandsBase {

    public ListCommand(CommandsBase cb, String commandName) {
        super(cb, commandName);
    }

    @Override
    public CommandType getType() {
        return CommandType.PLAYER;
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
