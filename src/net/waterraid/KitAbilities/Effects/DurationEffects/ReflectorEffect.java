package net.waterraid.KitAbilities.Effects.DurationEffects;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.waterraid.KitAbilities.Effects.DurationEffect;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ReflectorEffect extends DurationEffect {

    public ReflectorEffect(Main instance, Player target, double duration) {
        super(instance, target, duration);
    }

    public ReflectorEffect(Main instance, Player target, PlayerData targetData, double duration) {
        super(instance, target, targetData, duration);
    }

    @Override
    protected void setUpEffect() {
//        Bukkit.getPluginManager().callEvent(new EntityRegainHealthEvent(getTarget(), getTarget().getMaxHealth() - getTarget().getHealth(), EntityRegainHealthEvent.RegainReason.MAGIC));
//        getTarget().setHealth(getTarget().getMaxHealth());
        _target.sendMessage(ChatColor.RED + "I'll Pay Him Back Double Fold!!!");

    }

    @Override
    public void removeEffect() {

    }

    public void onEvent(EntityDamageByEntityEvent event) {
        if (event.getEntity().getUniqueId().equals(getTarget().getUniqueId())) {
            if (((Player) event.getEntity()).isBlocking() && event.getDamager() instanceof LivingEntity) {
                ((LivingEntity) event.getDamager()).damage(event.getDamage() * 2, event.getEntity());
                event.getDamager().setLastDamageCause(new EntityDamageEvent(event.getEntity(), EntityDamageEvent.DamageCause.ENTITY_ATTACK, event.getDamage() * 2));
                event.getDamager().sendMessage(ChatColor.RED + "Ouch, Your damage is being reflected. Hmm.");
                new BukkitRunnable() {
                    final Vector v = event.getEntity().getVelocity();
                    final Player e = (Player)event.getEntity();
                    @Override
                    public void run() {if(!e.isDead() && e.isOnline()) e.setVelocity(v.setY(e.getVelocity().getY()));}
                }.runTaskLater(Main.getPlugin(), 0L);
                Location entLoc = event.getEntity().getLocation();
                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.BARRIER, false, (float) (entLoc.getX()), (float) (entLoc.getY()), (float) entLoc.getZ(), 0.5f, 0.0f, 0.5f, 0, 1);
                ((CraftPlayer) event.getDamager()).getHandle().playerConnection.sendPacket(packet);
                ((CraftPlayer) event.getEntity()).getHandle().playerConnection.sendPacket(packet);
                event.setDamage(event.getDamage()/2);
            }

        }
    }

}
