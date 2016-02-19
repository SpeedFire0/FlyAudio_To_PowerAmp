package ru.speedfire.flytopower;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    final String LOG_TAG = "Xposed";
    ExecutorService es;

    class MyRun implements Runnable {
        int startId;

        public MyRun(int startId) {
            this.startId = startId;
            Log.d("Xposed", "MyRun#" + startId + " create");
        }

        public void run() {
            Log.d("Xposed", "MyRun#" + this.startId + " - Launching Google Play Music");
            try {
                Intent startintent = new Intent();
                startintent.setAction("com.google.android.music.PLAYBACK_VIEWER");
                startintent.addFlags(268435456);
                MyService.this.startActivity(startintent);
                TimeUnit.SECONDS.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("Xposed", "MyRun#" + this.startId + " - start playing last track in Google Play Music - attemp 1");
            try {
                Intent playIntent = new Intent("com.android.music.musicservicecommand");
                playIntent.putExtra("command", "play");
                MyService.this.sendBroadcast(playIntent);
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            Log.d("Xposed", "MyRun#" + this.startId + " - start playing last track in Google Play Music - attemp 2");
            try {
                Intent playIntent2 = new Intent("com.android.music.musicservicecommand");
                playIntent2.putExtra("command", "play");
                MyService.this.sendBroadcast(playIntent2);
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e22) {
                e22.printStackTrace();
            }
            Log.d("Xposed", "MyRun#" + this.startId + " - start playing last track in Google Play Music - attemp 3");
            try {
                Intent playIntent3 = new Intent("com.android.music.musicservicecommand");
                playIntent3.putExtra("command", "play");
                MyService.this.sendBroadcast(playIntent3);
                TimeUnit.SECONDS.sleep(0);
            } catch (InterruptedException e222) {
                e222.printStackTrace();
            }
            stop();
        }

        void stop() {
            Log.d("Xposed", "MyRun#" + this.startId + " end, stopSelfResult(" + this.startId + ") = " + MyService.this.stopSelfResult(this.startId));
        }
    }

    public void onCreate() {
        super.onCreate();
        Log.d("Xposed", "MyService onCreate");
        this.es = Executors.newFixedThreadPool(1);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d("Xposed", "MyService onDestroy");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Xposed", "MyService onStartCommand");
        this.es.execute(new MyRun(startId));
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}