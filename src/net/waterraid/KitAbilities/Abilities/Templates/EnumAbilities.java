package net.waterraid.KitAbilities.Abilities.Templates;

import net.waterraid.KitAbilities.Abilities.*;
import net.waterraid.KitAbilities.Main;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public enum EnumAbilities {
    ATOM(Atom::new), BLAZE(Blaze::new), CLIFF(Cliff::new), DODGE(Dodge::new), UNTARGET(Untarget::new),
    RAGE(Rage::new), REFLECTOR(Reflector::new), SHADOWSTEP(ShadowStep::new), SMASH(Smash::new), PHASE(Phase::new),
    TANK(Tank::new), HOOKER(Hooker::new), CLONE(Clone::new);
    final Function<Player, Abilities> kitClass;

    EnumAbilities(Function<Player, Abilities> KitClass) {
        kitClass = KitClass;
    }

    public Abilities get( Player player) {
        return kitClass.apply(player);
    }
    public Function<Player, Abilities> getKitClass(){
        return this.kitClass;
    }
}
