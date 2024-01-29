package net.waterraid.KitAbilities;

import com.Arhke.ArhkeLib.Lib.Base.CommandsBase;
import com.Arhke.ArhkeLib.Lib.Base.PluginBase;
import com.Arhke.ArhkeLib.Lib.FileIO.ConfigManager;
import com.Arhke.ArhkeLib.Lib.FileIO.FileManager;
import com.Arhke.ArhkeLib.Lib.Hook.Plugins;
import net.waterraid.KitAbilities.Commands.*;
import net.waterraid.KitAbilities.Managers.ItemManager;
import net.waterraid.KitAbilities.Managers.PlayerDataManager;
import net.waterraid.KitAbilities.Managers.WeaponManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.nio.file.Paths;

public class Main extends PluginBase {
    /**
     * packplayoutworldparticles type, x, y ,z, radiusX, radiusY, radiusZ, speed/data, amount
     * @param plugin The Main instance
     * @param player The Player that applied this effect
     * @param entity The Entity which this effect affects
     * @param times The Amount of times this is applied.
     * @param strength The Strength of the applied effect
     * @param interval The time interval if you need to manipulate that too (in ticks)
     */
    static Main _plugin;
    ItemManager _armorManager;
    PlayerDataManager pdManager;
    WeaponManager _weaponManager;

    private File _dataFile;

    String ArmorKey = "ArmorMap", PlayerDataKey = "DataMap", WeaponKey = "WeaponMap";
    @Override
    public void onEnable() {
        _plugin = this;

        registerConfigLoader(ConfigFiles.class);
        //Initialize File Paths
        _dataFile = Paths.get(getDataFolder().toString(), "data.yml").toFile();

        //initialize config
        FileManager data = new FileManager(_dataFile);


//        _armorManager = new ItemManager(new ConfigManager());
        registerGUI();
        pdManager = new PlayerDataManager(data, data.getDataManager().getDataManager(PlayerDataKey));
        _weaponManager = new WeaponManager(data, data.getDataManager().getDataManager(WeaponKey));
        CommandsBase setup = new SetupCommand("kit", getConfig(ConfigFiles.KitLang)),
                effects = new EffectsCommand("effects", getConfig(ConfigFiles.EffectLang)), pf = new PotionFillCommand("potionfill", getConfig(ConfigFiles.EffectLang)),
                weapon = new WeaponCommand("weapon", getConfig(ConfigFiles.EffectLang));
        AbilityCommand ability = new AbilityCommand(getConfig(ConfigFiles.AbilityLang));
        registerCommands(ability,setup);
        getCommand(effects.getCmd()).setExecutor(effects);
        getCommand(pf.getCmd()).setExecutor(pf);
        getCommand("pf").setExecutor(pf);
        getCommand(weapon.getCmd()).setExecutor(weapon);
        Bukkit.getPluginManager().registerEvents(new Listeners(getConfig(ConfigFiles.ListenerLang), pdManager), this);
        registerHooks(Plugins.PLACEHOLDERAPI, Plugins.VAULT);
        registerCustomEvents(8);
        getHook().registerPlaceholderAPI("ABILITY", (p, s)-> getPlugin().getPDManager().getData(p.getUniqueId()).getAbilityKit().getItemStack().getItemMeta().getDisplayName() + " " + ChatColor.GRAY);
        registerCustomAttributeEvents();

    }
    @Override
    public void onDisable() {
        pdManager.unregisterAll();
//        deleteDirectory(Paths.get(getDataFolder().getParentFile().toString(), "MythicMobs", "SavedData").toFile());
    }
    public ItemManager getArmorManager() {
        return _armorManager;
    }
    public PlayerDataManager getPDManager() {
        return pdManager;
    }
    public WeaponManager getWeaponManager() {
        return _weaponManager;
    }


    public static Main getPlugin(){
        return _plugin;
    }
}
