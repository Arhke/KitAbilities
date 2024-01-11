package net.waterraid.KitAbilities.Abilities;

import net.waterraid.KitAbilities.Abilities.Templates.RightClickAbilities;
import net.waterraid.KitAbilities.Main;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static net.waterraid.KitAbilities.Abilities.Clone.secondsToTicks;
import static net.waterraid.KitAbilities.Main.getPlugin;

public class Tank extends RightClickAbilities {
    {
        setUpAbilityAndItem(6, 1, new ItemStack(Material.INK_SACK, 1, (short) 0, (byte) 11));

    }
    public Tank(Player player) {
        super(player);
    }

    @Override
    protected void doAbility(LivingEntity entity) {
        getPlugin().getPDManager().getOrNewData(getPlayer().getUniqueId()).addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, secondsToTicks(getCastTime()), 1, false, false), getPlayer());
    }
}
