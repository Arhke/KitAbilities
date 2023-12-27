package net.waterraid.KitAbilities.Effects.DurationEffects;

import net.waterraid.KitAbilities.Effects.DurationEffect;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import net.waterraid.KitAbilities.Utils.AbilityCastEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class StunEffect extends DurationEffect {


    public StunEffect(Main instance, Player target, double duration) {
        super(instance, target, duration);
    }
    public StunEffect(Main instance, Player target, PlayerData targetData, double duration) {
        super(instance, target, targetData, duration);
    }

    @Override
    protected void setUpEffect() {
        _target.sendMessage("You Have Been Stunned");registerAsSubEffect(new RootEffect(getPlugin(), getTarget(), getDuration()));
    }

    @Override
    public void removeEffect() {
        subEffectList.forEach(DurationEffect::removeEffect);
    }

    @Override
    public void onEvent(PlayerInteractEvent event) {
        if (event.getPlayer().getUniqueId().equals(_target.getUniqueId()) && !isExpired()){
            event.getPlayer().sendMessage(ChatColor.DARK_PURPLE + "You are stunned.");
            event.setCancelled(true);
        }
    }
    @Override
    public void onEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager().getUniqueId().equals(_target.getUniqueId()) && !isExpired()){
            event.getDamager().sendMessage(ChatColor.DARK_PURPLE + "You are stunned.");
            event.setCancelled(true);
        }
    }
    @Override
    public void onEvent(AbilityCastEvent event) {
        if(event.getPlayer().getUniqueId().equals(_target.getUniqueId()) && !isExpired()){
            event.getPlayer().sendMessage(ChatColor.DARK_PURPLE + "You are stunned.");
            event.setCancelled(true);
        }
    }
}
