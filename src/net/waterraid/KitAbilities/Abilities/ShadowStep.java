package net.waterraid.KitAbilities.Abilities;

import net.waterraid.KitAbilities.Abilities.Templates.RightClickAbilities;
import net.waterraid.KitAbilities.Effects.DurationEffects.ShadowStepEffect;
import net.waterraid.KitAbilities.Main;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ShadowStep extends RightClickAbilities{
    {
        setUpAbilityAndItem(8, 6, Material.COAL);
    }
    public ShadowStep(Main instance, Player player) {
        super(instance, player);
    }
    @Override
    protected void doAbility(LivingEntity entity) {
        getPlayer().playSound(getPlayer().getLocation(), Sound.AMBIENCE_CAVE, 10f, 1f);
        Vector v = getPlayer().getVelocity().clone();
        v.add(getPlayer().getEyeLocation().getDirection().multiply(5));
        Arrow arrow = getPlayer().launchProjectile(Arrow.class, v);
        arrow.setKnockbackStrength(2);
        ShadowStepEffect effect = new ShadowStepEffect(getPlugin(), getPlayer(), getCastTime(), arrow);
        effect.setFrom(getPlayer());
        effect.applyEffect();

    }
}
