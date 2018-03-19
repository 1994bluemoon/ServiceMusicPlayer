package vinova.henry.com.servicemusicplayer.service;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import vinova.henry.com.servicemusicplayer.model.Song;

public class PlayerServiceImp extends Service implements IPlayerControler{

    private final IBinder mBinder = new LocalBinder();
    private MediaPlayer mMediaPlayer;
    List<Song> songs;
    int songPosition;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle ex = intent.getExtras();
        songs = new ArrayList<>();
        if (ex != null) {
            songPosition = ex.getInt("position");
            songs = (List<Song>) ex.getSerializable("SongList");
            play();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) mMediaPlayer.release();
    }

    @Override
    public void resume(){
        mMediaPlayer.start();
    }

    @Override
    public void next() {
        if (songPosition < songs.size()) {
            songPosition += 1;
            play();
        }
    }

    @Override
    public void previous() {
        if (songPosition > 0) {
            songPosition -= 1;
            play();
        }
    }
    @Override
    public void pause(){
        mMediaPlayer.pause();
    }

    @Override
    public void stop() {
        mMediaPlayer.stop();
    }

    private void play(){
        long id = songs.get(songPosition).getSongId();

        Uri contentUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        if (mMediaPlayer != null) mMediaPlayer.release();
        mMediaPlayer = MediaPlayer.create(this,contentUri);
        mMediaPlayer.start();
    }

    public class LocalBinder extends Binder {
        public PlayerServiceImp getService() {
            // Return this instance of LocalService so clients can call public methods
            return PlayerServiceImp.this;
        }
    }

}
