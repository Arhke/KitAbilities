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
import org.bukkit.util.Vector;

public class KnockBackEffect extends Effect {


    public KnockBackEffect(Main instance, Player target) {
        super(instance, target);
    }
    public KnockBackEffect(Main instance, Player target, PlayerData targetData) {
        super(instance, target, targetData);
    }

    @Override
    protected void setUpEffect() {
        if (getFrom() == null){
            return;
        }
        Location entLoc = getTarget().getLocation();
        Location playerLoc = getFrom().getLocation();
        double x = entLoc.getX()-playerLoc.getX(), y = entLoc.getY()-playerLoc.getY(), z = entLoc.getZ()-playerLoc.getZ();
        getTarget().teleport(getTarget().getLocation().add(0,.3,0));
        Vector v = new Vector(x, y, z);
        v.normalize();
        v.multiply(getStrength());
        v.setY(0.7);
//        x = x == 0?1:x;
//        y = y == 0?1:y;
//        z = z == 0?1:z;
        getTarget().setVelocity(v);
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.SPELL, false, (float) (entLoc.getX()), (float) (entLoc.getY()), (float)entLoc.getZ(), 0.5f, 0.0f, 0.5f, 0, 200);
        for (Player online: Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
        }

    }


}
