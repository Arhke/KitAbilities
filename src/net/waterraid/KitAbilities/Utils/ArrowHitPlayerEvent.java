package net.waterraid.KitAbilities.Utils;

import net.waterraid.KitAbilities.Abilities.Templates.Abilities;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArrowHitPlayerEvent extends Event implements Cancellable {
    private boolean _isCancelled = false;

    private final Player _player;
    private final Player _shooter;
    private final Arrow _arrow;
    private final double _damage;
    private final double _finalDamage;
    private static final HandlerList HANDLERS = new HandlerList();
    public ArrowHitPlayerEvent(Player shooter, Player player, Arrow arrow, double damage, double finalDamage, boolean cancelled){
        _player = player;
        _shooter = shooter;
        _arrow = arrow;
        _damage = damage;
        _finalDamage = finalDamage;
        _isCancelled = cancelled;

    }

    @Override
    public boolean isCancelled() {
        return _isCancelled;
    }
    public Player getPlayer() {
        return _player;
    }
    public Player getShooter() {
        return _shooter;
    }
    public Arrow getArrow() {
        return _arrow;
    }
    public double getDamage(){
        return _damage;
    }public double getFinalDamage() {
        return _finalDamage;
    }
    @Override
    public void setCancelled(boolean b) {
        _isCancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
