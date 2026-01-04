package com.ozop.impI;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import cozy.AdE;
import o1.d0;

/**
 * Date：2025/9/25
 * Describe:
 * com.ozop.impI.Core
 */
public class Core {

//    public static long insAppTime = 0L; //installAppTime
    private static SharedPreferences mmkv;
    public static Application mApp;

    public static void init(Context ctx) {
        mApp = (Application) ctx;
        mmkv = ctx.getSharedPreferences("cozy_core", 0);
        pE("test_d_load");
//        inIf(mApp);
        AdE.a2();
    }

    public static void pE(String string, String value) {
        d0.b1(string, value);
    }

    public static void pE(String string) {
        pE(string, "");
    }

    public static void postAd(String string) {
//        e.c(string);
        pE("ape", string);
    }


    public static String getStr(String key) {
        return mmkv.getString(key, "");
    }

    public static void saveC(String ke, String con) {
        mmkv.edit().putString(ke, con).apply();
    }

    public static int getInt(String key) {
        return mmkv.getInt(key, 0);
    }

    public static void saveInt(String key, int i) {
        mmkv.edit().putInt(key, i).apply();
    }

//    private static void inIf(Context context) {
//        try {
//            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//            insAppTime = pi.firstInstallTime;
//        } catch (Exception ignored) {
//        }
//    }
}
