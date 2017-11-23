package com.example.henly.musicplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

public class PlayService extends Service {

    private MediaPlayer mPlayer;


    public PlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        mPlayer = new MediaPlayer();
        return new PlayBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class PlayBinder extends Binder {
        public void play(Song song,int position){
            try {
                if (song != null) {
                    mPlayer.reset();
                    mPlayer.setDataSource(getApplicationContext(), Uri.fromFile(new File(song.mSongPath)));
                    mPlayer.prepare();
                    if (position != 0) {
                        mPlayer.seekTo(position);
                    }
                    mPlayer.start();
                } else {
                    mPlayer.start();
                }
            } catch (Exception e) {
                mPlayer.release();
            }
        }

        public void pause(){
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            }
        }

        public int getSongDuration(){
            return mPlayer.getDuration();
        }

        public int getCurrentPosition(){
            return mPlayer.getCurrentPosition();
        }
        public boolean isPlaying(){
            return mPlayer.isPlaying();
        }
    }
}
