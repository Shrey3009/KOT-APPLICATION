package com.alfredbase.global;

import android.app.Application;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alfredbase.BuildConfig;
import com.alfredbase.store.Store;
import com.bugsee.library.Bugsee;

import java.util.HashMap;

/**
 * Created by Arif S. on 6/19/19
 */
public class BugseeHelper {
    private static boolean isActive;

    public static void init(Application application, String token) {
        isActive = Store.getBoolean(application, Store.BUGSEE_STATUS, true);
        if (!isActive) return;

        HashMap<String, Object> options = new HashMap<>();
        options.put(Bugsee.Option.VideoEnabled, true);//set true to recode video
        options.put(Bugsee.Option.MaxRecordingTime, 2 * 60 * 60);
        options.put(Bugsee.Option.ShakeToTrigger, false);

        if (BuildConfig.DEBUG) {
//            Bugsee.launch(application, "f4eb9760-2327-4253-9f5b-533ee0e711c6", options);
            Bugsee.launch(application, token, options);
        } else {
            Bugsee.launch(application, token, options);
        }

    }

    public static void setEmail(String email) {
        if (!isActive) return;
        Bugsee.setEmail(email);
    }

    public static void setAttribute(String key, Object value) {
        if (!isActive) return;
        Bugsee.setAttribute(key, value);
    }

    public static void trace(String key, Object value) {
        if (!isActive) return;
        Bugsee.setAttribute(key, value);
    }

    public static void buttonClicked(View v) {
        String text = String.valueOf(v.getId());

        try {
            if (v instanceof Button) {
                text = ((Button) v).getText().toString();
            } else if (v instanceof TextView) {
                text = ((TextView) v).getText().toString();
            } else if (v instanceof ImageButton) {
            } else if (v instanceof ImageView) {
            }
        } catch (Exception ex) {
            text = String.valueOf(v.getId());
        }

        if (v.getTag() != null)
            text += " -> " + v.getTag();

        buttonClicked(text);
    }

    public static void buttonClicked(String message) {
        event("BC -> " + message);
    }

    public static void event(String message) {
        if (!isActive) return;
        Bugsee.event(message);
    }

    public static void event(String key, HashMap<String, Object> param) {
        if (!isActive) return;
        Bugsee.setAttribute(key, param);
    }

    public static void log(String message) {
        if (!isActive) return;
        Bugsee.log(message);
    }

    public static void reportBugsee() {
        try {
            throw new NullPointerException();
        } catch (NullPointerException ex) {

        }
    }

}
