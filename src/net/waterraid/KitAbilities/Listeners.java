package net.waterraid.KitAbilities;

import com.Arhke.ArhkeLib.Lib.Base.Base;
import com.Arhke.ArhkeLib.Lib.CustomEvents.ArmorEquipEvent1_8;
import com.Arhke.ArhkeLib.Lib.CustomEvents.ArmorType;
import com.Arhke.ArhkeLib.Lib.CustomEvents.TrueDamageEvent;
import com.Arhke.ArhkeLib.Lib.FileIO.DataManager;
import com.sk89q.worldguard.bukkit.WGBukkit;
import de.tr7zw.nbtapi.NBTItem;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.exceptions.InvalidMobTypeException;
import me.elsiff.morefish.event.PlayerCatchCustomFishEvent;
import me.neznamy.tab.shared.Shared;
import net.ess3.api.events.teleport.PreTeleportEvent;
import net.minecraft.server.v1_8_R3.EntityFishingHook;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.waterraid.KitAbilities.Abilities.Templates.Abilities;
import net.waterraid.KitAbilities.Armor.Armor;
import net.waterraid.KitAbilities.Armor.Weapons;
import net.waterraid.KitAbilities.Commands.PotionFillCommand;
import net.waterraid.KitAbilities.Effects.DurationEffect;
import net.waterraid.KitAbilities.LavaFishing.LavaFishingHook;
import net.waterraid.KitAbilities.Managers.PlayerData;
import net.waterraid.KitAbilities.Managers.PlayerDataManager;
import net.waterraid.KitAbilities.Utils.*;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFish;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import static com.Arhke.ArhkeLib.Lib.Base.Base.tcm;
import java.util.*;

@SuppressWarnings("unused")
public class Listeners implements Listener {
    DataManager config;
    PlayerDataManager pdManager;
    //(range: 0.0 - 4.0)
    final double speed = 1.0;
    HashMap<UUID, Inventory> deathLootMap = new HashMap<>();
    public Listeners(DataManager dm, PlayerDataManager pdManager) {
        config = dm;
    }

