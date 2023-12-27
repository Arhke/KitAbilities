package net.waterraid.KitAbilities.Abilities;

import net.waterraid.KitAbilities.Abilities.Templates.Abilities;
import net.waterraid.KitAbilities.Abilities.Templates.RightClickAbilities;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Utils.Direction3D;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;

public class Phase extends Abilities {
    {
        setUpAbilityAndItem(11, 0, Material.EYE_OF_ENDER);
    }
    Location tp;
    public Phase(Main instance, Player player) {
        super(instance, player);
    }

    @Override
    public void onEvent(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getPlayer().getItemInHand() == null || event.getPlayer().getItemInHand().getType() == Material.AIR || !isThisAbilityKit(event.getPlayer().getItemInHand())){
                return;
            }
            event.setCancelled(true);
            if(isInProtectedRegion(getPlayer())){
                getPlayer().sendMessage(ChatColor.RED + "You may not use this here");
                return;
            }
            if(event.getClickedBlock() == null){
                getPlayer().sendMessage(ChatColor.RED + "No block to phase through or phase to");
                return;
            }
            if (event.getClickedBlock().getLocation().distance(getPlayer().getLocation()) > 2){
                getPlayer().sendMessage(ChatColor.RED+ "That Block is too far away please move closer");
                return;
            }
            Vector v = getPlayer().getEyeLocation().getDirection();
            Direction3D dir = Direction3D.getDirectionFromCentroid(v.getX(), v.getY(), v.getZ());
            Location l = event.getClickedBlock().getLocation().add(dir.x, dir.y,dir.z);
            Location clone = l.clone();
            if (clone.getBlock().getType().isSolid()){
                getPlayer().sendMessage(ChatColor.RED + "Wall is Too Thick To Phase");
                return;
            }
            Location up = l.clone().add(0,1,0);
            Location down = l.clone().add(0,-1,0);
            if(!down.getBlock().getType().isSolid()){
                this.tp = down.add(0.5,0,0.5).setDirection(v);
                if(isInProtectedRegion(this.tp)){
                    getPlayer().sendMessage(ChatColor.RED + "You may not phase into a non PVP zone");
                    return;
                }
                castAbility(null);
            }else if (!up.getBlock().getType().isSolid()) {
                this.tp = clone.add(0.5,0,0.5).setDirection(v);
                if(isInProtectedRegion(this.tp)){
                    getPlayer().sendMessage(ChatColor.RED + "You may not phase into a non PVP zone");
                    return;
                }
                castAbility(null);
            }else{
                getPlayer().sendMessage(ChatColor.RED + "Wall is Too Thick To Phase");
            }



        }
    }
    @Override
    protected void doAbility(LivingEntity entity) {

        getPlayer().teleport(this.tp);
        getPlayer().sendMessage(ChatColor.GREEN + "~~WOOSH~~");
    }
}
