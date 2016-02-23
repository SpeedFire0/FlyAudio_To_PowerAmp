package ru.speedfire.flytopower;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Main implements IXposedHookLoadPackage {
    public static final String LOG_TAG = "Xposed";
    private static Thread start_services;

    @Override
    @SuppressWarnings("serial")
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {


        if (!lpparam.packageName.equals("cn.flyaudio.media"))
            return;

        Log.d(LOG_TAG, "We are now inside the original app - FlyAudio Media >>>>>>>>>>>>>>>>>>>>>>");

        // Step 1 - Kill FlyMediaApplication
        XposedHelpers.findAndHookMethod(XposedHelpers.findClass("cn.flyaudio.media.FlyMediaApplication", lpparam.classLoader), "onCreate", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                try {
                    Log.d(LOG_TAG, "Replace FlyMediaApplication with Nothing >>>>>>>>>>>>");
//                     activity.finish();
                } catch (Throwable t) {
                    param.setThrowable(t);
                }
                return null;
            }
        });

        // Step 2b - Hook to MusicPlaybackActivity
        XposedHelpers.findAndHookMethod(XposedHelpers.findClass("cn.flyaudio.media.view.activity.MusicPlaybackActivity", lpparam.classLoader), "onCreate", Bundle.class, new XC_MethodHook() {

            @Override
            @SuppressWarnings("deprecation")
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.d(LOG_TAG, "We are inside << onCreate >> method");

                Activity activity = (Activity) param.thisObject;

                Log.d(LOG_TAG, "Send Intent to startService RunService");

                activity.startService(new Intent("ru.speedfire.flytopower.RunService"));

                Log.d(LOG_TAG, "Intent is successfully sent !!!! Back to Home >>>>> ");

                Intent mHomeIntent = new Intent("android.intent.action.MAIN");
                mHomeIntent.addCategory("android.intent.category.HOME");
                mHomeIntent.addFlags(270532608);
                mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(mHomeIntent);

                Log.d(LOG_TAG, ">>>>>>>>>>>>>>>>>>>>>>> QUIT APP >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                activity.finish(); // This is needed to prevent re-run of the app when tap Back button,
                // This is the most crucial command. System.exit(0) hard quits the app. It prevents any further error messages.
                // Usually when you try to prevent onCreate method from running it returns errors and app craches
                System.exit(0);
                Log.d(LOG_TAG, ">>>>>>>>>>>>>>>>>>>>>>> QUIT DONE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        });

        // Step 4a - Kill fixMyViewError() Method
/*            XposedBridge.hookAllConstructors(XposedHelpers.findClass("cn.flyaudio.media.view.activity.MusicPlaybackActivity", lpparam.classLoader), new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    Activity activity = (Activity) param.thisObject;
//                    Method method = (Method) param.thisObject;
                    Log.d(LOG_TAG, ">>>>>>>>>>>>>>>>>>> Kill hookAllConstructors >>>>>>>>>>>> - " + param);
                    Log.d(LOG_TAG, ">>>>>>>>>>>>>>>>>>> GetResult >>>>>>>>>>>> = " + param.getResult());
//                    Log.d(LOG_TAG, ">>>>>>>>>>>>>>>>>>> Activity >>>>>>>>>>>>> = " + activity);
//                    Log.d(LOG_TAG, ">>>>>>>>>>>>>>>>>>> Method >>>>>>>>>>>>>>> = " );
                    param.setResult(null);
                }
            });
*/

        // Step 4a - Kill fixMyViewError() Method
/*        try {
            findAndHookMethod(XposedHelpers.findClass("cn.flyaudio.media.view.activity.MusicPlaybackActivity", lpparam.classLoader), "fixMyViewError", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    try {
                        Log.d(LOG_TAG, ">>>>>>>>>>>>>>>>>>> Kill fixMyViewError() Method >>>>>>>>>>>> !!!!");
    //                     activity.finish();
                    } catch (Throwable t) {
                        param.setThrowable(t);
                    }
                    return null;
                }
            });
        } catch (Throwable t) {
        }

*/

        // Step 4b - Kill getRootLayout() Method
/*        try {
            findAndHookMethod(XposedHelpers.findClass("cn.flyaudio.media.view.activity.MusicPlaybackActivity", lpparam.classLoader), "getRootLayout", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    try {
                        Log.d(LOG_TAG, ">>>>>>>>>>>>>>>>>>>>>  Kill getRootLayout() Method >>>>>>>>>>>> !!!!");
    //                     activity.finish();
                    } catch (Throwable t) {
                        param.setThrowable(t);
                    }
                    return null;
                }
            });
        } catch (Throwable t) {
        }
*/
        // Step 2a - Replace MusicPlaybackActivity
/*        XposedHelpers.findAndHookMethod(XposedHelpers.findClass("cn.flyaudio.media.view.activity.MusicPlaybackActivity", lpparam.classLoader), "onCreate", Bundle.class, new XC_MethodReplacement() {
            @Override
            @SuppressWarnings("serial")
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                try {
                    Log.d(LOG_TAG, "Replace MusicPlaybackActivity with PowerAmp >>>>>>>>>>>> !!!!");
                    Log.d(LOG_TAG, "We are inside onCreate method");

                    Activity activity = (Activity) param.thisObject;

                    //PowerAmp

                    Log.d(LOG_TAG, "Open PowerAmp window itself");
                    Intent pwramp_start_intent = new Intent();
                    pwramp_start_intent.setPackage("com.maxmpz.audioplayer");
                    pwramp_start_intent.setClassName("com.maxmpz.audioplayer", "com.maxmpz.audioplayer.StartupActivity");
                    activity.startActivity(pwramp_start_intent);

                    Log.d(LOG_TAG, "Send Intent to start PowerAmp service with Resume (play) function");
                    Intent pwramp_intent = new Intent("com.maxmpz.audioplayer.API_COMMAND");
                    pwramp_intent.setPackage("com.maxmpz.audioplayer");
                    pwramp_intent.putExtra("cmd", 3);
                    activity.startService(pwramp_intent);

                    Log.d(LOG_TAG, "Intent is successfully sent !!!!");
                    Log.d(LOG_TAG, ">>>>>>>>>>>>>>>>>>>>>>> QUIT >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    System.exit(0);
                    Log.d(LOG_TAG, ">>>>>>>>>>>>>>>>>>>>>>> QUIT DONE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                } catch (Throwable t) {
                    param.setThrowable(t);
                }
                return null;
            }
        });
*/



/*
        // Step 3 - Hook to MusicPlaybackActivity onResume
        findAndHookMethod(XposedHelpers.findClass("cn.flyaudio.media.view.activity.MusicPlaybackActivity", lpparam.classLoader), "onResume", new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.d(LOG_TAG, "We are inside !!!!! onResume !!!!! method");

                Activity activity = (Activity) param.thisObject;

                //PowerAmp

                Log.d(LOG_TAG, "Open PowerAmp window itself");
                Intent pwramp_start_intent = new Intent();
                pwramp_start_intent.setPackage("com.maxmpz.audioplayer");
                pwramp_start_intent.setClassName("com.maxmpz.audioplayer", "com.maxmpz.audioplayer.StartupActivity");
                activity.startActivity(pwramp_start_intent);

                Log.d(LOG_TAG, "Send Intent to start PowerAmp service with Resume (play) function");
                Intent pwramp_intent = new Intent("com.maxmpz.audioplayer.API_COMMAND");
                pwramp_intent.setPackage("com.maxmpz.audioplayer");
                pwramp_intent.putExtra("cmd", 3);
                activity.startService(pwramp_intent);

                Log.d(LOG_TAG, "Intent is successfully sent !!!!");

//                Log.d(LOG_TAG, "Try to close the original app (FlyAudio player) - prevent from running >>>>>>");
//                activity.finish();
                // Instead of activity.finish() you can use param.setResult(null) to prevent original app from running, but in my case the app was closed with error.
//                param.setResult(null);
                try {
//                    param.setResult(null);
                } catch (Throwable t) {
                    param.setThrowable(t);
                }
                Log.d(LOG_TAG, "Exit hook");
            }
        });

*/

/*             // Step 3 - Kill MusicPlaybackActivity
             findAndHookMethod(XposedHelpers.findClass("cn.flyaudio.media.view.activity.MusicPlaybackActivity", lpparam.classLoader), "onCreate", Bundle.class, new XC_MethodReplacement() {
                 @Override
                 @SuppressWarnings("all")
                 protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                     try {
                         Log.d(LOG_TAG, "Replace FlyMediaApplication with Nothing >>>>>>>>>>>> !!!!");
//                     activity.finish();
                     } catch (Throwable t) {
                         param.setThrowable(t);
                     }
                     return null;
                 }
             });

*/
        // =========================================================================================
        // ===== OPTION 1: Hook before/after the original method (e.g. "onCreate") is executed =====
        // ============================================================================
/*        findAndHookMethod(XposedHelpers.findClass("cn.flyaudio.media.view.activity.MusicPlaybackActivity", lpparam.classLoader), "onResume", new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.d(LOG_TAG, "We are inside onCreate method");

                Activity activity = (Activity) param.thisObject;

                //PowerAmp

                Log.d(LOG_TAG, "Open PowerAmp window itself");
                Intent pwramp_start_intent = new Intent();
                pwramp_start_intent.setPackage("com.maxmpz.audioplayer");
                pwramp_start_intent.setClassName("com.maxmpz.audioplayer", "com.maxmpz.audioplayer.StartupActivity");
                activity.startActivity(pwramp_start_intent);

                Log.d(LOG_TAG, "Send Intent to start PowerAmp service with Resume (play) function");
                Intent pwramp_intent = new Intent("com.maxmpz.audioplayer.API_COMMAND");
                pwramp_intent.setPackage("com.maxmpz.audioplayer");
                pwramp_intent.putExtra("cmd", 3);
                activity.startService(pwramp_intent);

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

*/
        // ========================================================================
        // ===== OPTION 2: REPLACE method (e.g. "onCreate") with our own code =====
        // ========================================================================
 //        findAndHookMethod(XposedHelpers.findClass("cn.flyaudio.media.view.activity.MusicPlaybackActivity", lpparam.classLoader), "onCreate", Bundle.class, new XC_MethodReplacement() {
/*         findAndHookMethod(XposedHelpers.findClass("cn.flyaudio.media.FlyMediaApplication", lpparam.classLoader), "onCreate", new XC_MethodReplacement() {
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
                     Log.d(LOG_TAG, "Replace FlyMediaApplication with Nothing >>>>>>>>>>>> !!!!");
                     activity.finish();
                 } catch (Throwable t) {
                     param.setThrowable(t);
                 }
                return null;
            }


        });
*/


        // =================================================================================
        // ========================== DIFFERENT STAFF ======================================
        // =================================================================================
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
    }




}
