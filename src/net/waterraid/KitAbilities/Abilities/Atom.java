package net.waterraid.KitAbilities.Abilities;

import net.waterraid.KitAbilities.Abilities.Templates.RightClickAbilities;
import net.waterraid.KitAbilities.Effects.InstantEffects.KnockBackEffect;
import net.waterraid.KitAbilities.Main;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import static com.Arhke.ArhkeLib.Lib.Base.Base.trueDamage;
import static net.waterraid.KitAbilities.Abilities.Clone.isTargeteableLivingEntity;
import static net.waterraid.KitAbilities.Main.getPlugin;

public class Atom extends RightClickAbilities {
    {
       setUpAbilityAndItem(5, 0, Material.RECORD_12);
    }
    int _radius = 5;

    public Atom(Player caster) {
        super(caster);
    }

    //==============================

    @Override
    protected void doAbility(LivingEntity entity) {
        for(Entity e:getPlayer().getNearbyEntities(getRadius(), 1, getRadius())) {
            if (isTargeteableLivingEntity(e)){
                KnockBackEffect kbe = new KnockBackEffect(getPlugin(), (Player)e);
                kbe.setStrength(2.5);
                kbe.setFrom(getPlayer());
                kbe.applyEffect();
                trueDamage(getPlayer(),(LivingEntity)e, 2d);
            }else if(e instanceof Projectile){
                e.setVelocity(e.getVelocity().multiply(-1));
            }
        }
    }
    public int getRadius(){
        return _radius;
    }
}
