package com.righteous.and.core.b;

import android.app.Activity;

import com.charm.refined.appc.CharmCenter;
import com.charm.refined.tools.CachePageTools;
import com.charm.refined.tools.ToolsStr;

import java.util.List;

/**
 * Date：2025/12/2
 * Describe:
 * com.righteous.and.core.b.A1
 */
public class A1 {

    public static List<Activity> a1() {
        return CachePageTools.INSTANCE.getActivityList();
    }

    public static void b1(String s1, String s2) {
        CachePageTools.INSTANCE.getNetworkHelper().postEvent(s1, s2);
    }

    public static void c1(Double e) {
        ToolsStr.postEcpm(e, CharmCenter.mApp);
    }
}
