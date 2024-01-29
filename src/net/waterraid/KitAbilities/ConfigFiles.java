package net.waterraid.KitAbilities;

import com.Arhke.ArhkeLib.Lib.Base.Base;
import com.Arhke.ArhkeLib.Lib.Configs.ConfigFile;
import com.Arhke.ArhkeLib.Lib.FileIO.DataManager;
import com.Arhke.ArhkeLib.Lib.FileIO.FileManager;
import net.waterraid.KitAbilities.Abilities.Templates.Abilities;
import net.waterraid.KitAbilities.Abilities.Templates.EnumAbilities;

import java.util.Collections;

public enum ConfigFiles implements ConfigFile {
    AbilityLang("Lang", "Ability.yml"){
        @Override
        public void createConfigDefaults(FileManager fileManager) {

        }
    }, ArmorLang("Lang","Armor.yml") {
        @Override
        public void createConfigDefaults(FileManager fileManager) {

        }
    }, EffectLang("Lang","Effect.yml") {
        @Override
        public void createConfigDefaults(FileManager fileManager) {

        }
    }, KitLang("Lang","KitCommand.yml") {
        @Override
        public void createConfigDefaults(FileManager fileManager) {

        }
    }, AbilityConfig("abilities.yml"){
        @Override
        public void createConfigDefaults(FileManager fileManager) {
            DataManager dm = fileManager.getDataManager();
            for(EnumAbilities ea:EnumAbilities.values()){
                DataManager ability = dm.getDataManager(Base.capitalize(ea.name()));
                ability.isOrDefault("&a&lDefault Name", Abilities.NameKey);
                ability.isOrDefault(Collections.singletonList("&a&lDefault Lore \n CoolDown {0}, CastTime {1}"), Abilities.LoreKey);
            }


        }
    }, ListenerLang("Lang", "Listeners.yml"){
        @Override
        public void createConfigDefaults(FileManager fileManager) {
//            Listeners:
//            NoThrowAbility: "&cYou can't throw Ability Items out of your inventory"
//            CantUseCrafting: "&cNope, Can't Use that here, Sorry"
//            CantCraft: "&cWould Be Nice No? Too bad you can't"
//            GainStack: "&7Your {0}&r&7 gained a Stack"
//            ArmorUpgrade: "&bThat Item has been Upgraded"
//            MaxStacks: "&aMax stacks!, separate ability"
//            LoreMaxStacks: "&4&lMaxStacks"
//            LoreStacks: "&4{0} stacks"

        }
    };
    final String[] file;
    ConfigFiles(String... file){
        this.file = file;
    }
    @Override
    public String[] getFilePath() {
        return this.file;
    }
}
