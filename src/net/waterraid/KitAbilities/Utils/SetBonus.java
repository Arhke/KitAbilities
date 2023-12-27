package net.waterraid.KitAbilities.Utils;

import com.Arhke.ArhkeLib.Lib.CustomEvents.ArmorType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum SetBonus {
    LEAP("Leap"){
        public void applyPotion(Player player){
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 99999, 0, false, false), false);
        }

        public void removePotion(Player player){
            player.removePotionEffect(PotionEffectType.JUMP);
        }
    }, VAMPIRE("Vampire"), LIFESTEAL("Life Steal"), SHIELDING("Shielding"), POISONOUS("Poisonous"), VENOMOUS("Venomous"),
    MONOPOLY("Monopoly"), WITHERING("Withering"), DECAYING("Decaying"),DAZZLING("Dazzling"), FEVERISH("Feverish"), SNEAKY("Sneaky"){
        public void applyPotion(Player player){
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 1, false,false),false);
        }
        public void removePotion(Player player){
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    },
    REGENERATIVE("Regenerative"){
        public void applyPotion(Player player){
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 99999, 1, false, false), false);
        }

        public void removePotion(Player player){
            player.removePotionEffect(PotionEffectType.REGENERATION);
        }
    }, SPEEDY("Speedy"){
        public void applyPotion(Player player){
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 0, false, false), false);
        }

        public void removePotion(Player player){
            player.removePotionEffect(PotionEffectType.SPEED);
        }
    }, RESISTANT("Resistant"){
        public void applyPotion(Player player){
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 99999, 0, false, false), false);
        }

        public void removePotion(Player player){
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
    },
    FIREPROOF("Fire Proof"){
        public void applyPotion(Player player){
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 99999, 0, false, false), false);
        }

        public void removePotion(Player player){
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        }
    }, HASTE("Haste"){
        public void applyPotion(Player player){
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 99999, 0, false, false), false);
        }

        public void removePotion(Player player){
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
        }
    };
    final String verbose;

    SetBonus(String verbose) {
        this.verbose = verbose;
    }

    String getVerbose() {
        return verbose;
    }
    public void applyPotion(Player player){}
    public void removePotion(Player player){}

    public static Set<SetBonus> parsePlayer(Player player) {
        Set<SetBonus> bonuses = null;
        boolean set = false;
        for (ItemStack is : player.getInventory().getArmorContents()) {
            if(is == null || is.getType() == Material.AIR) return Collections.emptySet();
            ArmorAccessories aa = ArmorAccessories.parseSetBonus(is);
            if (!set) {
                bonuses = new HashSet<>(aa.getSetBonus());
                set = true;
                continue;
            }
            bonuses.retainAll(aa.getSetBonus());
        }
        return bonuses;

    }

    public static Set<SetBonus> parsePlayer(ArmorType at, ItemStack item, Player player) {
        Set<SetBonus> bonuses = null;
        boolean set = false;
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (int i = 0; i < armor.length; i++) {

            ItemStack is;
            if (i == at.getSlot()) {
                is = item;
            } else {
                is = armor[i];
            }
            if(is == null || is.getType() == Material.AIR) return Collections.emptySet();
            ArmorAccessories aa = ArmorAccessories.parseSetBonus(is);
            if (!set) {
                bonuses = new HashSet<>(aa.getSetBonus());
                set = true;
                continue;
            }
            bonuses.retainAll(aa.getSetBonus());
        }
        return bonuses;
    }
}
