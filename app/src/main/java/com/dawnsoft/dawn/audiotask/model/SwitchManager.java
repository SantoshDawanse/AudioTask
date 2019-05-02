package com.dawnsoft.dawn.audiotask.model;

import android.content.Context;
import android.content.SharedPreferences;

public class SwitchManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "MediaSwitch";
    private static final String KEY_IS_CHECKED = "isChecked";
    private static final String KEY_PROGRESS_VALUE = "progress_value";

    public SwitchManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void setChecked(boolean isChecked){
        editor.putBoolean(KEY_IS_CHECKED, isChecked);
        editor.commit();
    }

    public boolean isChecked(){
        return sharedPreferences.getBoolean(KEY_IS_CHECKED, false);
    }

    public void setProgressValue(int progressValue){
        editor.remove(KEY_PROGRESS_VALUE);
        editor.putInt(KEY_PROGRESS_VALUE, progressValue);
        editor.commit();
    }

    public int getProgressValue(){
        return sharedPreferences.getInt(KEY_PROGRESS_VALUE, 10000);
    }
}
