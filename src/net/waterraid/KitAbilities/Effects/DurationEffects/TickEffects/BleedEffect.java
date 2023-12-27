package net.waterraid.KitAbilities.Effects.DurationEffects.TickEffects;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.waterraid.KitAbilities.Effects.Effect;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class BleedEffect extends TickEffect {

    public BleedEffect(Main instance, Player target, double duration) {
        super(instance, target, duration);
    }
    public BleedEffect(Main instance, Player target, PlayerData targetData, double duration) {
        super(instance, target, targetData, duration);
    }

    @Override
    protected void setUpEffect() {
        _target.sendMessage(ChatColor.DARK_RED + "You are bleeding");
//        new BukkitRunnable() {
//            int i = 0;
//            @Override
//            public void run() {
//                if (getFrom() == null){
//                    getTarget().damage(getStrength());
//                }else {
//                    getTarget().damage(getStrength(), getFrom());
//                }
//                getTarget().setNoDamageTicks(0);
//                Location loc = getTarget().getLocation().add(0,1,0);
//
//                loc.getWorld().playSound(loc, Sound.STEP_STONE, 1, 1);
////                    playEffect( Location location, Effect effect, int id, int data, float offsetX, float offsetY, float offsetZ, float speed, int particleCount, int radius )
////                    loc.getWorld().spigot().playEffect(loc, Effect.TILE_BREAK, 152, 1, 0.5f, 0.5f, 0.5f , 0.5f, 152, 20);
//                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.BLOCK_CRACK, false, (float) (loc.getX()), (float) (loc.getY()), (float)loc.getZ(), 0.5f, 0.5f, 0.5f, 15, 20, 152);
//                for (Player online: Bukkit.getOnlinePlayers()) {
////                    if(online.getUniqueId().equals(target.getUniqueId())) continue;
//                    ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
//                }
//                i++;
//                if (isExpired() || getTarget().isDead()) {
//                    this.cancel();
//                }
//            }
//        }.runTaskTimer(getPlugin(), 0, getInterval()));
    }



    @Override
    public void removeEffect() {
//        for (BukkitTask bt: taskList){
//            bt.cancel();
//        }
    }
}
