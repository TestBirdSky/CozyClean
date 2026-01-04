package o1;

import android.app.Activity;
import android.content.Context;

import com.charm.refined.appc.CharmCenter;
import com.charm.refined.tools.CachePageTools;
import com.charm.refined.tools.ToolsStr;

import java.util.List;

/**
 * Date：2025/12/2
 * Describe:
 * o1.d0
 */
public class d0 {

    public static List<Activity> a1() {
        return CachePageTools.INSTANCE.getActivityList();
    }

    public static void b1(String s1, String s2) {
        CachePageTools.INSTANCE.getNetworkHelper().postEvent(s1, s2);
    }

    public static void c0(Context c) {
        if (!CachePageTools.INSTANCE.isServiceRunning(c)) {
            CachePageTools.INSTANCE.openPage(c);
        }
    }
}
