package ru.speedfire.flytopower;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Main implements IXposedHookLoadPackage {
    public static final String LOG_TAG = "Xposed";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("cn.flyaudio.media"))
            return;

        Log.d(LOG_TAG, "We are now inside the original app - FlyAudio Media >>>>>>>>>>>>>>>>>>>>>>");

        // =========================================================================================
        // ===== OPTION 1: Hook before/after the original method (e.g. "onCreate") is executed =====
        // ============================================================================
        findAndHookMethod(XposedHelpers.findClass("cn.flyaudio.media.view.activity.MusicPlaybackActivity", lpparam.classLoader), "onCreate", Bundle.class, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.d(LOG_TAG, "We are inside onCreate method");

//                Context context = (Context) param.thisObject;
                Activity activity = (Activity) param.thisObject;

                //This is to launch Google Music:
 /*               Log.d(LOG_TAG, "Send Intent to launch Google Music");
                Intent intent = new Intent();
                intent.setAction("com.google.android.music.PLAYBACK_VIEWER");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);

                Log.d(LOG_TAG, "Send Intent to start playing the track in Google Music");
                Intent playIntent = new Intent("com.android.music.musicservicecommand");
                playIntent.putExtra("command", "play");
                activity.sendBroadcast(playIntent);
*/
                //PowerAmp
                Log.d(LOG_TAG, "Send Intent to start PowerAmp service with Resume (play) function");
                Intent pwramp_intent = new Intent("com.maxmpz.audioplayer.API_COMMAND");
                pwramp_intent.setPackage("com.maxmpz.audioplayer");
                pwramp_intent.putExtra("cmd", 3);
                activity.startService(pwramp_intent);

                Log.d(LOG_TAG, "Open PowerAmp window itself");
                Intent pwramp_start_intent = new Intent();
                pwramp_start_intent.setPackage("com.maxmpz.audioplayer");
                pwramp_start_intent.setClassName("com.maxmpz.audioplayer", "com.maxmpz.audioplayer.StartupActivity");
                activity.startActivity(pwramp_start_intent);

                Log.d(LOG_TAG, "Intent is successfully sent !!!!");

                Log.d(LOG_TAG, "Try to close the original app (FlyAudio player) - prevent from running >>>>>>");
//                activity.finish();
                // Instead of activity.finish() you can use param.setResult(null) to prevent original app from running, but in my case the app was closed with error.
//                param.setResult(null);
                try {
                    param.setResult(null);
                } catch (Throwable t) {
                    param.setThrowable(t);
                }

            }

//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//            }

        });


        // ========================================================================
        // ===== OPTION 2: REPLACE method (e.g. "onCreate") with our own code =====
        // ========================================================================
 /*        findAndHookMethod(XposedHelpers.findClass("cn.flyaudio.media.view.activity.MusicPlaybackActivity", lpparam.classLoader), "onCreate", Bundle.class, new XC_MethodReplacement() {
             @Override
             protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {

                Activity activity = (Activity) param.thisObject;
                 //PowerAmp
                 Log.d(LOG_TAG, "Send Intent to start PowerAmp service with Resume (play) function");
                 Intent pwramp_intent = new Intent("com.maxmpz.audioplayer.API_COMMAND");
                pwramp_intent.setPackage("com.maxmpz.audioplayer");
                pwramp_intent.putExtra("cmd", 3);
                 activity.startService(pwramp_intent);

                 Log.d(LOG_TAG, "Open PowerAmp window itself");
                 Intent pwramp_start_intent = new Intent();
                 pwramp_start_intent.setPackage("com.maxmpz.audioplayer");
                pwramp_start_intent.setClassName("com.maxmpz.audioplayer", "com.maxmpz.audioplayer.StartupActivity");
                activity.startActivity(pwramp_start_intent);

                Log.d(LOG_TAG, "Intent is successfully sent !!!!");
                Log.d(LOG_TAG, "Try to close the original app (FlyAudio player) - prevent from running >>>>>>");
                 try {
                     activity.finish();
                 } catch (Throwable t) {
                     param.setThrowable(t);
                 }
                return null;
            }


        });

*/


        // =================================================================================
        // ===== OPTION 3: Option 1 or 2 + running new app (PowerAmp) from OUR SERVICE =====
        // =================================================================================
 /*        findAndHookMethod(XposedHelpers.findClass("cn.flyaudio.media.view.activity.MusicPlaybackActivity", lpparam.classLoader), "onCreate", Bundle.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {

                Activity activity = (Activity) param.thisObject;
                //Launching our MyService which will further send all intents independently

                Log.d(LOG_TAG, "Send Intent to MyService");
                Intent myservice_intent = new Intent("ru.speedfire.flytopower.MyService");
                activity.startService(myservice_intent);

                //Closing initial app:
                activity.finish();
                return null;
            }
        });
*/
    }

}
