package net.waterraid.KitAbilities.Utils;

//public class CustomAttributeListener implements Listener {
//    @EventHandler
//    public void durabilityEvent(PlayerItemDamageEvent event) {
//        NBTItem nbti = new NBTItem(event.getItem());
//        double value = com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.getCustomTagDouble(nbti, com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.Slot.MAINHAND.getCode(), com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomAttributes.DURABILITY);
//        value = event.getDamage() / Math.max(0.0001d, 1d + value);
//        event.setDamage((int) value);
//        value = value - (int) value;
//        if (Math.random() < value) {
//            event.setDamage(event.getDamage() + 1);
//        }
//
//    }
//
//    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
//    public void onPlayerDamage(EntityDamageByEntityEvent event) {
//        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
//            //Ranged Damage
//            //Deflect
//            if (event.getEntity() instanceof Player) {
//                Player player = (Player) event.getEntity();
//                if (Base.randNum(100) < com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.getPlayerAttribute(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomAttributes.DEFLECT) * 100) {
//                    player.getWorld().playSound(player.getLocation(), Sound.ANVIL_LAND, 3, 3);
//                    event.setCancelled(true);
//                    return;
//                }
//                double damage = event.getDamage();
//                //defense
//                damage = Math.max(0.001, damage - com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.getPlayerAttribute(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomAttributes.DEFENSE));
//                event.setDamage(damage);
//            }
//            if (event.getDamager() instanceof Arrow && ((Arrow) event.getDamager()).getShooter() instanceof Player && event.getEntity() instanceof LivingEntity) {
//                Player player = (Player) ((Arrow) event.getDamager()).getShooter();
//                double damage = event.getDamage() * Math.max(0, 1 + com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.getPlayerAttribute(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomAttributes.RANGEDDAMAGE));
//                //toughness
//                double toughness = ((LivingEntity) event.getEntity()).getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getValue();
//                damage *= (1f - Math.min(Math.max((damage * toughness - 120) / (damage * toughness + 0.0000001), 0f), 0.9f));
//                event.setDamage(damage);
//            }
//
//        } else if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK ) {
//            //DODGE
//            if (event.getEntity() instanceof Player) {
//                Player player = (Player) event.getEntity();
//                if (Base.randNum(100) < com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.getPlayerAttribute(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomAttributes.DODGE) * 100) {
//                    player.getWorld().playSound(player.getLocation(), Sound.BAT_TAKEOFF, 3, 3);
//                    event.setCancelled(true);
//                    return;
//                }
//                double damage = event.getDamage();
//                //defense
//                damage = Math.max(0.001, damage - com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.getPlayerAttribute(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomAttributes.DEFENSE));
//                event.setDamage(damage);
//            }
//            if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
//                Player player = (Player) event.getDamager();
//                LivingEntity le = (LivingEntity) event.getEntity();
//                //Valid On-Hit
//                if ((!(event.getEntity() instanceof Player) || !((Player) event.getEntity()).isBlocking())) {
//                    if (hasPlayerEffect(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.ONHITPOISON)) {
//                        le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 1, false, false));
//                    }
//                    if (hasPlayerEffect(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.ONHITSLOW)) {
//                        le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1, false, false));
//                    }
//                    if (hasPlayerEffect(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.ONHITWITHER)) {
//                        le.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 90, 1, false, false));
//                    }
//                    if (hasPlayerEffect(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.ONHITWEAKNESS)) {
//                        le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 0, false, false));
//                    }
//                    if (hasPlayerEffect(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.ONHITBLIND)) {
//                        le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0, false, false));
//                    }
//                    if (hasPlayerEffect(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.ONHITHUNGER)) {
//                        le.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 60, 5, false, false));
//                    }
//                    if (hasPlayerEffect(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.ONHITABSORPTION)) {
//                        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 60,0));
//                    }
//                    if (hasPlayerEffect(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.ONHITCLEANSER)) {
//                        le.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
//                        le.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
//                        le.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
//                        le.removePotionEffect(PotionEffectType.REGENERATION);
//                        le.removePotionEffect(PotionEffectType.ABSORPTION);
//                        le.removePotionEffect(PotionEffectType.HEALTH_BOOST);
////                        le.removePotionEffect(PotionEffectType.WATER_BREATHING);
//                        le.removePotionEffect(PotionEffectType.FAST_DIGGING);
//                        le.removePotionEffect(PotionEffectType.JUMP);
//                        le.removePotionEffect(PotionEffectType.NIGHT_VISION);
//                    }
//                    if (hasPlayerEffect(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.ANTIHEAL1)) {
//                        le.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 120, 0, false, false));
//                    }
//                    if (hasPlayerEffect(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.ANTIHEAL2)) {
//                        le.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 120, 1, false, false));
//                    }
//                    if (hasPlayerEffect(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.ANTIHEAL3)) {
//                        le.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 120, 2, false, false));
//                    }
//                    if (hasPlayerEffect(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.ANTIHEAL4)) {
//                        le.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 120, 3, false, false));
//                    }
//                    if (hasPlayerEffect(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.ONHITHEAL)) {
//                        EntityRegainHealthEvent ere = new EntityRegainHealthEvent(player, 1d, EntityRegainHealthEvent.RegainReason.CUSTOM);
//                        Bukkit.getPluginManager().callEvent(ere);
//                        if (!ere.isCancelled())
//                            player.setHealth(Math.min(ere.getAmount() + player.getHealth(), player.getMaxHealth()));
//                    }
//                    //CRIT
//                    if (player.getFallDistance() > 0.0F && !player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
//                        event.setDamage(Math.max(0.01, event.getDamage() + com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.getPlayerAttribute(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomAttributes.CRITDMG)));
//                    }
//                }
//                double damage = event.getDamage();
//                //toughness
//                double toughness = Objects.requireNonNull(le.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)).getValue();
//                damage *= (1f - Math.min(Math.max((damage * toughness - 120) / (damage * toughness + 0.0000001), 0f), 0.8f));
//                //physical
//                double val = (1 + com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.getPlayerAttribute(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomAttributes.PHYSDMG)) * damage;
//                event.setDamage(val);
//                //blast
//                val = com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.getPlayerAttribute(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomAttributes.BLASTDMG) * damage;
//                val = CustomProtectionCalculation.customDamageReduction(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, le, val);
//                event.setDamage(EntityDamageEvent.DamageModifier.BASE, val + event.getDamage(EntityDamageEvent.DamageModifier.BASE));
//                //Proj
//                val = com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.getPlayerAttribute(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomAttributes.PROJDMG) * damage;
//                val = CustomProtectionCalculation.customDamageReduction(EntityDamageEvent.DamageCause.PROJECTILE, le, val);
//                event.setDamage(EntityDamageEvent.DamageModifier.BASE, val + event.getDamage(EntityDamageEvent.DamageModifier.BASE));
//                //Fire
//                val = com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.getPlayerAttribute(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomAttributes.FIREDMG) * damage;
//                val = CustomProtectionCalculation.customDamageReduction(EntityDamageEvent.DamageCause.FIRE, le, val);
//                event.setDamage(EntityDamageEvent.DamageModifier.BASE, val + event.getDamage(EntityDamageEvent.DamageModifier.BASE));
//                //Magic
//                val = com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.getPlayerAttribute(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomAttributes.MAGICDMG) * damage;
//                val = CustomProtectionCalculation.customDamageReduction(EntityDamageEvent.DamageCause.MAGIC, le, val);
//                if (le instanceof Player)
//                    val = val * (1 - Math.min(1, com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.getPlayerAttribute((Player) le, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomAttributes.MAGICRESIST)));
//                event.setDamage(EntityDamageEvent.DamageModifier.BASE, val + event.getDamage(EntityDamageEvent.DamageModifier.BASE));
//                if (event.getFinalDamage() != 0) {
//                    double value = com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.getPlayerAttribute(player, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomAttributes.LIFESTEAL);
//                    if (value != 0) {
//                        EntityRegainHealthEvent ere = new EntityRegainHealthEvent(player, event.getFinalDamage() * value, EntityRegainHealthEvent.RegainReason.CUSTOM);
//                        Bukkit.getPluginManager().callEvent(ere);
//                        if (!ere.isCancelled())
//                            player.setHealth(Math.min(ere.getAmount() + player.getHealth(), player.getMaxHealth()));
//                    }
//
//                }
//            }
//            if (event.getDamage() <= 0) {
//                event.setDamage(0.001);
//            }
//
//
//        }
//    }
//
//    @EventHandler
//    public void onPotion(EntityPotionEffectEvent event) {
//        if (event.getEntity() instanceof Player && event.getCause() != EntityPotionEffectEvent.Cause.COMMAND) {
//            Player p = (Player) event.getEntity();
//            if (event.getNewEffect() != null) {
//                if (event.getModifiedType().equals(PotionEffectType.POISON) && hasPlayerEffect(p, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.POISONRESISTANCE)) {
//                    event.setCancelled(true);
//                    return;
//                }
//                if (event.getModifiedType().equals(PotionEffectType.WITHER) && hasPlayerEffect(p, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.WITHERRESISTANCE)) {
//                    event.setCancelled(true);
//                    return;
//                }
//                if (event.getModifiedType().equals(PotionEffectType.WATER_BREATHING) && hasPlayerEffect(p, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.ANTIHEALRESISTANCE)) {
//                    event.setCancelled(true);
//                    return;
//                }
//                if (event.getModifiedType().equals(PotionEffectType.WEAKNESS) && hasPlayerEffect(p, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.WEAKNESSRESISTANCE)) {
//                    event.setCancelled(true);
//                    return;
//                }
//                if (event.getModifiedType().equals(PotionEffectType.SLOW) && hasPlayerEffect(p, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.SLOWRESISTANCE)) {
//                    event.setCancelled(true);
//                    return;
//                }
//                if (event.getModifiedType().equals(PotionEffectType.HUNGER) && hasPlayerEffect(p, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.HUNGERRESISTANCE)) {
//                    event.setCancelled(true);
//                    return;
//                }
//                if (event.getModifiedType().equals(PotionEffectType.BLINDNESS) && hasPlayerEffect(p, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.BLINDRESISTANCE)) {
//                    event.setCancelled(true);
//                    return;
//                }
//                if (event.getModifiedType().equals(PotionEffectType.SLOW_DIGGING) && hasPlayerEffect(p, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.FATIGUERESISTANCE)) {
//                    event.setCancelled(true);
//                    return;
//                }
//                if (event.getModifiedType().equals(PotionEffectType.CONFUSION) && hasPlayerEffect(p, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomEffects.CONFUSIONRESISTANCE)) {
//                    event.setCancelled(true);
//                    return;
//                }
//            }
//        }
//
//    }
//
//    @EventHandler
//    public void onAccessory(InventoryClickEvent event) {
//        ItemStack current = event.getCurrentItem();
//        ItemStack cursor = event.getCursor();
//        if (current == null || cursor == null || current.getType() == Material.AIR || cursor.getType() == Material.AIR)
//            return;
//        ItemStack newItem = new ItemStack(current);
//        newItem.setAmount(1);
//        NBTItem nbtiCurrent = new NBTItem(newItem);
//        NBTItem nbtiCursor = new NBTItem(cursor);
//        if (!com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.isAccessory(nbtiCursor) || !com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.canAttach(nbtiCurrent))
//            return;
//        com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags at = new com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags(nbtiCurrent);
//        at.addAccessory(nbtiCursor, EquipmentType.getEquipmentType(current.getType()).getSlot());
//        Base.itemAmountMinus(cursor);
//        Base.itemAmountMinus(current);
//        newItem = at.getItem();
//        if (event.getWhoClicked() instanceof Player) {
//            Player p = Bukkit.getPlayer(event.getWhoClicked().getName());
//            assert p != null;
//            Base.addItemToPlayer(p, newItem);
//        } else {
//            event.getWhoClicked().getInventory().addItem(newItem);
//        }
//    }
//
//    @EventHandler(priority = EventPriority.LOW)
//    public void onHeal(EntityRegainHealthEvent event) {
//        if (event.getEntity() instanceof Player) {
//            Player p = (Player) event.getEntity();
//            double healRedux = 1d;
//            PotionEffect pe = p.getActivePotionEffects().stream().filter((a)->(a.getType() == PotionEffectType.WATER_BREATHING)).findFirst().orElse(null);
//            if (pe != null) {
//                if (pe.getAmplifier() == 0) {
//                    healRedux = 0.75d;
//                } else if (pe.getAmplifier() == 1) {
//                    healRedux = 0.50d;
//                } else if (pe.getAmplifier() == 2) {
//                    healRedux = 0.25d;
//                } else if (pe.getAmplifier() >= 3) {
//                    healRedux = 0.0d;
//                }
//            }
//            event.setAmount(healRedux * event.getAmount() * (1 + Math.max(-1, com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.getPlayerAttribute(p, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomAttributes.HEALING))));
//        }
//    }
//
//    @EventHandler
//    public void onHunger(FoodLevelChangeEvent event) {
//        if (event.getEntity() instanceof Player) {
//            Player p = (Player) event.getEntity();
//            int change = p.getFoodLevel() - event.getFoodLevel();
//            if (change <= 0) return;
//            double value = com.Arhke.ArhkeLib.ItemUtil.CustomItem.ArmorTags.getPlayerAttribute(p, com.Arhke.ArhkeLib.ItemUtil.CustomItem.Attributes.CustomAttributes.HUNGER);
//            value = change * Math.max(0.0d, 1d - value);
//            change = (int) value;
//            if (Math.random() < value) {
//                change++;
//            }
//            change = p.getFoodLevel() - change;
//            event.setFoodLevel(change);
//
//        }
//    }
//
//    @EventHandler
//    public void damaged(EntityDamageEvent event) {
//        if ((event.getCause() == EntityDamageEvent.DamageCause.MAGIC ||
//                event.getCause() == EntityDamageEvent.DamageCause.WITHER ||
//                event.getCause() == EntityDamageEvent.DamageCause.POISON) && event.getEntity() instanceof Player) {
//            Player player = (Player) event.getEntity();
//            event.setDamage(event.getDamage() * (1 - Math.min(1, ArmorTags.getPlayerAttribute(player, Attributes.CustomAttributes.MAGICRESIST))));
//        }
//    }
//}
//Magic, Physical, Blast, Fire, Projectile
// (Vampirism, LifeSteal, withering, poisonous, Ice Aspect)