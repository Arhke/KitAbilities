package net.waterraid.KitAbilities.Abilities;

import net.waterraid.KitAbilities.Abilities.Templates.RightClickAbilities;
import net.waterraid.KitAbilities.Effects.DurationEffects.NoTargetEffect;
import net.waterraid.KitAbilities.Main;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import static net.waterraid.KitAbilities.Main.getPlugin;

public class Untarget extends RightClickAbilities {
    {
        setUpAbilityAndItem(15, 2, Material.FERMENTED_SPIDER_EYE);
    }
    public Untarget(Player player) {
        super(player);
    }

    @Override
    protected void doAbility(LivingEntity entity) {
        NoTargetEffect nte = new NoTargetEffect(getPlugin(), getPlayer(), getCastTime());
        nte.setFrom(getPlayer());
        nte.applyEffect();
    }
}
