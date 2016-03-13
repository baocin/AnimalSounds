package com.github.baocin.animalsounds;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by aoi on 3/12/2016.
 */
public class AudioHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }

    public AudioHandler(Looper audioLooper) {
        super(audioLooper);
    }
}
