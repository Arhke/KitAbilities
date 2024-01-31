//package net.waterraid.KitAbilities.Utils;
//
//import org.bukkit.entity.Arrow;
//import org.bukkit.entity.LivingEntity;
//import org.bukkit.entity.Player;
//import org.bukkit.event.entity.EntityDamageByEntityEvent;
//import org.bukkit.event.entity.EntityRegainHealthEvent;
//
//public interface Attributes {
//    enum MCAttributes implements Attributes{
//        MAXHEALTH("generic.maxHealth", "Max Health"), KNOCKBACKRESIST("generic.knockbackResistance", "KnockBack Resist"),
//        /*DAMAGE("generic.attackDamage", "Damage"),*//* ARMOR("generic.armor", "Armor"),*/
//        /*ARMORTOUGHNESS("generic.armorToughness", "generic.armor_toughness"),*/ /*ATTACKSPEED("generic.attack_speed", "generic.attack_speed"),*/
//        /*LUCK("generic.luck","Luck"),*/ SPEED("generic.movementSpeed", "Speed");
//        final String name, verbose;
//        MCAttributes(String name, String newname){
//            this.name = name; this.verbose = newname;
//        }
//        public static MCAttributes getAttribute(String identifier){
//            for(MCAttributes attr: MCAttributes.values()){
//                if(attr.name().equals(identifier)){
//                    return attr;
//                }
//            }
//            return null;
//        }
//        public String getName() {
//            return name;
//        }
//        public String getVerbose(){
//            return this.verbose;
//        }
//    }
//    enum CustomAttributes implements Attributes{
//        DAMAGE("Damage"){
//
//        },
//        DODGE("Dodge"){
//            @Override
//            public void onEvent(EntityDamageByEntityEvent event, boolean receiver, double value){
//                if (!receiver) return;
//                if(!(event.getDamager() instanceof Player || event.getDamager() instanceof LivingEntity)) return;
//                if(value > Math.random()*100) event.setCancelled(true);
//            }
//        }, DEFLECT("Deflect"){
//            @Override
//            public void onEvent(EntityDamageByEntityEvent event, boolean receiver, double value){
//                if (!receiver) return;
//                if(!(event.getDamager() instanceof Arrow)) return;
//                if(value > Math.random()*100) event.setCancelled(true);
//            }
//        }, HEALING("Healing"){
//            @Override
//            public void onEvent(EntityRegainHealthEvent event, double value){
//                event.setAmount(event.getAmount()*(1+value/100d));
//            }
//        }, DEFENSE("Defense"){
//            @Override
//            public void onEvent(EntityDamageByEntityEvent event, boolean receiver, double value){
//                if (!receiver) return;
//                if(!(event.getDamager() instanceof Player || event.getDamager() instanceof LivingEntity)) return;
//                event.setDamage(event.getDamage()-value);
//            }
//        }, RANGEDDAMAGE("Ranged Damage"){
//            @Override
//            public void onEvent(EntityDamageByEntityEvent event, boolean receiver, double value){
//                if (receiver) return;
//                if(!(event.getDamager() instanceof Arrow)) return;
//                if(value > Math.random()*100) event.setCancelled(true);
//            }
//        },DURABILITY("Durability"){
//
//        };
//
//        public static Attributes getAttribute(String identifier){
//            for(CustomAttributes attr: CustomAttributes.values()){
//                if(attr.name().equals(identifier)){
//                    return attr;
//                }            }
//            return null;
//        }
//        final String verbose;
//        CustomAttributes(String verbose){
//            this.verbose = verbose;
//        }
//        @Override
//        public String getVerbose(){
//            return verbose;
//        }
//        public void onEvent(EntityDamageByEntityEvent event, boolean receiver, double value){}
//        public void onEvent(EntityRegainHealthEvent event, double value){}
//    }
//    static Attributes getAttribute(String identifier){
//        Attributes attr;
//        if((attr = MCAttributes.getAttribute(identifier))!=null) return attr;
//        if((attr = CustomAttributes.getAttribute(identifier))!=null) return attr;
//        return null;
//    }
//    String getVerbose();
//}
