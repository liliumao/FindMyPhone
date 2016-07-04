package com.ufotosoft.findmyphone;


import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;


import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * Created by Li on 2016/7/2.
 */
public class MyApplication extends Application{
    public static final String APP_ID = "2882303761517486900";
    public static final String APP_KEY = "5781748662900";
    public static final String TAG = "findmyphone";
    public static String IMEI = null;

    @Override
    public void onCreate() {
        super.onCreate();
        shouldInit();
    }


    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
