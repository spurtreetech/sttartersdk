package com.sttarter.init;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by RahulT on 16-06-2015.
 */
public class PreferenceHelper {

    public static SharedPreferences getSharedPreference() {
        try {
            Log.d("PreferenceHelper", "Context - " + STTarterManager.getInstance().getContext());
            return STTarterManager.getInstance().getContext().getSharedPreferences(STTKeys.STTARTER_PREFERENCES, Context.MODE_PRIVATE);
        } catch (STTarterManager.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static SharedPreferences.Editor getSharedPreferenceEditor() {
        return PreferenceHelper.getSharedPreference().edit();
    }
}
