package net.waterraid.KitAbilities.Effects.DurationEffects;

import net.waterraid.KitAbilities.Effects.DurationEffect;
import net.waterraid.KitAbilities.Effects.DurationEffects.TickEffects.BleedEffect;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import net.waterraid.KitAbilities.Utils.ArrowHitPlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static com.Arhke.ArhkeLib.Base.Base.tcm;
import static com.Arhke.ArhkeLib.Base.Base.trueDamage;
import static net.waterraid.KitAbilities.Abilities.Clone.isTargeteableLivingEntity;
import static net.waterraid.KitAbilities.Abilities.Clone.secondsToTicks;
import static net.waterraid.KitAbilities.Main.getPlugin;

public class ShadowStepEffect extends DurationEffect {
    PlayerData _markedPlayerData;
    Player _markedPlayer;
    Arrow _arrow;
    int stacks = 0;
    public ShadowStepEffect(Main instance, Player target, double duration, Arrow arrow) {
        super(instance, target, duration);
        _arrow = arrow;
    }

    @Override
    protected void setUpEffect() {
        registerPotionEffects(new PotionEffect(PotionEffectType.NIGHT_VISION, secondsToTicks(getDuration()), 0));
        _target.sendMessage(ChatColor.DARK_GRAY + "~~Phantom Arrow~~.");
    }

    @Override
    public void removeEffect() {
        subEffectList.forEach(DurationEffect::removeEffect);
    }

    @Override
    public void onEvent(EntityDamageByEntityEvent event){
        if (event.getDamager().getUniqueId().equals(_target.getUniqueId()) && event.getEntity() instanceof LivingEntity){
            if(_markedPlayer != null) {
                stacks++;
                if (stacks == 4) {
                    event.setCancelled(true);
                    Bukkit.broadcastMessage(tcm("&c&l"+ _target.getDisplayName() + " &f&lPRIMARY LOTUS!"));
                    _markedPlayer.setVelocity(new Vector(0,-1,0));
                    _target.setVelocity(new Vector(0,-1,0));
                    BleedEffect be = new BleedEffect(getPlugin(), _markedPlayer, _markedPlayerData, 4);
                    be.setFrom(_target);
                    be.setInterval(0.5);
                    be.setStrength(5);
                    be.applyEffect();
                }else{
                    event.setCancelled(true);
                    trueDamage(_target, _markedPlayer, 2);
                    teleportBehind(_target, _markedPlayer);
                }
            }
        }
    }
    @Override
    public void onEvent(PlayerDeathEvent event){
        if (event.getEntity().equals(_markedPlayer) && _target.equals(event.getEntity().getKiller())) {
            Bukkit.broadcastMessage(tcm("&7{0} &6&lWas Dragged into the &0&lShadows &6&lby &r&7{1}", _markedPlayer.getName(), _target.getName()));
        }
    }

    @Override
    public void onEvent(ArrowHitPlayerEvent event){
        if (event.getShooter().getUniqueId().equals(getTarget().getUniqueId()) &&
                event.getArrow().getUniqueId().equals(_arrow.getUniqueId()) && isTargeteableLivingEntity(event.getPlayer())){
            event.setCancelled(true);
            event.getArrow().remove();

//            if (event.getPlayer().isOnGround()){
//                _target.sendMessage(ChatColor.RED + "The Phantom Arrow Fades");
//                return;
//            }

            if (event.getPlayer().getHealth() > 6){
                _target.sendMessage(ChatColor.RED + "The Shadows Lost Its Grip On Your Targets Soul.");
                return;
            }
            _target.sendMessage(ChatColor.RED + "Steel Yourself, Here goes.");

            _markedPlayerData = getPlugin().getPDManager().getOrNewData(event.getPlayer().getUniqueId());
            _markedPlayerData.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, secondsToTicks(getRemainingTime()), 5),event.getPlayer());
            _markedPlayer = event.getPlayer();
            _markedPlayer.sendMessage(ChatColor.RED + "Steel Thyself, Here goes.");
            teleportBehind(event.getShooter(), event.getPlayer());
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().setVelocity(new Vector(0, 0, 0));
                    event.getShooter().setVelocity(new Vector(0, 0, 0));
                }
            }.runTaskLater(getPlugin(), 1L);

        }
    }
    private static void teleportBehind(Player behindPlayer, Player player){
        Location loc = player.getLocation().clone();
        Vector v = player.getEyeLocation().getDirection().setY(0);
        loc.setDirection(v);
        v = v.normalize();
        loc.add(v.multiply(-1));
        behindPlayer.teleport(loc);
    }
}
