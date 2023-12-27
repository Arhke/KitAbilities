package net.waterraid.KitAbilities.Abilities;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.waterraid.KitAbilities.Abilities.Templates.Abilities;
import net.waterraid.KitAbilities.Effects.DurationEffects.AntiSprintEffect;
import net.waterraid.KitAbilities.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Hooker extends Abilities {
    {
        setUpAbilityAndItem(3, 0, Material.FISHING_ROD);
        ItemMeta im = _is.getItemMeta();
        im.spigot().setUnbreakable(true);
        _is.setItemMeta(im);
    }

    public Hooker(Main instance, Player player) {
        super(instance, player);
    }


    @Override
    protected void doAbility(LivingEntity entity) {

    }

    @Override
    public void onEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof FishHook && event.getEntity() instanceof LivingEntity) {
            Player player = (Player) ((FishHook) event.getDamager()).getShooter();
            if (!player.equals(getPlayer()) || player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR || !isThisAbilityKit(player.getItemInHand())) {
                return;
            }
            castAbility((LivingEntity) event.getEntity());
        }
    }

    @Override
    public void onEvent(ProjectileLaunchEvent event) {
        Player player = (Player) event.getEntity().getShooter();
        if (isOnCoolDown() && isThisAbilityKit(player.getItemInHand())) {
            event.setCancelled(true);
        }
    }


    @Override
    public void onEvent(PlayerFishEvent event) {
        event.setExpToDrop(0);
        if (event.getCaught() == null && event.getState() == PlayerFishEvent.State.IN_GROUND || event.getHook().isOnGround()){
            castAbility(null);
            Location entLoc = event.getHook() .getLocation(), playerLoc = getPlayer().getLocation();
            double x = entLoc.getX()-playerLoc.getX(), y = entLoc.getY()-playerLoc.getY(), z = entLoc.getZ()-playerLoc.getZ();
            Vector v = new Vector(x, y, z);
            v.normalize();
            v.multiply(1.3);
            v.add(new Vector(0,0.7,0));
            new BukkitRunnable() {
                final Vector vector = v;
                @Override
                public void run() {
                    getPlayer().setVelocity(vector);
                }
            }.runTaskLater(getPlugin(), 0);

        }else if (event.getCaught() instanceof Player) {
            Player player = (Player) event.getCaught();
            player.teleport(player.getLocation().add(0,.3,0));
            Location entLoc = player.getLocation(), playerLoc = getPlayer().getLocation();
            double x = entLoc.getX()-playerLoc.getX(), y = entLoc.getY()-playerLoc.getY(), z = entLoc.getZ()-playerLoc.getZ();
            Vector v = new Vector(x, y, z);
            v.normalize();
            v.multiply(-1.3);
            trueDamage(event.getPlayer(), player, 2);
            Location loc = player.getLocation();
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.SMOKE_NORMAL, false, (float) (loc.getX()), (float) (loc.getY()), (float) loc.getZ(), 0.5f, 0.0f, 0.5f, 0, 200);
            new BukkitRunnable() {
                final Vector vector = v;
                @Override
                public void run() {
                    player.setVelocity(vector);
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                    }
                }
            }.runTaskLater(getPlugin(), 0);
        }
    }
}
