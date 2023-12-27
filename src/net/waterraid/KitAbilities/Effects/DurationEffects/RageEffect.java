package net.waterraid.KitAbilities.Effects.DurationEffects;

import net.waterraid.KitAbilities.Effects.DurationEffect;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RageEffect extends DurationEffect {
    private int stacks = 0;

    public RageEffect(Main instance, Player target, double duration) {
        super(instance, target, duration);
    }
    public RageEffect(Main instance, Player target, PlayerData targetData, double duration) {
        super(instance, target, targetData, duration);
    }

    @Override
    protected void setUpEffect() {
        _target.sendMessage(ChatColor.RED + "Have They Not Done You Wrong All These Years?" + ChatColor.BOLD + "THEY HAVE!");
        registerPotionEffects(new PotionEffect(PotionEffectType.FAST_DIGGING, secondsToTicks(getDuration()), 1));
    }



    @Override
    public void removeEffect() {

    }
    @Override
    public void onEvent(EntityDamageByEntityEvent event){
        if (event.getDamager().getUniqueId().equals(_target.getUniqueId()) && isTargeteableLivingEntity(event.getEntity())){
            stacks++;
            if (stacks >= 3) {
                event.getEntity().getWorld().strikeLightningEffect(event.getEntity().getLocation());
                trueDamage(_target, (LivingEntity)event.getEntity(), 2);
                if (event.getEntity().isDead()){
                    event.setCancelled(true);
                }
                if (event.getEntity() instanceof Player && !event.getEntity().isDead()){
                    Main.getPlugin().getPDManager().getOrNewData(event.getEntity().getUniqueId()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 0, false, false), (Player)event.getEntity());
                }
                stacks = 0;
            }

        }

    }

}
