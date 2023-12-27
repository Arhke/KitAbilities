package net.waterraid.KitAbilities.Abilities;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.waterraid.KitAbilities.Abilities.Templates.OnHitAbilities;
import net.waterraid.KitAbilities.Effects.DurationEffects.NoFallEffect;
import net.waterraid.KitAbilities.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Cliff extends OnHitAbilities {
    {
        setUpAbilityAndItem(8, 0, Material.BONE);
    }
    public Cliff(Main instance, Player player) {
        super(instance, player);
    }


//===========================


    @Override
    protected void doAbility(LivingEntity entity) {
        if (isTargeteableLivingEntity(entity)) {
            Player player = (Player) entity;
            NoFallEffect effect = new NoFallEffect(getPlugin(), player, 8);
            effect.setFrom(getPlayer());
            effect.applyEffect();
            Location loc = player.getLocation();
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.SLIME, false, (float) (loc.getX()), (float) (loc.getY()), (float) loc.getZ(), 0.5f, 0.0f, 0.5f, 0, 200)   ;
            Vector v = entity.getVelocity();
            new BukkitRunnable(){
                @Override
                public void run(){
                    entity.setVelocity(new Vector(v.getX(), 1.2, v.getZ()));
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                    }
                }
            }.runTaskLater(getPlugin(), 0);

        }

    }
}
