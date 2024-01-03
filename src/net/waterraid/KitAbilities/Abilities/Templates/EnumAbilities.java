package net.waterraid.KitAbilities.Abilities.Templates;

import net.waterraid.KitAbilities.Abilities.*;
import net.waterraid.KitAbilities.Main;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public enum EnumAbilities {
    ATOM(Atom.class), BLAZE(Blaze.class), CLIFF(Cliff.class), DODGE(Dodge.class), UNTARGET(Untarget.class),
    RAGE(Rage.class), REFLECTOR(Reflector.class), SHADOWSTEP(ShadowStep.class), SMASH(Smash.class), PHASE(Phase.class),
    TANK(Tank.class), HOOKER(Hooker.class), CLONE(Clone.class);
    final Class<? extends Abilities> _kitClass;

    EnumAbilities(Class<? extends Abilities> KitClass) {
        _kitClass = KitClass;
    }

    public Abilities get(Main instance, Player player) {
        try {
            return _kitClass.getDeclaredConstructor(Main.class, Player.class).newInstance(instance, player);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Class<? extends Abilities> getKitClass(){
        return this._kitClass;
    }
}
