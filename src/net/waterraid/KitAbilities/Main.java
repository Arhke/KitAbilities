package net.waterraid.KitAbilities;

import com.Arhke.ArhkeLib.Lib.Base.PluginBase;
import com.Arhke.ArhkeLib.Lib.Hook.Plugins;
import net.waterraid.KitAbilities.Commands.*;
import net.waterraid.KitAbilities.FileIO.DataManager;
import net.waterraid.KitAbilities.FileIO.FileManager;
import net.waterraid.KitAbilities.Managers.ArmorManager;
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
    ArmorManager _armorManager;
    PlayerDataManager _pdManager;
    WeaponManager _weaponManager;

    private File _configFile, _dataFile;
    DataManager _config;

    String ArmorKey = "ArmorMap", PlayerDataKey = "DataMap", WeaponKey = "WeaponMap";
    public static Listeners listen;
    @Override
    public void onEnable() {
        _plugin = this;

        registerConfigLoader(ConfigFiles.class);
        //Initialize File Paths
        _configFile = Paths.get(getDataFolder().toString(), "config.yml").toFile();
        _dataFile = Paths.get(getDataFolder().toString(), "data.yml").toFile();

        //initialize config
        saveResource(_configFile.getName(), false);
        _config = new FileManager(_configFile).getDataManager();
        FileManager data = new FileManager(_dataFile);


        _armorManager = new ArmorManager(this, data, data.getDataManager().getDataManager(ArmorKey));
        _pdManager = new PlayerDataManager(this, data, data.getDataManager().getDataManager(PlayerDataKey));
        _weaponManager = new WeaponManager(this, data, data.getDataManager().getDataManager(WeaponKey));
        CommandsBase armor = new ArmorCommand(this),
                effects = new EffectsCommand(this), pf = new PotionFillCommand(this), weapon = new WeaponCommand(this);
        getCommand(armor.getCmd()).setExecutor(armor);
        AbilityCommand ability = new AbilityCommand(this, getConfig(ConfigFiles.AbilityLang));
        getConfig(ConfigFiles.AbilityLang).getFM().save();
        registerCommands(ability);
        getCommand(effects.getCmd()).setExecutor(effects);
        getCommand(pf.getCmd()).setExecutor(pf);
        getCommand("pf").setExecutor(pf);
        getCommand(weapon.getCmd()).setExecutor(weapon);
        Bukkit.getPluginManager().registerEvents(listen = new Listeners(this), this);
        registerHooks(Plugins.PLACEHOLDERAPI, Plugins.VAULT);
        registerCustomEvents();
        getHook().registerPlaceholderAPI("ABILITY", (p, s)-> getPlugin().getPDManager().getData(p.getUniqueId()).getAbilityKit().getItemStack().getItemMeta().getDisplayName() + " " + ChatColor.GRAY);

    }
    @Override
    public void onDisable() {
        _pdManager.unregisterAll();
        deleteDirectory(Paths.get(getDataFolder().getParentFile().toString(), "MythicMobs", "SavedData").toFile());
    }
    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
    public ArmorManager getArmorManager() {
        return _armorManager;
    }
    public PlayerDataManager getPDManager() {
        return _pdManager;
    }
    public WeaponManager getWeaponManager() {
        return _weaponManager;
    }

    public DataManager getConfigManager() {
        return _config;
    }

    //todo
    public static Main getPlugin(){
        return _plugin;
    }
}
