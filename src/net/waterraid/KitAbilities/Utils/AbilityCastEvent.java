package net.waterraid.KitAbilities.Utils;

import net.waterraid.KitAbilities.Abilities.Templates.Abilities;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AbilityCastEvent extends Event implements Cancellable {
    private boolean _isCancelled = false;
    private Abilities _ability;
    private Player _player;
    private static final HandlerList HANDLERS = new HandlerList();
    public AbilityCastEvent(Player player, Abilities ability){
        _player = player;
        _ability = ability;
    }

    @Override
    public boolean isCancelled() {
        return _isCancelled;
    }
    public Player getPlayer() {
        return _player;
    }

    @Override
    public void setCancelled(boolean b) {
        _isCancelled = b;
    }
    public Abilities getAbility() {
        return _ability;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
