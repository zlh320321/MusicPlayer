package com.example.henly.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class PlayService extends Service {
    public PlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new PlayBinder();
    }

    public class PlayBinder extends Binder {

        public void play(String songPath){

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
