package net.waterraid.KitAbilities.Effects.InstantEffects;

import net.waterraid.KitAbilities.Effects.Effect;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Managers.PlayerData;
import org.bukkit.entity.Player;

public class FoodHealthRestore extends Effect {
    public FoodHealthRestore(Main instance, Player target) {
        super(instance, target);
    }
    public FoodHealthRestore(Main instance, Player target, PlayerData targetData) {
        super(instance, target, targetData);
    }
    @Override
    protected void setUpEffect() {
        getTarget().setHealth(getTarget().getMaxHealth());
        getTarget().setFoodLevel(20);
    }
}
