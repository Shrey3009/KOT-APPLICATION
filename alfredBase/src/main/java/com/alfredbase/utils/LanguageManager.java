package com.alfredbase.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alfredbase.R;

import java.util.Locale;

public class LanguageManager {
    public static final String LANGUAGE_KEY_ENGLISH = "en";
    public static final String LANGUAGE_KEY_CHINESE = "zh";
    public static final String LANGUAGE_KEY_INDONESIA = "in";
    public static final String LANGUAGE_KEY_TAGALOG = "tl";
    public static final String LANGUAGE_KEY_ARABIC = "ar";
    public static final String LANGUAGE_KEY_THAILAND = "th";
    public static final String LANGUAGE_KEY_VIETNAM = "vi";

    private static final String LANGUAGE_KEY = "language_key";


    /**
     * set current pref locale
     *
     * @param mContext
     * @return
     */
    public static Context setLocale(Context mContext) {
        return updateResources(mContext, getLanguagePref(mContext));
    }

    /**
     * Set new Locale with context
     *
     * @param mContext
     * @param mLocaleKey
     * @return
     */
    public static Context setNewLocale(Context mContext, String mLocaleKey) {
        setLanguagePref(mContext, mLocaleKey);
        return updateResources(mContext, mLocaleKey);
    }

    /**
     * Get saved Locale from SharedPreferences
     *
     * @param mContext current context
     * @return current locale key by default return english locale
     */
    public static String getLanguagePref(Context mContext) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return mPreferences.getString(LANGUAGE_KEY, LANGUAGE_KEY_ENGLISH);
    }

    public static Drawable getLanguageFlag(Context mContext) {
        String str = getLanguagePref(mContext);
        int flag = R.drawable.ic_flag_uk;
        if (!TextUtils.isEmpty(str)) {
            if (str.equals(LANGUAGE_KEY_ENGLISH)) {
                flag = R.drawable.ic_flag_uk;
            } else if (str.equals(LANGUAGE_KEY_CHINESE)) {
                flag = R.drawable.ic_flag_china;
            } else if (str.equals(LANGUAGE_KEY_INDONESIA)) {
                flag = R.drawable.ic_flag_indonesia;
            } else if (str.equals(LANGUAGE_KEY_TAGALOG)) {
                flag = R.drawable.ic_flag_philippines;
            } else if (str.equals(LANGUAGE_KEY_ARABIC)) {
                flag = R.drawable.ic_flag_arabia;
            } else if (str.equals(LANGUAGE_KEY_THAILAND)) {
                flag = R.drawable.ic_flag_thailand;
            } else if (str.equals(LANGUAGE_KEY_VIETNAM)) {
                flag = R.drawable.ic_flag_vietnam;
            }
        }

        return mContext.getResources().getDrawable(flag);
    }

    public static String getLanguageName(Context mContext) {
        String str = getLanguagePref(mContext);
        if (TextUtils.isEmpty(str)) {
            return mContext.getString(R.string.language_en);
        } else {
            if (str.equals(LANGUAGE_KEY_ENGLISH)) {
                return mContext.getString(R.string.language_en);
            } else if (str.equals(LANGUAGE_KEY_CHINESE)) {
                return mContext.getString(R.string.language_ch);
            } else if (str.equals(LANGUAGE_KEY_INDONESIA)) {
                return mContext.getString(R.string.language_in);
            } else if (str.equals(LANGUAGE_KEY_TAGALOG)) {
                return mContext.getString(R.string.language_ph);
            } else if (str.equals(LANGUAGE_KEY_ARABIC)) {
                return mContext.getString(R.string.language_ar);
            } else if (str.equals(LANGUAGE_KEY_THAILAND)) {
                return mContext.getString(R.string.language_th);
            } else if (str.equals(LANGUAGE_KEY_VIETNAM)) {
                return mContext.getString(R.string.language_vi);
            } else {
                return mContext.getString(R.string.language_en);
            }
        }
    }

    /**
     * set pref key
     *
     * @param mContext
     * @param localeKey
     */
    private static void setLanguagePref(Context mContext, String localeKey) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mPreferences.edit().putString(LANGUAGE_KEY, localeKey).commit();
    }

    /**
     * update resource
     *
     * @param context
     * @param language
     * @return
     */
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }


    public static AlertDialog alertLanguage(Context context, LanguageDialogListener languageDialogListener) {
        AlertDialog.Builder dialogLanguage = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_select_language, null);
        dialogLanguage.setView(dialogView);
        dialogLanguage.setCancelable(true);
        final AlertDialog alertLanguage = dialogLanguage.show();

        ImageView ivClose = (ImageView) dialogView.findViewById(R.id.iv_close);
        LinearLayout llEng = (LinearLayout) dialogView.findViewById(R.id.ll_language_english);
        LinearLayout llCh = (LinearLayout) dialogView.findViewById(R.id.ll_language_china);
        LinearLayout llIna = (LinearLayout) dialogView.findViewById(R.id.ll_language_ina);
        LinearLayout llPh = (LinearLayout) dialogView.findViewById(R.id.ll_language_ph);
        LinearLayout llAr = (LinearLayout) dialogView.findViewById(R.id.ll_language_ar);
        LinearLayout llTh = (LinearLayout) dialogView.findViewById(R.id.ll_language_th);
        LinearLayout llVi = (LinearLayout) dialogView.findViewById(R.id.ll_language_vi);

        setLanguage(llEng, LanguageManager.LANGUAGE_KEY_ENGLISH, languageDialogListener);
        setLanguage(llCh, LanguageManager.LANGUAGE_KEY_CHINESE, languageDialogListener);
        setLanguage(llIna, LanguageManager.LANGUAGE_KEY_INDONESIA, languageDialogListener);
        setLanguage(llPh, LanguageManager.LANGUAGE_KEY_TAGALOG, languageDialogListener);
        setLanguage(llAr, LanguageManager.LANGUAGE_KEY_ARABIC, languageDialogListener);
        setLanguage(llTh, LanguageManager.LANGUAGE_KEY_THAILAND, languageDialogListener);
        setLanguage(llVi, LanguageManager.LANGUAGE_KEY_VIETNAM, languageDialogListener);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertLanguage.dismiss();
            }
        });
        return alertLanguage;
    }

    public interface LanguageDialogListener {
        void setLanguage(final String language);
    }

    public static void setLanguage(final View view, final String language, final LanguageDialogListener languageDialogListener) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageDialogListener.setLanguage(language);
            }
        });
    }

}
