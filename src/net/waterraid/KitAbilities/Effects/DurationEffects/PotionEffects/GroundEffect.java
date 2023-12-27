package net.waterraid.KitAbilities.Effects.DurationEffects.PotionEffects;

import net.waterraid.KitAbilities.Effects.DurationEffect;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import net.waterraid.KitAbilities.Utils.AbilityCastEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GroundEffect extends DurationEffect {

    public GroundEffect(Main instance, Player target, double duration) {
        super(instance, target, duration);
    }

    public GroundEffect(Main instance, Player target, PlayerData targetData, double duration) {
        super(instance, target, targetData, duration);
    }

    @Override
    protected void setUpEffect() {
        _target.sendMessage("You have been grounded");
        registerPotionEffects(new PotionEffect(PotionEffectType.JUMP, secondsToTicks(getDuration()), 250, false, false));
    }


    @Override
    public void removeEffect() {
        _target.removePotionEffect(PotionEffectType.JUMP);
    }

    @Override
    public void onEvent(AbilityCastEvent event){
        if (event.getPlayer().getUniqueId().equals(_target.getUniqueId()) && !isExpired()){
            if (event.getAbility().moves()){
                _target.sendMessage(ChatColor.DARK_GREEN + "Sorry, You may not Use that while Grounded");
            }
        }
    }
}
