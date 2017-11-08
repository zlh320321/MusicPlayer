package com.example.henly.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.File;

public class PlayService extends Service {
    public PlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new PlayBinder();
    }

    public class PlayBinder extends Binder {
        private MediaPlayer mPlayer = new MediaPlayer();
        public void play(Song song){
            try {
                if (song != null) {
                    mPlayer.reset();
                    mPlayer.setDataSource(getApplicationContext(), Uri.fromFile(new File(song.mSongPath)));
                    mPlayer.prepare();
                    mPlayer.start();
                } else {
                    mPlayer.start();
                }
            } catch (Exception e) {

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
