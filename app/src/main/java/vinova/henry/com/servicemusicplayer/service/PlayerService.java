package vinova.henry.com.servicemusicplayer.service;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import vinova.henry.com.servicemusicplayer.model.Songs;

/**
 * Created by dminh on 2/5/2018.
 */

public class PlayerService extends Service{

    private static final String ACTION_PLAY = "com.example.action.PLAY";
    private MediaPlayer mMediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle ex = intent.getExtras();
        if (ex != null) {
            Songs song = (Songs) ex.get("Song");
            long id = song.getSongId();
            Uri contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
            mMediaPlayer = MediaPlayer.create(this,contentUri);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) { mMediaPlayer.release();}
    }
}
