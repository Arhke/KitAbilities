package net.waterraid.KitAbilities.Abilities;

import net.waterraid.KitAbilities.Abilities.Templates.RightClickAbilities;
import net.waterraid.KitAbilities.Effects.DurationEffects.RageEffect;
import net.waterraid.KitAbilities.Main;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Rage extends RightClickAbilities {
    {
        setUpAbilityAndItem(15, 5, Material.FLINT_AND_STEEL);
    }
    public Rage(Main instance, Player player) {
        super(instance, player);
    }

    @Override
    protected void doAbility(LivingEntity entity) {
        RageEffect re = new RageEffect(getPlugin(), getPlayer(), getCastTime());
        re.setFrom(getPlayer());
        re.applyEffect();
    }
}
