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

        public void play(Song song){
            try {
            Log.i("zhanglh","play song:"+song);
            MediaPlayer player = MediaPlayer.create(getApplicationContext(), Uri.fromFile(new File(song.mSongPath)));
            player.start();
            } catch (Exception e) {

            }
        }

        public void pause(){

        }

        public long getSongDuration(){
            return 0;
        }

        public void getCurrentProgress(){

        }
    }
}
