package net.waterraid.KitAbilities.Commands;

import net.waterraid.KitAbilities.Effects.Effect;
import net.waterraid.KitAbilities.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

import static net.waterraid.KitAbilities.Main.getPlugin;

public class EffectsCommand extends CommandsBase implements CommandExecutor {
    public EffectsCommand(Main instance){
        super(instance);
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.isOp()){
            return true;
        }
        try {
            Effect effects = (Effect)Class.forName("net.waterraid.KitAbilities.Effects."+args[0]).getDeclaredConstructor(Main.class, Player.class).newInstance(getPlugin(), Bukkit.getPlayer(args[1]));
            effects.applyEffect();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
}
