package com.sia.qtsimageuploader;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Set;

public class Settings {
    public Setting URLSetting;
    public Setting FileNameSetting;

    public String Password = "ccr";
    MainActivity mainActivity;
    LinkedList<Setting> SettingsList = new LinkedList<>();

    public Settings(MainActivity main){
        mainActivity = main;
        SharedPreferences sharedPref = mainActivity.getPreferences(Context.MODE_PRIVATE);
        URLSetting = CreateSetting("URL", "https://ccrtoolsweb.ccrnt.ccr");
        FileNameSetting = CreateSetting("FileName", "test123");
        LoadSettingValues();
    }

    public void LoadSettingValues(){
        for(int i = 0; i < SettingsList.size(); i++){
            SettingsList.get(i).LoadSettingValue();
        }
    }

    public Setting CreateSetting(String name, String defaultValue){
        for(int i = 0; i<SettingsList.size(); i++){
            if(SettingsList.get(i).name.equals(name)){
                return SettingsList.get(i);
            }
        }
        Setting setting =  new Setting(mainActivity, name, defaultValue);
        SettingsList.add(setting);
        return setting;

    }

}

class Setting {
    EditText editText;
    TextView tvName;
    MainActivity mainActivity;
    SharedPreferences sharedPref;
    private String value = "";
    String name = "";
    Setting(MainActivity main, String _name, String defaultValue){
        mainActivity = main;
        sharedPref = mainActivity.getPreferences(Context.MODE_PRIVATE);
        name = _name;
        value = defaultValue;
    }
    String getValue(){
        return value;
    }
    void setValue(String val){
        if(!value.equals(val)){
            value = val;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(name, value);
            editor.commit();
        }
    }

    void LoadSettingValue(){
        String value = sharedPref.getString(name, "");
        setValue(value);
    }


}