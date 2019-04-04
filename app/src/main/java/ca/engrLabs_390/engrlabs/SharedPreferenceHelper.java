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

    public void clearPreferences(Context context){
        sharedPreferences.edit().clear().apply();
    }
}