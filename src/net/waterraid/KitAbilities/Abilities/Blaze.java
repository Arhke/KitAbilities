package net.waterraid.KitAbilities.Abilities;

import net.waterraid.KitAbilities.Abilities.Templates.RightClickAbilities;
import net.waterraid.KitAbilities.Effects.DurationEffects.BurnEffect;
import net.waterraid.KitAbilities.Main;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import static net.waterraid.KitAbilities.Abilities.Clone.isTargeteableLivingEntity;
import static net.waterraid.KitAbilities.Main.getPlugin;

public class Blaze extends RightClickAbilities {
    {
        setUpAbilityAndItem(5, 5, Material.BLAZE_POWDER);
    }
    public Blaze(Player player) {
        super(player);
    }

    //=======================
    int _radius = 5;

    @Override
    protected void doAbility(LivingEntity entity) {
        for(Entity e:getPlayer().getNearbyEntities(getRadius(), 1, getRadius())) {
            if (isTargeteableLivingEntity(e)){
                BurnEffect effect = new BurnEffect(getPlugin(),(Player)e, 5);
                effect.setFrom(getPlayer());
                effect.applyEffect();
            }
        }
    }
    public int getRadius(){
        return _radius;
    }
}
