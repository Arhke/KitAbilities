package net.waterraid.KitAbilities.Utils;

import org.bukkit.ChatColor;

public enum Debuff {
    ANTIHEAL("AHeal", ChatColor.RED), FROZEN("Froz", ChatColor.AQUA), GROUND("Grnd", ChatColor.DARK_BLUE), NOFALL("NoFall", ChatColor.WHITE),
    PACIFY("Pcf", ChatColor.YELLOW), ROOT("Rt", ChatColor.DARK_GRAY), SILENCE("Silc", ChatColor.GRAY), STUN("Stun", ChatColor.DARK_PURPLE);
    String _abbrev;
    ChatColor _color;
    Debuff(String abbrev, ChatColor color){
        _abbrev = abbrev;
        _color = color;
    }

}