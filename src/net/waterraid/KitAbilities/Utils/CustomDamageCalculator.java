//package net.waterraid.KitAbilities.Utils;
//
//
//
//import com.google.common.base.Function;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.ListTag;
//import net.minecraft.server.v1_8_R3.*;
//import net.minecraft.world.damagesource.CombatRules;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.item.enchantment.EnchantmentHelper;
//import net.minecraft.world.item.enchantment.ProtectionEnchantment;
//import org.apache.commons.lang.mutable.MutableInt;
//import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
//import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
//import org.bukkit.entity.Player;
//import org.bukkit.event.entity.EntityDamageEvent;
//import org.bukkit.potion.PotionEffectType;
//
//
//
//
//
//
//
//public class CustomProtectionCalculation {
//    protected boolean d(final DamageSource damagesource, float f) {
//        if (!this.isInvulnerable(damagesource)) {
//            final boolean human = this instanceof EntityHuman;
//            float originalDamage = f;
//            Function<Double, Double> hardHat = new Function<Double, Double>() {
//                public Double apply(Double f) {
//                    return (damagesource == DamageSource.ANVIL || damagesource == DamageSource.FALLING_BLOCK) && EntityLiving.this.getEquipment(4) != null ? -(f - f * 0.75D) : -0.0D;
//                }
//            };
//            float hardHatModifier = ((Double)hardHat.apply((double)f)).floatValue();
//            f += hardHatModifier;
//            Function<Double, Double> blocking = new Function<Double, Double>() {
//                public Double apply(Double f) {
//                    return human && !damagesource.ignoresArmor() && ((EntityHuman)EntityLiving.this).isBlocking() && f > 0.0D ? -(f - (1.0D + f) * 0.5D) : -0.0D;
//                }
//            };
//            float blockingModifier = ((Double)blocking.apply((double)f)).floatValue();
//            f += blockingModifier;
//            Function<Double, Double> armor = new Function<Double, Double>() {
//                public Double apply(Double f) {
//                    return -(f - (double)EntityLiving.this.applyArmorModifier(damagesource, f.floatValue()));
//                }
//            };
//            float armorModifier = ((Double)armor.apply((double)f)).floatValue();
//            f += armorModifier;
//            Function<Double, Double> resistance = new Function<Double, Double>() {
//                public Double apply(Double f) {
//                    if (!damagesource.isStarvation() && EntityLiving.this.hasEffect(MobEffectList.RESISTANCE) && damagesource != DamageSource.OUT_OF_WORLD) {
//                        int i = (EntityLiving.this.getEffect(MobEffectList.RESISTANCE).getAmplifier() + 1) * 5;
//                        int j = 25 - i;
//                        float f1 = f.floatValue() * (float)j;
//                        return -(f - (double)(f1 / 25.0F));
//                    } else {
//                        return -0.0D;
//                    }
//                }
//            };
//            float resistanceModifier = ((Double)resistance.apply((double)f)).floatValue();
//            f += resistanceModifier;
//            Function<Double, Double> magic = new Function<Double, Double>() {
//                public Double apply(Double f) {
//                    return -(f - (double)EntityLiving.this.applyMagicModifier(damagesource, f.floatValue()));
//                }
//            };
//            float magicModifier = ((Double)magic.apply((double)f)).floatValue();
//            f += magicModifier;
//            Function<Double, Double> absorption = new Function<Double, Double>() {
//                public Double apply(Double f) {
//                    return -Math.max(f - Math.max(f - (double)EntityLiving.this.getAbsorptionHearts(), 0.0D), 0.0D);
//                }
//            };
//            float absorptionModifier = ((Double)absorption.apply((double)f)).floatValue();
//            EntityDamageEvent event = CraftEventFactory.handleLivingEntityDamageEvent(this, damagesource, (double)originalDamage, (double)hardHatModifier, (double)blockingModifier, (double)armorModifier, (double)resistanceModifier, (double)magicModifier, (double)absorptionModifier, hardHat, blocking, armor, resistance, magic, absorption);
//            if (event.isCancelled()) {
//                return false;
//            } else {
//                f = (float)event.getFinalDamage();
//                if ((damagesource == DamageSource.ANVIL || damagesource == DamageSource.FALLING_BLOCK) && this.getEquipment(4) != null) {
//                    this.getEquipment(4).damage((int)(event.getDamage() * 4.0D + (double)this.random.nextFloat() * event.getDamage() * 2.0D), this);
//                }
//
//                float f2;
//                if (!damagesource.ignoresArmor()) {
//                    f2 = (float)(event.getDamage() + event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) + event.getDamage(EntityDamageEvent.DamageModifier.HARD_HAT));
//                    this.damageArmor(f2);
//                }
//
//                absorptionModifier = (float)(-event.getDamage(EntityDamageEvent.DamageModifier.ABSORPTION));
//                this.setAbsorptionHearts(Math.max(this.getAbsorptionHearts() - absorptionModifier, 0.0F));
//                if (f != 0.0F) {
//                    if (human) {
//                        ((EntityHuman)this).applyExhaustion(damagesource.getExhaustionCost());
//                        if (f < 3.4028235E37F) {
//                            ((EntityHuman)this).a(StatisticList.x, Math.round(f * 10.0F));
//                        }
//                    }
//
//                    f2 = this.getHealth();
//                    this.setHealth(f2 - f);
//                    this.bs().a(damagesource, f2, f);
//                    if (human) {
//                        return true;
//                    }
//
//                    this.setAbsorptionHearts(this.getAbsorptionHearts() - f);
//                }
//
//                return true;
//            }
//        } else {
//            return false;
//        }
//    }
//
//    protected static float getDamageAfterArmorAbsorb(LivingEntity le, DamageSource damagesource, float f) {
//        if (!damagesource.isBypassArmor()) {
//            f = CombatRules.getDamageAfterAbsorb(f, (float)le.getArmorValue(), (float)le.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
//        }
//
//        return f;
//    }
//    protected static float getDamageAfterMagicAbsorb(LivingEntity le, DamageSource damagesource, float f) {
//        if (damagesource.isBypassMagic()) {
//            return f;
//        } else if (f <= 0.0F) {
//            return 0.0F;
//        } else if (damagesource.isBypassEnchantments()) {
//            return f;
//        } else {
//            int i = getDamageProtection(le.getArmorSlots(), damagesource);
//            if (i > 0) {
//                f = CombatRules.getDamageAfterMagicAbsorb(f, (float)i);
//            }
//
//            return f;
//        }
//    }
//    public static int getDamageProtection(Iterable<net.minecraft.world.item.ItemStack> var0, DamageSource var1) {
//        MutableInt var2 = new MutableInt();
//        runIterationOnInventory((var2x, var3) -> {
//            var2.add(getDamageProtection(var2x, var3, var1));
//        }, var0);
//        return var2.intValue();
//    }
//    private static void runIterationOnInventory(EnchantmentVisitor var0, Iterable<net.minecraft.world.item.ItemStack> var1) {
//
//        for (net.minecraft.world.item.ItemStack var3 : var1) {
//            runIterationOnItem(var0, var3);
//        }
//    }
//    private static void runIterationOnItem(EnchantmentVisitor var0, net.minecraft.world.item.ItemStack var1) {
//        if (!var1.isEmpty()) {
//            ListTag var2 = var1.getEnchantmentTags();
//
//            for(int var3 = 0; var3 < var2.size(); ++var3) {
//                CompoundTag var4 = var2.getCompound(var3);
//                BuiltInRegistries.ENCHANTMENT.getOptional(EnchantmentHelper.getEnchantmentId(var4)).ifPresent((var2x) -> {
//                    var0.accept(var2x, EnchantmentHelper.getEnchantmentLevel(var4));
//                });
//            }
//
//        }
//    }
//    public static int getDamageProtection(net.minecraft.world.item.enchantment.Enchantment enchantment, int var0, DamageSource var1) {
//        if(!(enchantment instanceof ProtectionEnchantment )){
//            return enchantment.getDamageProtection(var0, var1);
//        }
//        ProtectionEnchantment pe = (ProtectionEnchantment)enchantment;
//        ProtectionEnchantment.Type type = pe.type;
//        if (var1.isBypassInvul()) {
//            return 0;
//        } else if (type == ProtectionEnchantment.Type.ALL) {
//            return var0;
//        } else if (type == ProtectionEnchantment.Type.FIRE && var1.isFire()) {
//            return (int)(var0 * 3.5);
//        } else if (type == ProtectionEnchantment.Type.FALL && var1.isFall()) {
//            return var0 * 3;
//        } else if (type == ProtectionEnchantment.Type.EXPLOSION && var1.isExplosion()) {
//            return (int)(var0 * 3.5);
//        } else {
//            return type == ProtectionEnchantment.Type.PROJECTILE && var1.isProjectile() ? var0 * 3 : 0;
//        }
//    }
//    @FunctionalInterface
//    private interface EnchantmentVisitor {
//        void accept(net.minecraft.world.item.enchantment.Enchantment var1, int var2);
//
//    }
//    public static double customDamageReduction(EntityDamageEvent.DamageCause dc, org.bukkit.entity.LivingEntity entity, double value){
//        if(value <= 0){
//            return 0d;
//        }
//        DamageSource ds;
//        LivingEntity le = ((CraftLivingEntity)entity).getHandle();
//        if (dc == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || dc == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION){
//            if(entity instanceof Player && ((Player)entity).isBlocking()){
//                return 0d;
//            }
//            ds = DamageSource.explosion(null);
//        } else if (dc == EntityDamageEvent.DamageCause.FIRE || dc == EntityDamageEvent.DamageCause.FIRE_TICK) {
//            ds = DamageSource.IN_FIRE;
//            if(entity.isInWater()){
//                return 0d;
//            }
//            if (entity.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
//                value *= 0.8;
//            }
//
//        }else if (dc == EntityDamageEvent.DamageCause.MAGIC) {
//            ds = DamageSource.MAGIC;
//        }else if (dc == EntityDamageEvent.DamageCause.PROJECTILE){
//            if(entity instanceof Player && ((Player)entity).isBlocking()){
//                return 0d;
//            }
//            net.minecraft.world.entity.projectile.Arrow arrow =
//                    new net.minecraft.world.entity.projectile.Arrow(le.getLevel(),
//                            le);
//            ds = DamageSource.arrow(arrow, le);
//        }else{
//            return value;
//        }
//        float val = (float)value;
//        val = getDamageAfterArmorAbsorb(le, ds, val);
//        val = getDamageAfterMagicAbsorb(le, ds, val);
//        return val;
//    }
//
//}
//
