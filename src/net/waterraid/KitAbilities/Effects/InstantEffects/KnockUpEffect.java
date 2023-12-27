package net.waterraid.KitAbilities.Effects.InstantEffects;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.waterraid.KitAbilities.Effects.Effect;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class KnockUpEffect extends Effect {

    public KnockUpEffect(Main instance, Player target) {
        super(instance, target);
    }
    public KnockUpEffect(Main instance, Player target, PlayerData targetData) {
        super(instance, target, targetData);
    }

    @Override
    protected void setUpEffect() {

        getTarget().setVelocity(new Vector(getTarget().getVelocity().getX(), getStrength(), getTarget().getVelocity().getZ()));
        Location loc = getTarget().getLocation();
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.SLIME, false, (float) (loc.getX()), (float) (loc.getY()), (float) loc.getZ(), 0.5f, 0.0f, 0.5f, 0, 200);
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.getUniqueId().equals(getTarget().getUniqueId()))
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
