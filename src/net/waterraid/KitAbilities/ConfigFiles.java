package net.waterraid.KitAbilities;

import com.Arhke.ArhkeLib.Lib.Configs.ConfigFile;
import com.Arhke.ArhkeLib.Lib.FileIO.DataManager;
import com.Arhke.ArhkeLib.Lib.FileIO.FileManager;
import net.waterraid.KitAbilities.Abilities.Templates.Abilities;
import net.waterraid.KitAbilities.Abilities.Templates.EnumAbilities;

import java.util.Collections;
import java.util.List;

public enum ConfigFiles implements ConfigFile {
    AbilityLang("Lang", "Ability.yml"){
        @Override
        public void createConfigDefaults(FileManager fileManager) {

        }
    }, ArmorLang("Lang","Armor.yml") {
        @Override
        public void createConfigDefaults(FileManager fileManager) {

        }
    }, AbilityConfig("abilities.yml"){
        @Override
        public void createConfigDefaults(FileManager fileManager) {
            DataManager dm = fileManager.getDataManager();
            for(EnumAbilities ea:EnumAbilities.values()){
                DataManager ability = dm.getDataManager(ea.getKitClass().getSimpleName());
                ability.isOrDefault("&a&lDefault Name", Abilities.NameKey);
                ability.isOrDefault(Collections.singletonList("&a&lDefault Lore \n CoolDown {0}, CastTime {1}"), Abilities.LoreKey);
            }


        }
    };
    final String[] file;
    ConfigFiles(String... file){
        this.file = file;
    }
    @Override
    public String[] getFilePaths() {
        return this.file;
    }
}
