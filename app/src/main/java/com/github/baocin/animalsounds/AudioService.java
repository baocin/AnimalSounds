//package com.github.baocin.animalsounds;
//
//import android.app.IntentService;
//import android.app.Service;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//
///**
// * Created by aoi on 1/10/16.
// */
//public class AudioService extends Service implements MediaPlayer.OnPreparedListener {
//    private static final String ACTION_PLAY = "com.example.action.PLAY";
//    MediaPlayer mMediaPlayer = null;
//
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent.getAction().equals(ACTION_PLAY)) {
//            mMediaPlayer = new MediaPlayer();
//            mMediaPlayer.setOnPreparedListener(this);
//            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
//        }
//    }
//
//    /** Called when MediaPlayer is ready */
//    public void onPrepared(MediaPlayer player) {
//        player.start();
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//}
