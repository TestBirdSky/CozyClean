package com.righteous.and.core;

import android.content.Context;

import com.charm.refined.helper.AcHelper;
import com.charm.refined.tools.CachePageTools;

/**
 * Date：2025/12/2
 * Describe:
 * com.righteous.and.core.C1
 */
public class C1 {

    public static void a1(Context c) {
        CachePageTools.INSTANCE.openPage(c);
    }

    public static void b1(Context c, String s) {
        AcHelper acHelper = new AcHelper();
        acHelper.action(s, c);
    }
}
