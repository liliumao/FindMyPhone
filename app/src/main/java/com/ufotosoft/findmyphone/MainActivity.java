package com.ufotosoft.findmyphone;

import com.google.android.gms.ads.InterstitialAd;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import android.*;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.*;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Remove the below line after defining your own ad unit ID.

    private static final int START_LEVEL = 1;
    private int mLevel;
    private Button mNextLevelButton;
    private TextView mLevelTextView;
    private static final int MY_PERMISSIONS_REQUEST_IMEI = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the text view to show the level number.
        mLevelTextView = (TextView) findViewById(R.id.level);
        mLevel = START_LEVEL;

        // Create the next level button, which tries to show an interstitial when clicked.
        mNextLevelButton = ((Button) findViewById(R.id.next_level_button));
        mNextLevelButton.setEnabled(true);
        mNextLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MessageReceiver.mp != null && MessageReceiver.mp.isPlaying()){
                    MessageReceiver.mp.stop();
                    MessageReceiver.mp = null;
                    AudioManager audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                    audiomanage.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
                }
            }
        });


        //初始化push推送服务

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_IMEI);
        }

        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        MyApplication.IMEI = tm.getDeviceId();

        Log.d("IMEI",MyApplication.IMEI);

        MiPushClient.setAlias(this, MyApplication.IMEI, null);
        MiPushClient.registerPush(this, MyApplication.APP_ID, MyApplication.APP_KEY);

        //打开Log
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(MyApplication.TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(MyApplication.TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_IMEI: {
                //如果请求被取消，那么 result 数组将为空
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    this.finish();
                }

                return;
            }
        }
    }

}
