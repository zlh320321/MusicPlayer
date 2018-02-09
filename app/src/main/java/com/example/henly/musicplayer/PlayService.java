package com.example.henly.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import java.io.File;
import java.io.IOException;

public class PlayService extends Service {
    private MediaPlayer mPlayer;
    private int mCurrentPosition;

    public PlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        mPlayer = new MediaPlayer();
        return stub;
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
        public void play(int position) {
            try {
                Song song = MusicUtils.mSongList.get(position);
                if (song != null) {
                    mPlayer.reset();
                    mPlayer.setDataSource(getApplicationContext(), Uri.fromFile(new File(song.mSongPath)));
                    mPlayer.prepare();
                    if (position != 0) {
                        mPlayer.seekTo(position);
                    }
                    mPlayer.start();
                }
            } catch (Exception e) {
                mPlayer.release();
            }
        }

    }


    IPlayService.Stub stub = new IPlayService.Stub() {

        @Override
        public boolean isPlaying() throws RemoteException {
            return false;
        }

        @Override
        public void stop() throws RemoteException {

        }

        @Override
        public void pause() throws RemoteException {
            if(mPlayer.isPlaying()) {
                mPlayer.pause();
            }
        }

        @Override
        public void play(int position) throws RemoteException {
            try {
                if (position == mCurrentPosition) {
                    if (!mPlayer.isPlaying()) {
                        mPlayer.start();
                    }
                } else {
                    mPlayer.reset();
                    Song song = MusicUtils.getMusicList().get(position);
                    mPlayer.setDataSource(getApplicationContext(), Uri.fromFile(new File(song.mSongPath)));
                    mPlayer.prepare();
                    mPlayer.start();
                    mCurrentPosition = position;
                }
            } catch (IOException e) {
                mPlayer.release();
                mCurrentPosition = 0;
            }
        }

        @Override
        public void prev() throws RemoteException {

        }

        @Override
        public void next() throws RemoteException {

        }

        @Override
        public int duration() throws RemoteException {
            return mPlayer.getDuration();
        }

        @Override
        public int position() throws RemoteException {
            return mPlayer.getCurrentPosition();
        }

        @Override
        public void seek(int pos) throws RemoteException {
            mPlayer.seekTo(pos);
        }

        @Override
        public String getTrackName() throws RemoteException {
            return null;
        }

        @Override
        public String getAlbumName() throws RemoteException {
            return null;
        }

        @Override
        public long getAlbumId() throws RemoteException {
            return 0;
        }

        @Override
        public String getArtistName() throws RemoteException {
            return null;
        }

        @Override
        public long getArtistId() throws RemoteException {
            return 0;
        }
    };
}
