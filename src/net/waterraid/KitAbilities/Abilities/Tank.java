package net.waterraid.KitAbilities.Abilities;

import de.tr7zw.nbtapi.NBTItem;
import net.waterraid.KitAbilities.Abilities.Templates.RightClickAbilities;
import net.waterraid.KitAbilities.Effects.DurationEffects.RageEffect;
import net.waterraid.KitAbilities.Main;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;

public class Tank extends RightClickAbilities {
    {
        setUpAbilityAndItem(6, 1, new ItemStack(Material.INK_SACK, 1, (short) 0, (byte) 11));

    }
    public Tank(Main instance, Player player) {
        super(instance, player);
    }

    @Override
    protected void doAbility(LivingEntity entity) {
        getPlugin().getPDManager().getOrNewData(getPlayer().getUniqueId()).addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, secondsToTicks(getCastTime()), 1, false, false), getPlayer());
    }
}