    @EventHandler
    public void onThrowStuff(PlayerDropItemEvent event) {
        if (isAbility(event.getItemDrop().getItemStack())) {
            event.getPlayer().sendMessage(Base.tcm(config.getString("NoThrowAbility")));
            event.setCancelled(true);
            return;
        }
        if (event.getItemDrop().getItemStack().isSimilar(PotionFillCommand.SplashHeal)) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity().hasMetadata("NPC")) {
            return;
        }
        new BukkitRunnable(){
            @Override
            public void run(){
                if(!event.getEntity().isOnline()) {
                    return;
                }
                event.getEntity().spigot().respawn();
                event.getEntity().setExp(0);
            }
        }.runTaskLater(Main.getPlugin(), 10L);
        new BukkitRunnable(){
            @Override
            public void run(){
                if(!event.getEntity().isOnline()) {
                    return;
                }
                Shared.getPlayer(event.getEntity().getUniqueId()).detectBossBarsAndSend();
            }
        }.runTaskLater(Main.getPlugin(), 13L);
        Inventory inv = Bukkit.createInventory(null, 6 * 9, tcm("{0}'s Death Loot", event.getEntity().getDisplayName()));
        event.getDrops().stream().filter((a)->!a.isSimilar(PotionFillCommand.SplashHeal) && !isAbility(a))
                .forEach(inv::addItem);
        event.getDrops().clear();
        PlayerData pd = Main.getPlugin().getPDManager().getOrNewData(event.getEntity().getUniqueId());
        Iterator<DurationEffect> iEffects = pd.getEffectIterator();
        while (iEffects.hasNext()) {
            DurationEffect effects = iEffects.next();
            if (!effects.isExpired()) effects.onEvent(event);
            else iEffects.remove();
        }
        if (pd.getAbilityKit() != null) {
            pd.getAbilityKit().onEvent(event);
        }
        if (event.getEntity().getKiller() != null) {
            event.getEntity().sendMessage(tcm("&f&m>-------------&a &c[&dKitpvp &8* &c]&a &f&m-------------<"));
            event.getEntity().sendMessage("");
            event.getEntity().sendMessage(tcm("&7You were killed by &c{0}", event.getEntity().getKiller().getName()));
            event.getEntity().sendMessage("");
            event.getEntity().sendMessage(tcm("&f&m>-----------------------------------<"));
            event.getEntity().getKiller().sendMessage(tcm("&f&m>-------------&a &c[&dKitpvp &8* &c]&a &f&m-------------<"));
            event.getEntity().getKiller().sendMessage("");
            event.getEntity().getKiller().sendMessage(tcm("&7You have killed &a{0} {1}", event.getEntity().getName(), "[View Inventory]"));
            pd = Main.getPlugin().getPDManager().getOrNewData(event.getEntity().getKiller().getUniqueId());
            iEffects = pd.getEffectIterator();
            while (iEffects.hasNext()) {
                DurationEffect effects = iEffects.next();
                if (!effects.isExpired()) effects.onEvent(event);
                else iEffects.remove();
            }
            if (pd.getAbilityKit() != null) {
                pd.getAbilityKit().onEvent(event);
            }
            Set<SetBonus> bonuses = SetBonus.parsePlayer(event.getEntity().getKiller());
            PluginManager pm = Bukkit.getPluginManager();
            if(bonuses.contains(SetBonus.MONOPOLY)){
                Main.getPlugin().getHook().depositMoney(event.getEntity().getKiller(), 5d);
            }
            upgradeArmor(event.getEntity().getKiller());
            event.getEntity().getKiller().sendMessage("");
            event.getEntity().getKiller().sendMessage(tcm("&f&m>-----------------------------------<"));
        } else {
            event.getEntity().sendMessage(tcm("&f&m>-------------&a &c[&dKitpvp &8* &c]&a &f&m-------------<"));
            event.getEntity().sendMessage("");
            event.getEntity().sendMessage(tcm("&7You have died."));
            event.getEntity().sendMessage("");
            event.getEntity().sendMessage(tcm("&f&m>-----------------------------------<"));
        }
        Main.getPlugin().getPDManager().deathPlayer(event.getEntity().getUniqueId());
    }

    @EventHandler
    public void weatherChange(WeatherChangeEvent event) {
        if (event.getWorld().getGameRuleValue("doWeatherCycle").equals("false") && event.toWeatherState()) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onAccessoryClick(InventoryClickEvent event) {
        if(event.getCursor() == null || event.getCurrentItem() == null) return;
        ItemStack current = event.getCurrentItem(), cursor = event.getCursor();
        if(cursor.getItemMeta() == null || current.getItemMeta() == null) return;
        String mat = current.getType().name();
        if(!(mat.contains("HELMET") || mat.contains("CHESTPLATE") || mat.contains("LEGGINGS") || mat.contains("BOOTS"))){
            return;
        }
        List<String> cursorLore = cursor.getItemMeta().getLore();
        if(cursorLore == null) return;
        if(cursorLore.size() < 1 || !cursorLore.get(0).toLowerCase().contains("accessory")){
            return;
        }
        ArmorAccessories armor = new ArmorAccessories(current), accessory = new ArmorAccessories(cursor);
        if(!armor.addAccessory(accessory)){
            event.getWhoClicked().sendMessage(tcm("&cOops! Sorry, you can't do that."));
            return;
        }
        event.setCancelled(true);
        cursor.setAmount(cursor.getAmount()-1);
        event.getWhoClicked().setItemOnCursor(cursor);
        armor.regenerateLore();
        event.setCurrentItem(armor.getIs());



    }


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (event.getClickedBlock().getType() == Material.ANVIL
                || event.getClickedBlock().getType() == Material.FURNACE || event.getClickedBlock().getType() == Material.BURNING_FURNACE)) {
            event.getPlayer().sendMessage(tcm(config.getString("CantUseCrafting")));
            event.setCancelled(true);
        }
        PlayerData pd = Main.getPlugin().getPDManager().getOrNewData(event.getPlayer().getUniqueId());
        Iterator<DurationEffect> iterator = pd.getEffectIterator();
        while (iterator.hasNext()) {
            DurationEffect effects = iterator.next();
            effects.onEvent(event);
        }
        if (pd.getAbilityKit() == null) {
            return;
        }
        pd.getAbilityKit().onEvent(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEquip(ArmorEquipEvent1_8 event){
        System.out.println("Caught " + event.getPlayer());
        if(event.getType() == null)return;
        HashSet<SetBonus> oldbonuses = new HashSet<>(), newbonuses = new HashSet<>();
        if(event.getOldArmorPiece()!= null && event.getOldArmorPiece().getType() != Material.AIR){
            oldbonuses.addAll(SetBonus.parsePlayer(event.getType(), event.getOldArmorPiece(), event.getPlayer()));
        }
        if(event.getNewArmorPiece()!= null && event.getNewArmorPiece().getType() != Material.AIR){
            newbonuses.addAll(SetBonus.parsePlayer(event.getType(), event.getNewArmorPiece(), event.getPlayer()));
        }
        Set<SetBonus> copyOfOldBonus = new HashSet<>(oldbonuses);
        oldbonuses.removeAll(newbonuses);
        newbonuses.removeAll(copyOfOldBonus);
        oldbonuses.forEach((a)->a.removePotion(event.getPlayer()));
        newbonuses.forEach((a)->a.applyPotion(event.getPlayer()));



    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }if (event.getEntity().hasMetadata("NPC")) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            event.setDamage(event.getDamage()*50);
        }

        PlayerData pd = Main.getPlugin().getPDManager().getOrNewData(player.getUniqueId());
        Iterator<DurationEffect> iEffects = pd.getEffectIterator();
        while (iEffects.hasNext()) {
            DurationEffect effects = iEffects.next();
            if (!effects.isExpired()) effects.onEvent(event);
            else iEffects.remove();
        }
        if (pd.getAbilityKit() != null) {
            pd.getAbilityKit().onEvent(event);
        }
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onEntityHitEntity(EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerData pd = Main.getPlugin().getPDManager().getData(event.getEntity().getUniqueId());
            Attributes.CustomAttributes aca;
            if (event.getDamager() instanceof Arrow) {
                aca = Attributes.CustomAttributes.DEFLECT;
            } else {
                aca = Attributes.CustomAttributes.DODGE;
            }
            double ret = 1;
            double defense = 1;
            for (ItemStack is : ((Player) event.getEntity()).getInventory().getArmorContents()) {
                if (is != null && is.getType() != Material.AIR) {
                    ArmorAccessories aa = ArmorAccessories.parseCustom(is);
                    ret *= Math.max(0,(1-aa.getCustomAttr().getOrDefault(aca, 0)/100d));
                    defense *= (1-aa.getCustomAttr().getOrDefault(Attributes.CustomAttributes.DEFENSE, 0)/100d);
                }
            }
            if (Math.random() > ret) {
                Location loc = player.getLocation();
                if (event.getDamager() instanceof Arrow) {
                    event.getDamager().remove();
                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, 3, 3);
                    ProjectileSource ps = ((Arrow) event.getDamager()).getShooter();
                    if (ps instanceof CraftPlayer) {
                        ((CraftPlayer) ps).playSound(((Player)ps).getLocation(), Sound.ANVIL_LAND, 3, 3);
                    }
                    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.SNOWBALL, false, (float) (loc.getX()), (float) (loc.getY()), (float) loc.getZ(), 0.2f, 0.2f, 0.2f, 0, 200);
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (!online.getUniqueId().equals(player.getUniqueId()))
                            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                    }
                } else {
                    Location loc1 = event.getDamager().getLocation(), loc2 = event.getEntity().getLocation();
                    Vector oldV = new Vector(loc1.getX()-loc2.getX(),loc1.getY()-loc2.getY(),loc1.getZ()-loc2.getZ());
                    Vector v = new Vector(-oldV.getZ(), 0 , oldV.getX());
                    v.normalize().multiply(2.6 * (Base.randInt(2)-0.5));
                    v.setY(0.1);
                    player.setVelocity(v);
                    player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 3, 3);
                    if (event.getDamager() instanceof Player) {
                        ((Player) event.getDamager()).playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 3, 3);
                    }
                    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.CLOUD, false, (float) (loc.getX()), (float) (loc.getY()), (float) loc.getZ(), 0.2f, 0.1f, 0.2f, 0, 200);
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (!online.getUniqueId().equals(player.getUniqueId()))
                            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                    }
                }
                event.setCancelled(true);
            } else {
                event.setDamage(Math.max(event.getDamage()*0.2, event.getDamage()*defense));
            }
            if(SetBonus.parsePlayer((Player) event.getEntity()).contains(SetBonus.SHIELDING) && pd.shieldingCD < System.currentTimeMillis()){
                pd.shieldingCD = System.currentTimeMillis()+ 5000;
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 30, 2, false, false), false);
            }
            if (pd != null) {
                Iterator<DurationEffect> iterator = pd.getEffectIterator();
                while (iterator.hasNext()) {
                    DurationEffect effects = iterator.next();
                    if (effects.isExpired()) iterator.remove();
                    else effects.onEvent(event);
                }
                if (pd.getAbilityKit() != null) {
                    pd.getAbilityKit().onEvent(event);
                }
            }
        }
        if (event.getDamager() instanceof Player) {
            Player player = (Player)event.getDamager();
            int ret = 0;
            for (ItemStack is : ((Player) event.getDamager()).getInventory().getArmorContents()) {
                if (is != null && is.getType() != Material.AIR) {
                    ret += ArmorAccessories.parseCustom(is).getCustomAttr().getOrDefault(Attributes.CustomAttributes.DAMAGE, 0);
                }
            }
            event.setDamage(Math.max(0.0001, event.getDamage() + ret));



            PlayerData pd = Main.getPlugin().getPDManager().getData(event.getDamager().getUniqueId());
            if (pd != null) {
                Iterator<DurationEffect> iterator = pd.getEffectIterator();
                while (iterator.hasNext()) {
                    DurationEffect effects = iterator.next();
                    if (effects.isExpired()) iterator.remove();
                    else effects.onEvent(event);
                }
                if (pd.getAbilityKit() != null) pd.getAbilityKit().onEvent(event);
            }
            Set<SetBonus> bonuses = SetBonus.parsePlayer(player);
            PluginManager pm = Bukkit.getPluginManager();
            if(bonuses.contains(SetBonus.VAMPIRE)){
                EntityRegainHealthEvent ere = new EntityRegainHealthEvent(player,1d, EntityRegainHealthEvent.RegainReason.CUSTOM);
                pm.callEvent(ere);
                if(!ere.isCancelled())
                player.setHealth(Math.min(ere.getAmount() + player.getHealth(), player.getMaxHealth()));
            }
            if(bonuses.contains(SetBonus.LIFESTEAL)){
                EntityRegainHealthEvent ere = new EntityRegainHealthEvent(player,event.getFinalDamage(), EntityRegainHealthEvent.RegainReason.CUSTOM);
                pm.callEvent(ere);
                if(!ere.isCancelled())
                    player.setHealth(Math.min(ere.getAmount() + player.getHealth(), player.getMaxHealth()));
            }
            if(bonuses.contains(SetBonus.POISONOUS) && event.getEntity() instanceof LivingEntity){
                LivingEntity le = (LivingEntity) event.getEntity();
                le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 1, false, false));
            }
            if(bonuses.contains(SetBonus.VENOMOUS) && event.getEntity() instanceof LivingEntity){
                LivingEntity le = (LivingEntity) event.getEntity();
                le.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 2, false, false));
            }
            if(bonuses.contains(SetBonus.WITHERING) && event.getEntity() instanceof LivingEntity){
                LivingEntity le = (LivingEntity) event.getEntity();
                le.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 1, false, false));
            }
            if(bonuses.contains(SetBonus.DECAYING) && event.getEntity() instanceof LivingEntity){
                LivingEntity le = (LivingEntity) event.getEntity();
                le.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 2, false, false));
            }
            if(bonuses.contains(SetBonus.FEVERISH) && event.getEntity() instanceof LivingEntity){
                LivingEntity le = (LivingEntity) event.getEntity();
                le.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 2, false, false));
            }if(bonuses.contains(SetBonus.DAZZLING) && event.getEntity() instanceof LivingEntity){
                LivingEntity le = (LivingEntity) event.getEntity();
                le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 2, false, false));
            }

        }
        else if (event.getDamager() instanceof Arrow) {
            ProjectileSource ps = ((Arrow) event.getDamager()).getShooter();

            if (ps instanceof CraftPlayer) {
                double ret = 1;
                for (ItemStack is : ((Player) ps).getInventory().getArmorContents()) {
                    if (is != null && is.getType() != Material.AIR) {
                        ret *= (1+ArmorAccessories.parseCustom(is).getCustomAttr().getOrDefault(Attributes.CustomAttributes.RANGEDDAMAGE, 0)/100d);
                    }
                }
                event.setDamage(Math.max(0.01, event.getDamage()*ret));
                if (event.getEntity() instanceof Player) {
                    ArrowHitPlayerEvent ahpe;
                    Bukkit.getPluginManager().callEvent(
                            ahpe = new ArrowHitPlayerEvent(((CraftPlayer) ps).getPlayer(), (Player) event.getEntity(), (Arrow) event.getDamager(), event.getDamage(), event.getFinalDamage(), false));
                    event.setCancelled(ahpe.isCancelled());


                }
                Player player = (CraftPlayer)ps;
                Set<SetBonus> bonuses = SetBonus.parsePlayer(player);
                PluginManager pm = Bukkit.getPluginManager();
                if (bonuses.contains(SetBonus.VAMPIRE)) {
                    EntityRegainHealthEvent ere = new EntityRegainHealthEvent(player, 1d, EntityRegainHealthEvent.RegainReason.CUSTOM);
                    pm.callEvent(ere);
                    if(!ere.isCancelled())

                        player.setHealth(Math.min(ere.getAmount() + player.getHealth(), player.getMaxHealth()));
                }
                if (bonuses.contains(SetBonus.LIFESTEAL)) {
                    EntityRegainHealthEvent ere = new EntityRegainHealthEvent(player, event.getFinalDamage(), EntityRegainHealthEvent.RegainReason.CUSTOM);
                    pm.callEvent(ere);
                    if(!ere.isCancelled())
                        player.setHealth(Math.min(ere.getAmount() + player.getHealth(), player.getMaxHealth()));
                }
            }

        } else if (event.getDamager() instanceof FishHook) {
            ProjectileSource ps = ((FishHook) event.getDamager()).getShooter();
            if (!(ps instanceof Player)) {
                return;
            }
            PlayerData pd = Main.getPlugin().getPDManager().getData(((Player) ps).getUniqueId());
            if (pd != null) {
                Iterator<DurationEffect> iterator = pd.getEffectIterator();
                while (iterator.hasNext()) {
                    DurationEffect effects = iterator.next();
                    if (effects.isExpired()) iterator.remove();
                    else effects.onEvent(event);
                }
                if (pd.getAbilityKit() != null) pd.getAbilityKit().onEvent(event);
            }
        }

    }
    @EventHandler
    public void onXPGain(PlayerExpChangeEvent event){
        event.setAmount(0);

    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onFishEvent(ProjectileLaunchEvent event) {
        if(event.getEntity() instanceof CraftFish && !(((CraftFish) event.getEntity()).getHandle() instanceof LavaFishingHook)){
            CraftFish cf = (CraftFish) event.getEntity();
            World w = cf.getWorld();
            Location loc = cf.getLocation();
            LavaFishingHook lfh = new LavaFishingHook(((CraftWorld)w).getHandle());
            EntityFishingHook efh = ((CraftFish) event.getEntity()).getHandle();
            ((CraftWorld)w).getHandle().addEntity(lfh);
            efh.owner.hookedFish = lfh;
            lfh.owner = efh.owner;
            efh.owner = null;
            CraftFish lfhcf = new CraftFish(((CraftServer)Bukkit.getServer()), lfh);
            lfhcf.setShooter(cf.getShooter());
            lfhcf.setVelocity(cf.getVelocity());
            lfhcf.teleport(cf.getLocation());
            ((CraftFish) event.getEntity()).getHandle().die();
            event.setCancelled(true);

        }
    }

    @EventHandler
    public void onProjectileLaunchReal(ProjectileLaunchEvent event) {
        if(!(event.getEntity().getShooter() instanceof Player)) return;
        Player player = (Player) event.getEntity().getShooter();
        PlayerData pd = Main.getPlugin().getPDManager().getData(player.getUniqueId());
        if (pd != null) {
            Iterator<DurationEffect> iterator = pd.getEffectIterator();
            while (iterator.hasNext()) {
                DurationEffect effects = iterator.next();
                if (effects.isExpired()) iterator.remove();
                else effects.onEvent(event);
            }
            if (pd.getAbilityKit() != null && !event.isCancelled()) pd.getAbilityKit().onEvent(event);
        }
    }
    @EventHandler
    public void onSpawn(PreTeleportEvent event) {
        Player p = event.getTeleportee().getBase();
        if (WGBukkit.getRegionManager(p.getWorld()).
                getApplicableRegions(event.getTarget().getLocation()).getRegions().
                stream().anyMatch((a)-> a.getId().equalsIgnoreCase("star"))){
            if (p.isOp()) {
                p.performCommand("if");
            } else {
                p.setOp(true);
                p.performCommand("if");
                p.setOp(false);
            }
        }
    }
    @EventHandler
    public void onFish(PlayerFishEvent event) {
        event.setExpToDrop(0);
        if(event.getCaught() != null && event.getCaught() instanceof Item) {
            event.getCaught().setFireTicks(0);
            event.getCaught().teleport(event.getCaught().getLocation().add(0,1,0));
        }
        Player player = event.getPlayer();
        PlayerData pd = Main.getPlugin().getPDManager().getData(player.getUniqueId());
        if (pd != null) {
            Iterator<DurationEffect> iterator = pd.getEffectIterator();
            while (iterator.hasNext()) {
                DurationEffect effects = iterator.next();
                if (effects.isExpired()) iterator.remove();
                else effects.onEvent(event);
            }
            if (pd.getAbilityKit() != null && !event.isCancelled()) pd.getAbilityKit().onEvent(event);
        }
    }
    @EventHandler
    public void onCustomFish(PlayerCatchCustomFishEvent event) {
        Entity e = null;
        try {
            e = MythicMobs.inst().getAPIHelper().spawnMythicMob(event.getFish().getInternalName(), event.getPlayerFishEvent().getHook().getLocation());
        }catch(InvalidMobTypeException exception){
            exception.printStackTrace();
        }
        if (e != null){
            Entity remove = event.getPlayerFishEvent().getCaught();
            ((CraftFish)event.getPlayerFishEvent().getHook()).getHandle().hooked = ((CraftEntity)e).getHandle();
            e.teleport(remove.getLocation().add(0,1,0));
            e.setVelocity(remove.getVelocity().multiply(1.5));

            remove.remove();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void armorstandEvent(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player || event.getDamager() instanceof Arrow) ||
                !(event.getEntity() instanceof LivingEntity) || event.getEntity() instanceof ArmorStand) return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        long dmg = (long) (Math.ceil(entity.getHealth()) - Math.max(Math.ceil(entity.getHealth() - event.getFinalDamage()), 0));
        String health = dmg % 2 == 1 ? ChatColor.BLACK + "❤" : "";
        for (int i = 0; i < dmg / 2; i++) {
            health = "❤" + health;
        }
        setDamageIndicator(entity, health, entity.getVelocity().clone(), event.getDamager().getFallDistance() > 0 && !event.getDamager().isOnGround());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void armorstandEvent(TrueDamageEvent event) {
        if (event.getDamager() == null ||
                event.getEntity() == null || event.getEntity() instanceof ArmorStand) return;
        LivingEntity entity = event.getEntity();
        long dmg = (long) (Math.ceil(entity.getHealth()) - Math.max(Math.ceil(entity.getHealth() - event.getDamage()), 0));
        String health = dmg % 2 == 1 ? ChatColor.BLACK + "❤" : "";
        for (int i = 0; i < dmg / 2; i++) {
            health = "❤" + health;
        }
        final String hearts = health;
        new BukkitRunnable() {
            @Override
            public void run() {
                ArmorStand armorstand = (ArmorStand) entity.getWorld().spawnEntity(entity.getEyeLocation(), EntityType.ARMOR_STAND);
                armorstand.setVisible(false);
                armorstand.setGravity(true);
                armorstand.setVelocity(new Vector(Math.random() * 0.4 - 0.2, 0, Math.random() * 0.4 - 0.2).add(entity.getVelocity()));
                armorstand.setSmall(true);
                armorstand.setMarker(true);
                armorstand.setCustomNameVisible(true);
                armorstand.setCustomName(ChatColor.WHITE + "-" + hearts + ChatColor.WHITE + "*");
                setTicksLived(armorstand, 19);
            }
        }.runTaskLater(Main.getPlugin(), 1);
    }

    @EventHandler
    public void onEvent(PlayerToggleFlightEvent event) {
        PlayerData pd = Main.getPlugin().getPDManager().getData(event.getPlayer().getUniqueId());
        if (pd != null) {
            Iterator<DurationEffect> iterator = pd.getEffectIterator();
            while (iterator.hasNext()) {
                DurationEffect effects = iterator.next();
                if (effects.isExpired()) iterator.remove();
                else effects.onEvent(event);
            }
        }
    }

    @EventHandler
    public void onArrowHitPlayer(ArrowHitPlayerEvent event) {
        if (event.getPlayer().getUniqueId().equals(event.getShooter().getUniqueId())) {
            event.getShooter().sendMessage(ChatColor.RED + "Sorry, Bow Boosting is hereby disabled as of 7/27");
            event.setCancelled(true);
            return;
        }
        PlayerData pd = Main.getPlugin().getPDManager().getData(event.getShooter().getUniqueId());
        if (pd != null) {
            Iterator<DurationEffect> iterator = pd.getEffectIterator();
            while (iterator.hasNext()) {
                DurationEffect effects = iterator.next();
                if (effects.isExpired()) iterator.remove();
                else effects.onEvent(event);
            }
            if (pd.getAbilityKit() != null && !event.isCancelled()) pd.getAbilityKit().onEvent(event);
        }
    }

    @EventHandler
    public void onEntityHeal(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        double ret = 0;
        for (ItemStack is: player.getInventory().getArmorContents()) {
            if(is != null && is.getType() != Material.AIR){
                ret += ArmorAccessories.parseCustom(is).getCustomAttr().getOrDefault(Attributes.CustomAttributes.HEALING, 0);
            }
        }
        event.setAmount(event.getAmount()*(1+ret/100));

        int heal = (int) (Math.min(event.getAmount(), player.getMaxHealth() - player.getHealth()));
        String health = heal % 2 == 1 ? ChatColor.BLACK + "❤" + ChatColor.GREEN + "✔" : "✔";
        for (int i = 0; i < heal / 2; i++) {
            health = "❤" + health;
        }
        ArmorStand armorstand = (ArmorStand) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.ARMOR_STAND);
        armorstand.setVisible(false);
        armorstand.setCustomName(ChatColor.BOLD + "" + ChatColor.GREEN + "+" + health);
        armorstand.setGravity(true);
        armorstand.setVelocity(new Vector(Math.random() * 0.4 - 0.2, 0, Math.random() * 0.4 - 0.2).add(player.getVelocity()));
        armorstand.setSmall(true);
        armorstand.setMarker(true);
        armorstand.setCustomNameVisible(true);
        setTicksLived(armorstand, 20);
    }

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemCraft(CraftItemEvent event) {
        event.getWhoClicked().sendMessage(tcm(config.getString("CantCraft")));
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!event.getPlayer().hasPlayedBefore()){
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENDERDRAGON_GROWL, 3, 3);
        }
        Main.getPlugin().getPDManager().registerPlayer(Main.getPlugin(), event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Main.getPlugin().getPDManager().unregisterPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerSprint(PlayerToggleSprintEvent event) {
        PlayerData pd = Main.getPlugin().getPDManager().getData(event.getPlayer().getUniqueId());
        Iterator<DurationEffect> iterator = pd.getEffectIterator();
        while (iterator.hasNext()) {
            DurationEffect effects = iterator.next();
            if (effects.isExpired()) iterator.remove();
            else effects.onEvent(event);
        }
        if (pd.getAbilityKit() == null) {
            return;
        }
        pd.getAbilityKit().onEvent(event);

    }

    @EventHandler
    public void onAbilityCast(AbilityCastEvent event) {
        PlayerData pd = Main.getPlugin().getPDManager().getData(event.getPlayer().getUniqueId());
        Iterator<DurationEffect> iterator = pd.getEffectIterator();
        while (iterator.hasNext()) {
            DurationEffect effects = iterator.next();
            if (effects.isExpired()) iterator.remove();
            else effects.onEvent(event);
            if (event.isCancelled()) return;
        }

    }

    @EventHandler
    void onProjectileLaunch(final ProjectileLaunchEvent event) {
        if (event.getEntityType() == EntityType.SPLASH_POTION) {
            final Projectile projectile = event.getEntity();

            if (projectile.getShooter() instanceof Player && ((Player) projectile.getShooter()).isSprinting()) {
                final Vector velocity = projectile.getVelocity();

                velocity.setY(velocity.getY() - speed);
                projectile.setVelocity(velocity);
            }
        }
    }

    @EventHandler
    void onPotionSplash(final PotionSplashEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            final Player shooter = (Player) event.getEntity().getShooter();

            if (shooter.isSprinting() && event.getIntensity(shooter) > 0.5D) {
                event.setIntensity(shooter, 1.0D);
            }
        }
    }
    @EventHandler
    void onPotionSplash(final PlayerItemDamageEvent event) {
        if (ArmorType.matchType(event.getItem()) == null) return;
        ArmorAccessories aa = ArmorAccessories.parseCustom(event.getItem());
        double value = aa.getCustomAttr().getOrDefault(Attributes.CustomAttributes.DURABILITY, 0)/100d;
        value = event.getDamage()/Math.max(0.01d, 1d+value);
        event.setDamage((int)value);
        value = value-(int)value;
        if(Math.random() < value){
            event.setDamage(event.getDamage() + 1);
        }
    }

    @EventHandler
    public void onArrowPickup(PlayerPickupItemEvent event) {
        if (event.getItem().getType() == EntityType.ARROW) {
            event.setCancelled(true);
        }
    }

    // ==================<Helper Methods>==================

    public boolean itemIsKitOrAbility(ItemStack itemStack) {
        NBTItem nbti = new NBTItem(itemStack);
        return nbti.hasKey(Armor.NBTIArmor) || nbti.hasKey(Abilities.NBTIAbility) || nbti.hasKey(Weapons.NBTIWeapon);
    }

    public boolean isAbility(ItemStack is) {
        return new NBTItem(is).hasKey(Abilities.NBTIAbility);
    }

    public void setTicksLived(Entity entity, long ticks) {
        new BukkitRunnable() {
            @Override
            public void run() {
                entity.remove();
            }
        }.runTaskLater(Main.getPlugin(), ticks);
    }

    public void setDamageIndicator(LivingEntity entity, String heart, Vector initialVelocity, boolean isCritical) {
        new BukkitRunnable() {
            String hearts = heart;

            @Override
            public void run() {
                ArmorStand armorstand = (ArmorStand) entity.getWorld().spawnEntity(entity.getEyeLocation(), EntityType.ARMOR_STAND);
                armorstand.setVisible(false);
                armorstand.setGravity(true);
                armorstand.setVelocity(new Vector(Math.random() * 0.4 - 0.2, 0, Math.random() * 0.4 - 0.2).add(entity.getVelocity()));
                armorstand.setSmall(true);
                armorstand.setMarker(true);
                if (entity.getVelocity().distance(initialVelocity) > 1) {
                    hearts = "-" + ChatColor.UNDERLINE + hearts;
                } else {
                    hearts = "-" + hearts;
                }
                if (isCritical) {
                    hearts = ChatColor.GOLD + hearts + ChatColor.GOLD + "⚔";
                } else {
                    hearts = ChatColor.RED + hearts;
                }
                armorstand.setCustomNameVisible(true);

                armorstand.setCustomName(hearts);
                setTicksLived(armorstand, 19);
            }
        }.runTaskLater(Main.getPlugin(), 1);
    }

    final String NBTIStacks = "nbtiStacks";

    public void upgradeArmor(Player player) {
        ItemStack is = player.getInventory().getArmorContents()[Base.randInt(4)];

        if (is == null || is.getType() == Material.AIR) return;
        NBTItem nbti = new NBTItem(is);
        if (!nbti.hasKey(Armor.NBTIArmor)) return;
        int stacks = nbti.getInteger(NBTIStacks);
        nbti.setInteger(NBTIStacks, (stacks = stacks + 1));
        ItemMeta im = nbti.getItem().getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(config.getTCM("LoreStacks", stacks, 100));
        im.setLore(lore);
        is.setItemMeta(im);
        if (stacks % 4 == 0) {
            upgradeArmor(is);
            player.sendMessage(tcm(config.getString("GainStack"), is.getItemMeta().getDisplayName()) + " and upgraded");
        } else {
            player.sendMessage(tcm(config.getString("GainStack"), is.getItemMeta().getDisplayName()));
        }
    }

    // ===================Effect Methods========================
    public void upgradeArmor(ItemStack is) {
        int envProt = is.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
        if (envProt < 4) {
            is.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, envProt + 1);
            return;
        }
        int projProt = is.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
        if (projProt < 4) {
            is.addEnchantment(Enchantment.PROTECTION_PROJECTILE, projProt + 1);
            return;
        }
        int thorns = is.getEnchantmentLevel(Enchantment.THORNS);
        if (thorns < 3) {
            is.addEnchantment(Enchantment.THORNS, thorns + 1);
            return;
        }
        int blastProt = is.getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
        if (blastProt < 4) {
            is.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, blastProt + 1);
            return;
        }
        int featherFall = is.getEnchantmentLevel(Enchantment.PROTECTION_FALL);
        if (featherFall < 4 && is.getType().toString().contains("_BOOTS")) {
            is.addEnchantment(Enchantment.PROTECTION_FALL, featherFall + 1);
            return;
        }
        int fireProt = is.getEnchantmentLevel(Enchantment.PROTECTION_FIRE);
        if (fireProt < 4) {
            is.addEnchantment(Enchantment.PROTECTION_FIRE, fireProt + 1);
            return;
        }
        int unbreaking = is.getEnchantmentLevel(Enchantment.DURABILITY);
        if (unbreaking < 5) {
            is.addUnsafeEnchantment(Enchantment.DURABILITY, unbreaking + 1);
            return;
        }
        if (!is.getItemMeta().spigot().isUnbreakable()) {
            ItemMeta im = is.getItemMeta();
            im.spigot().setUnbreakable(true);
            is.setItemMeta(im);
            is.setDurability((short) 0);
            return;
        }
        NBTItem nbti = new NBTItem(is);
        ArmorTags at = new ArmorTags(nbti);
        at.applyTag(Attributes.MCAttributes.MAXHEALTH, ArmorTags.Operations.ADD, 1);
        is.setItemMeta(at.getItem().getItemMeta());
    }
}
