package net.waterraid.KitAbilities.Abilities;

import net.waterraid.KitAbilities.Abilities.Templates.Abilities;
import net.waterraid.KitAbilities.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class Dodge extends Abilities {
    {
        setUpAbilityAndItem(4, 0, Material.CARROT_STICK);
        _moves = true;
    }
    public Dodge(Main instance, Player player)  {
        super(instance, player);
    }
    @Override
    public void onEvent(PlayerInteractEvent event){
        if (event.getPlayer().getItemInHand() == null || event.getPlayer().getItemInHand().getType() == Material.AIR || !isThisAbilityKit(event.getPlayer().getItemInHand())){
            return;
        }

        if(event.getPlayer().isSneaking()){
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR){
                isShifting = false;
                getPlayer().sendMessage(ChatColor.GREEN + "Switched to " + ChatColor.WHITE + ChatColor.BOLD + "LEFT AND RIGHT");
            }else if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR){
                isShifting = true;
                getPlayer().sendMessage(ChatColor.BLUE+"Switched to "+ ChatColor.WHITE+ChatColor.BOLD+"FOWARDS AND BACKWARDS");
            }
            event.setCancelled(true);
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {

            isLeft = false;
            castAbility(null);
            event.setCancelled(true);

        }else if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR){
            isLeft = true;
            castAbility(null);
            event.setCancelled(true);
        }

    }








    boolean isLeft  = false;
    boolean isShifting = false;
    @Override
    protected void doAbility(LivingEntity entity) {
        double yaw = getPlayer().getLocation().getYaw()/180f*Math.PI;
        double cos, sin;
        if(isShifting){
            cos = Math.sin(yaw) * (isLeft ? -1 : 1);
            sin = Math.cos(yaw) * (isLeft ? 1 : -1);
        }else {
            cos = Math.cos(yaw) * (isLeft ? 1 : -1);
            sin = Math.sin(yaw) * (isLeft ? 1 : -1);
        }
        if (getPlayer().isOnGround()){
            if(getPlayer().getLocation().add(0,2,0).getBlock().getType().isSolid()){
                getPlayer().setVelocity(getPlayer().getVelocity().setY(0.1).add(new Vector(3.2 * cos, 0.0, 3.2 * sin)));
            } else if(getPlayer().isSprinting()) {
                getPlayer().teleport(getPlayer().getLocation().add(0, 0.5, 0));
                getPlayer().setVelocity(getPlayer().getVelocity().setY(0.1).add(new Vector(1.3 * cos, 0.0, 1.3 * sin)));
            } else {
                getPlayer().teleport(getPlayer().getLocation().add(0, 0.5, 0));
                getPlayer().setVelocity(new Vector(1.4 * cos, 0.0, 1.4 * sin));
            }
        }else{
            getPlayer().setVelocity(getPlayer().getVelocity().add(new Vector(0.9*cos,0.0,0.9*sin)).setY(0));
        }


    }
}
