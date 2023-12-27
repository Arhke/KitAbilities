package net.waterraid.KitAbilities.Utils;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TrueDamageEvent extends Event implements Cancellable {
    private boolean isCancelled = false;
    private final Player damager;
    private final LivingEntity entity;
    private final double damage;
    private static final HandlerList HANDLERS = new HandlerList();
    public TrueDamageEvent(Player damager, LivingEntity entity, double damage){
        this.damager = damager;
        this.entity = entity;
        this.damage = damage;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }
    public Player getDamager() {
        return this.damager;
    }
    public LivingEntity getEntity() {
        return entity;
    }
    public double getDamage(){
        return damage;
    }
    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }


    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
