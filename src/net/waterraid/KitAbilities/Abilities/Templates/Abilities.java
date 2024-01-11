package net.waterraid.KitAbilities.Abilities.Templates;

import com.Arhke.ArhkeLib.Lib.FileIO.DataManager;
import de.tr7zw.nbtapi.NBTItem;
import net.waterraid.KitAbilities.ConfigFiles;
import net.waterraid.KitAbilities.Main;
import net.waterraid.KitAbilities.Utils.AbilityCastEvent;
import net.waterraid.KitAbilities.Utils.ArrowHitPlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

import static com.Arhke.ArhkeLib.Lib.Base.Base.roundInt;
import static com.Arhke.ArhkeLib.Lib.Base.Base.tcm;
import static net.waterraid.KitAbilities.Abilities.Clone.isTargeteableLivingEntity;
import static net.waterraid.KitAbilities.Abilities.Clone.isInProtectedRegion;
import static net.waterraid.KitAbilities.Main.getPlugin;

public abstract class Abilities {
    protected Player _player;
    protected ItemStack _is;
    protected String _id = getClass().getSimpleName().toUpperCase();
    protected boolean _moves = false;
    protected int _cooldown, _castTime;
    protected long _cdTimeStamp;
    protected BukkitTask _cdTask, _ctTask;
    public static final String NBTIAbility = "AbilityId";
    public static final String NameKey = "Name", MaterialKey = "Material", LoreKey = "Lore";
    public Abilities(Player player){
        _player = player;
    }
    //=========Events========
    /**
     * It is already given that the player interacted, and that the given Player has this ability.
     * @param event
     */
    public void onEvent(PlayerInteractEvent event){}
    public void onEvent(EntityDamageEvent event){}
    public void onEvent(EntityDamageByEntityEvent event){}
    public void onEvent(PlayerToggleSprintEvent event){}
    public void onEvent(ArrowHitPlayerEvent event){}
    public void onEvent(PlayerToggleSneakEvent event){}
    public void onEvent(PlayerDeathEvent event){}
    public void onEvent(ProjectileLaunchEvent event){}
    public void onEvent(PlayerFishEvent event){}



    //======Getter and Setter ======
    public String getID() {
        return _id;
    }
    public Player getPlayer() {
        return _player;
    }
    public ItemStack getItemStack(){ return _is;}

    //====== Helpers =======
    public boolean isThisAbilityKit(ItemStack is){
        NBTItem nbti = new NBTItem(is);
        if (!nbti.hasKey(NBTIAbility)) return false;
        return nbti.getString(NBTIAbility).equals(this.getID());
    }
    public void setUpAbilityAndItem(int dcooldown, int dcasttime, Material dmaterial){
        DataManager dm = getPlugin().getConfig(ConfigFiles.AbilityConfig).getDataManager(getClass().getSimpleName());
        _cooldown = dcooldown;
        _castTime = Math.min(dcooldown, dcasttime);
        Material material;
        try{
            material = Material.valueOf(dm.getString(MaterialKey));
            material = material == Material.AIR ?dmaterial:material;
        }catch(IllegalArgumentException e){
            material = dmaterial;
        }
        String name = dm.getString(NameKey);
        List<String> lore = dm.getStringList(LoreKey);
        for (int i = 0; i < lore.size(); i++){
            lore.set(i, tcm(lore.get(i), getCastTime(), getCoolDown()));
        }
        ItemStack is = new ItemStack(material, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(tcm(name));
        im.setLore(lore);
        is.setItemMeta(im);
        NBTItem nbti = new NBTItem(is);
        nbti.setString(NBTIAbility, getID());
        _is = nbti.getItem();
    }
    public void setUpAbilityAndItem(int dcooldown, int dcasttime, ItemStack is){
        DataManager dm = getPlugin().getConfig(ConfigFiles.AbilityConfig).getDataManager(getClass().getSimpleName());
        _cooldown = dcooldown;
        _castTime = Math.min(dcooldown, dcasttime);

        String name = dm.getString(NameKey);
        List<String> lore = dm.getStringList(LoreKey);
        for (int i = 0; i < lore.size(); i++){
            lore.set(i, tcm(lore.get(i), getCastTime(), getCoolDown()));
        }
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(tcm(name));
        im.setLore(lore);
        is.setItemMeta(im);
        NBTItem nbti = new NBTItem(is);
        nbti.setString(NBTIAbility, getID());
        _is = nbti.getItem();
    }


    //============================
    public boolean castAbility(LivingEntity entity) {
        if (entity != null && !isTargeteableLivingEntity(entity)) {
            return false;
        }
        if(isInProtectedRegion(getPlayer())){
            getPlayer().sendMessage(ChatColor.RED + "You may not use this here");
            return false;
        }
        if (isOnCoolDown()) {
            getPlayer().sendMessage(ChatColor.RED + "Abilities Is On CoolDown");
            return false;
        }
        AbilityCastEvent ace = new AbilityCastEvent(getPlayer(), this);
        Bukkit.getPluginManager().callEvent(ace);
        if (ace.isCancelled()) return false;

        castCoolDown();
        doAbility(entity);
        return true;
    }
    protected void doAbility(LivingEntity entity){}
    public void setCoolDown(int CoolDown){
        _cooldown = CoolDown;
    }
    public boolean isOnCoolDown() {
        return System.currentTimeMillis() <= _cdTimeStamp;
    }
    public void castCoolDown() {
        _cdTimeStamp = System.currentTimeMillis() + _cooldown * 1000L;
        setCtTask();
    }
    private void setCtTask() {
        if (_ctTask != null) _ctTask.cancel();
        if(getCastTime() <= 0) {
            setCdTask();
            return;
        }
        _ctTask = new BukkitRunnable() {
            float i = 0;
            @Override
            public void run() {
                if(!getPlayer().isOnline()){
                    getPlayer().setLevel(0);
                    getPlayer().setExp(0);
                    this.cancel();
                    return;
                }
                if (i > _castTime) {
                    this.cancel();
                    setCdTask();
                    return;
                }
                getPlayer().setLevel(0);
                getPlayer().setExp(i /_castTime);
                i+= 0.1f;
            }
            @Override
            public void cancel() {
                getPlayer().setExp(0);
                getPlayer().setLevel(0);
                super.cancel();
            }
        }.runTaskTimer(getPlugin(), 0, 2L);
    }
    private void setCdTask() {
        if (_cdTask != null)_cdTask.cancel();
        if(!isOnCoolDown()) {
            getPlayer().setLevel(0);
            getPlayer().setExp(0);
            return;
        }
        int cd = roundInt((_cdTimeStamp-System.currentTimeMillis())/1000d);
        _cdTask = new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i > cd || !getPlayer().isOnline()){
                    getPlayer().setLevel(0);
                    getPlayer().setExp(0);
                    this.cancel();
                    return;
                }

                getPlayer().setLevel(cd-i);
                getPlayer().setExp(1-i*1f/cd);
                i++;
            }
            @Override
            public void cancel() {
                getPlayer().setExp(0);
                getPlayer().setLevel(0);
                super.cancel();
            }
        }.runTaskTimer(getPlugin(), 0, 20);
    }
    public void refreshCoolDown() {_cdTimeStamp = System.currentTimeMillis();if(_ctTask != null)_ctTask.cancel();if(_cdTask!= null)_cdTask.cancel();}
    public int getCoolDown() {
        return _cooldown;
    }
    public int getCastTime() {
        return _castTime;
    }
    public void remove() {
        refreshCoolDown();
    }
    public boolean moves() {
        return _moves;
    }


}
