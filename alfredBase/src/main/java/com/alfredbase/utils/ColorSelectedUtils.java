package com.alfredbase.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by Zun on 2017/2/17 0017.
 */

public class ColorSelectedUtils {

    /** 对TextView设置不同状态时其文字颜色 */
    public static ColorStateList createColorStateList(int normal, int pressed) {
        int[] colors = new int[] { pressed, pressed, normal, pressed, normal, normal };
        int[][] states = new int[6][];
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
        states[2] = new int[] { android.R.attr.state_enabled };
        states[3] = new int[] { android.R.attr.state_focused };
        states[4] = new int[] { android.R.attr.state_window_focused };
        states[5] = new int[] {};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /** 设置Selector */
    public static StateListDrawable newSelector(int normalColor, int pressedColor) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = new ColorDrawable(normalColor);
        Drawable pressed = new ColorDrawable(pressedColor);
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);
        // View.ENABLED_STATE_SET
        bg.addState(new int[] { android.R.attr.state_enabled }, normal);
        // View.EMPTY_STATE_SET
        bg.addState(new int[] {}, normal);
        return bg;
    }


}
