package net.waterraid.KitAbilities.Abilities.Templates;

import net.waterraid.KitAbilities.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class RightClickAbilities extends Abilities {
    public RightClickAbilities(Player player) {
        super(player);
    }

    @Override
    public void onEvent(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getPlayer().getItemInHand() == null || event.getPlayer().getItemInHand().getType() == Material.AIR || !isThisAbilityKit(event.getPlayer().getItemInHand())){
                return;
            }
            castAbility(null);
            event.setCancelled(true);

        }
    }


}
