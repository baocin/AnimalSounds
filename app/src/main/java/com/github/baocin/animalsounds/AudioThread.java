package com.github.baocin.animalsounds;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by aoi on 3/11/2016.
 */
public class AudioThread extends Thread {
    public static final int PLAY_STATUS = 0;
    public static final int STOP_STATUS = 1;
    public static final int SPEAK_STATUS = 2;
    public MediaPlayer mp;
    public TextToSpeech ttobj;
    public HashMap<String, Integer> animalAudio = new HashMap<>();
    private Context context;
    public Handler handler;
    public Handler mainHandler;
    private String TAG = "AudioThread";

    public AudioThread(Context c, Handler mh){
        context = c;
        mainHandler = mh;

        animalAudio.put("cat", R.raw.cat);
        animalAudio.put("cow", R.raw.cow);
        animalAudio.put("donkey", R.raw.donkey);
        animalAudio.put("frog", R.raw.frog);
        animalAudio.put("horse", R.raw.horse);
        animalAudio.put("pig", R.raw.pig);
        animalAudio.put("sheep", R.raw.sheep);
        animalAudio.put("fox", R.raw.fox);

        mp = new MediaPlayer();

        ttobj=new TextToSpeech(c, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                ttobj.setLanguage(Locale.US);
            }
        });

        //Setup Handler
        handler = new Handler(new Handler.Callback() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case AudioThread.PLAY_STATUS:
                        final String animalName = msg.getData().getString("data");

                        if (animalAudio.containsKey(animalName)){
                            mp = MediaPlayer.create(context, animalAudio.get(animalName));
                            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    stopAudio();
                                    Log.d(TAG, "MP Completed");
                                    mainHandler.sendEmptyMessage(MainActivityFragment.MUSIC_DONE);
                                }
                            });
                            mp.start();
//                            ttobj.setOnUtteranceProgressListener(new UtteranceProgressListener() {
//                                @Override
//                                public void onStart(String utteranceId) {
//                                }
//
//                                @Override
//                                public void onDone(String utteranceId) {
//                                    Log.d(TAG, "Done speaking");
//                                    mp = MediaPlayer.create(context, animalAudio.get(animalName));
//                                    mp.start();
//                                }
//
//                                @Override
//                                public void onError(String utteranceId) {
//                                }
//                            });

                        }


                        break;
                    case AudioThread.STOP_STATUS:
                        stopAudio();
                        break;
                    case AudioThread.SPEAK_STATUS:
                        String speechText = msg.getData().getString("data");
                        if (ttobj!=null){
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");
                            ttobj.speak(speechText, TextToSpeech.QUEUE_FLUSH, map);
                        }
                        break;
                }
                return false;
            }
        });

//        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                Bundle bundle = new Bundle();
//                bundle.putBoolean("AudioDone", true);
//                Message message = new Message();
//                message.setData(bundle);
//                handler.sendMessage(message);
//            }
//        });
    }

    @Override
    public void run() {
        //Setup MessageQueue
        Looper.prepare();
        Looper.loop();
    }

    public boolean isPlaying(){
        return mp.isPlaying() || ttobj.isSpeaking();
    }

    public void stopAudio(){
        //Stop audio (if playing)
//        mp.stop();

        mp.release();

        //Stop text to speach
        ttobj.stop();
    }


}
