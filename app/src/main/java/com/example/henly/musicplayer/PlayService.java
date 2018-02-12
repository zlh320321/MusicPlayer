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

        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(mOnCompletionListener);
        return stub;
    }

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            try {
                stub.next();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
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
        public void play(final int position) throws RemoteException {
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
        public int prev() throws RemoteException {
            if (mCurrentPosition == 0) {
                mCurrentPosition = MusicUtils.getMusicList().size()-1;
            } else {
                mCurrentPosition = mCurrentPosition - 1;
            }
            play(mCurrentPosition);
            return mCurrentPosition;
        }

        @Override
        public int next() throws RemoteException {
            if (mCurrentPosition >= MusicUtils.getMusicList().size()) {
                mCurrentPosition = 0;
            } else {
                mCurrentPosition = mCurrentPosition + 1;
            }
            play(mCurrentPosition);
            return mCurrentPosition;
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

        @Override
        public int loopNextSong() throws RemoteException {
            return 0;
        }
    };
}
