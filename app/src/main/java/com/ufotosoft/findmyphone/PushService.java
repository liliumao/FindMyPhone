package com.ufotosoft.findmyphone;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * Created by Li on 2016/7/6.
 */
public class PushService extends Service {
    protected Integer NOTIFICATION_ID = 2333; // Some random integer
    private LoadNotification loadNotification;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;

        loadNotification = new LoadNotification("findMyPhone", "Keep it alive");
        loadNotification.notifyMessage();

        MiPushClient.setAlias(this, MyApplication.IMEI, null);
        MiPushClient.registerPush(this, MyApplication.APP_ID, MyApplication.APP_KEY);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        stopForeground(true);
        super.onDestroy();
        Intent sevice = new Intent(this, PushService.class);
        this.startService(sevice);
    }

    class LoadNotification {

        private String titleMessage;
        private String textMessage;


        public LoadNotification(String titleMessage, String textMessage) {
            this.titleMessage = titleMessage;
            this.textMessage = textMessage;
        }

        public void notifyMessage() {
            NotificationCompat.Builder builder = getNotificationBuilder(MainActivity.class);
            startForeground(NOTIFICATION_ID, builder.build());

        }

        protected NotificationCompat.Builder getNotificationBuilder(Class clazz) {
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

            builder.setSmallIcon(R.mipmap.ic_launcher);  // icon id of the image

            builder.setContentTitle(this.titleMessage)
                    .setContentText(this.textMessage)
                    .setContentInfo("JukeSpot");

            Intent foregroundIntent = new Intent(getApplicationContext(), clazz);

            foregroundIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, foregroundIntent, 0);

            builder.setContentIntent(contentIntent);
            return builder;
        }

    }
}
