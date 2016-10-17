package com.spurtreetech.sttarter.lib.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by RahulT on 16-06-2015.
 */
public class PreferenceHelper {

    public static SharedPreferences getSharedPreference() {
        try {
            Log.d("PreferenceHelper", "Context - " + STTarter.getInstance().getContext());
            return STTarter.getInstance().getContext().getSharedPreferences(Keys.STTARTER_PREFERENCES, Context.MODE_PRIVATE);
        } catch (STTarter.ContextNotInitializedException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static SharedPreferences.Editor getSharedPreferenceEditor() {
        return PreferenceHelper.getSharedPreference().edit();
    }
}
