package net.waterraid.KitAbilities.Abilities;

import net.waterraid.KitAbilities.Abilities.Templates.OnHitAbilities;
import net.waterraid.KitAbilities.Effects.DurationEffects.RootEffect;
import net.waterraid.KitAbilities.Effects.Effect;
import net.waterraid.KitAbilities.Main;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Smash extends OnHitAbilities {
    {
        setUpAbilityAndItem(10, 0, Material.RABBIT_HIDE);
    }
    public Smash(Main instance, Player player) {
        super(instance, player);
    }

    @Override
    protected void doAbility(LivingEntity entity) {
        if (isTargeteableLivingEntity(entity) && entity instanceof Player){
            trueDamage(getPlayer(), entity, entity.getHealth()*0.35);
            Player player = (Player)entity;
            double x =getPlayer().getEyeLocation().getDirection().getX();
            double y =getPlayer().getEyeLocation().getDirection().getY();
            double z =getPlayer().getEyeLocation().getDirection().getZ();
            if (y < 0 && Math.abs(x)<Math.abs(y) && Math.abs(z) < Math.abs(y)){
                Effect effects = new RootEffect(getPlugin(), player, 1.5);
                effects.applyEffect();
                getPlayer().sendMessage(tcm("&6&lLeg Breaker!"));
            }else{
                entity.teleport(entity.getLocation().add(0,.3,0));
                Vector v = getPlayer().getEyeLocation().getDirection().normalize().multiply(5);
                entity.setVelocity(v);
            }
        }

    }
}
