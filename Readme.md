FlyAudio to PowerAmp
===========

An xposed module that replaces built in audio player on FlyAudio car head unit ("cn.flyaudio.media") with PowerAmp. 
PowerAmp is launched instead of this player.

This example can be used to create Xposed modules which launch one app instead of other. Launch of initial app is cancelled with finish() statement which is the less intrusive way to do this. 

This app Hooks to onCreate method (in this case onCreate method had parameters - protected void onCreate(Bundle savedInstanceState)). 


