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

        public void pause() {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            }
        }

        public int getSongDuration() {
            return mPlayer.getDuration();
        }

        public int getCurrentPosition() {
            return mPlayer.getCurrentPosition();
        }

        public boolean isPlaying() {
            return mPlayer.isPlaying();
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

        }

        @Override
        public void play(int position) throws RemoteException {
            try {
                mPlayer.reset();
                mPlayer.setDataSource(getApplicationContext(), Uri.fromFile(new File(song.mSongPath)));
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                mPlayer.release();
            }
        }

        @Override
        public void prev() throws RemoteException {

        }

        @Override
        public void next() throws RemoteException {

        }

        @Override
        public long duration() throws RemoteException {
            return 0;
        }

        @Override
        public long position() throws RemoteException {
            return 0;
        }

        @Override
        public long seek(long pos) throws RemoteException {
            return 0;
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
