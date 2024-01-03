package net.waterraid.KitAbilities.Abilities;

import net.waterraid.KitAbilities.Abilities.Templates.RightClickAbilities;
import net.waterraid.KitAbilities.Effects.DurationEffects.ReflectorEffect;
import net.waterraid.KitAbilities.Main;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import static net.waterraid.KitAbilities.Main.getPlugin;

public class Reflector extends RightClickAbilities {
    {
        setUpAbilityAndItem(18, 6, Material.MAGMA_CREAM);
    }
    public Reflector(Main instance, Player player) {
        super(instance, player);
    }
    @Override
    protected void doAbility(LivingEntity entity) {
        getPlayer().playSound(getPlayer().getLocation(), Sound.WITHER_IDLE, 10f, 1f);
        ReflectorEffect re = new ReflectorEffect(getPlugin(), getPlayer(), getCastTime());
        re.setFrom(getPlayer());
        re.applyEffect();
    }
}
