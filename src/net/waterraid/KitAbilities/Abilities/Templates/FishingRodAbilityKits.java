package net.waterraid.KitAbilities.Abilities.Templates;

import net.waterraid.KitAbilities.Main;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerFishEvent;

public abstract class FishingRodAbilityKits extends Abilities{

    public FishingRodAbilityKits(Main instance, Player player) {
        super(instance, player);
    }
    @Override
    public void onEvent(PlayerFishEvent event) {
//        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
//            Player player = (Player)event.getDamager();
//            if (!player.equals(getPlayer()) || player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR || !isThisAbilityKit(player.getItemInHand())) {
//                return;
//            }
//            castAbility((LivingEntity)event.getEntity());
//        }
    }
}
