package ru.speedfire.flytopower;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RunService extends Service {
    ExecutorService es;
    public static final String LOG_TAG = "Xposed";
    
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "RunService onCreate");
        es = Executors.newFixedThreadPool(2);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "RunService onDestroy");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "RunService onStartCommand");
        MyRun mr = new MyRun(startId);
        es.execute(mr);
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    class MyRun implements Runnable {

        int startId;
        String myAction;
        String myExtraName;
        String myExtraValue;

        public MyRun(int startId) {
            this.startId = startId;
            Log.d(LOG_TAG, ">>>>>>>>>>>>>>>>>> We are in RunService now >>>>>>>>>>>>>>> MyRun#" + startId + " create");
        }

        public void run() {
/*            try {
                //Запускаем Google Play Music
                Log.d(LOG_TAG, "RunService: Send Intent to start playing the track in Google Music");
                Intent playIntent = new Intent("com.android.music.musicservicecommand");
                playIntent.putExtra("command", "play");
                sendBroadcast(playIntent);
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                //Запускаем Google Play Music
                Log.d(LOG_TAG, "RunService >>> Send Intent to launch Google Music #1");
                Intent intent = new Intent();
                intent.setAction("com.google.android.music.PLAYBACK_VIEWER");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                //Запускаем Google Play Music
                Log.d(LOG_TAG, "RunService: Send Intent to start playing the track in Google Music");
                Intent playIntent = new Intent("com.android.music.musicservicecommand");
                playIntent.putExtra("command", "play");
                sendBroadcast(playIntent);
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/

            //Запускаем PowerAmp
            try {
                TimeUnit.SECONDS.sleep(0);
                Log.d(LOG_TAG, "Open PowerAmp window itself");
                Intent pwramp_start_intent = new Intent();
                pwramp_start_intent.setPackage("com.maxmpz.audioplayer");
                pwramp_start_intent.setClassName("com.maxmpz.audioplayer", "com.maxmpz.audioplayer.StartupActivity");
                pwramp_start_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                pwramp_start_intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(pwramp_start_intent);
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Log.d(LOG_TAG, "Send Intent to start PowerAmp service with Resume (play) function");
                Intent pwramp_intent = new Intent("com.maxmpz.audioplayer.API_COMMAND");
                pwramp_intent.setPackage("com.maxmpz.audioplayer");
                pwramp_intent.putExtra("cmd", 3);
                startService(pwramp_intent);
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Log.d(LOG_TAG, "Open PowerAmp window itself");
                Intent pwramp_start_intent = new Intent();
                pwramp_start_intent.setPackage("com.maxmpz.audioplayer");
                pwramp_start_intent.setClassName("com.maxmpz.audioplayer", "com.maxmpz.audioplayer.StartupActivity");
                pwramp_start_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                pwramp_start_intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(pwramp_start_intent);
                TimeUnit.MILLISECONDS.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            stop();
        }

        void stop() {
            Log.d(LOG_TAG, "MyRun#" + startId + " end, stopSelfResult("
                    + startId + ") = " + stopSelfResult(startId));
        }
    }
}
