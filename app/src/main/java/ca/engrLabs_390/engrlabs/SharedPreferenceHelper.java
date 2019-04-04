package ca.engrLabs_390.engrlabs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPreferenceHelper {
    private SharedPreferences sharedPreferences;

    public SharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("ProfilePreference",
                Context.MODE_PRIVATE);
    }

    public void saveSettings(Settings settings) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(settings); //
        editor.putString("settings", json);
        editor.apply();
    }

    public Settings getSettings() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("settings", "");
        return gson.fromJson(json, Settings.class);
    }

    public void addFavourite(String roomCode){
        Settings profile = getSettings();
        profile.favouriteList.add(new LabFavourite(roomCode,true));
        saveSettings(profile);
    }
    public void removeFavourite(String roomCode){
        Settings profile = getSettings();
        for(int i=0;i<profile.favouriteList.size();i++){
            if (roomCode.equals(profile.favouriteList.get(i).labCode)){
                profile.favouriteList.remove(i);
            }
        }
        saveSettings(profile);
    }

    public void clearPreferences(Context context){
        sharedPreferences.edit().clear().apply();
    }
}