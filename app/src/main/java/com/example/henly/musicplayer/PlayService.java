package com.example.henly.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import java.io.File;
import java.io.IOException;

public class PlayService extends Service implements IPlayService {
    private MediaPlayer mPlayer;
    private int mCurrentPosition;
    private MyBinder mMyBinder = new MyBinder();
    private IServiceDataToActivity mServiceDataToActivity;

    public PlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(mOnCompletionListener);
        return mMyBinder;
    }

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
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

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void stop() {
        stopSelf();
        mPlayer.release();
        mCurrentPosition = 0;
    }

    @Override
    public void pause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    @Override
    public void play(final int position) {
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
            mServiceDataToActivity.refreshSongViewInfo(mCurrentPosition);
        } catch (IOException e) {
            mPlayer.release();
            mCurrentPosition = 0;
        }
    }

    @Override
    public int prev() {
        int tempPosition = 0;
        if (mCurrentPosition == 0) {
            tempPosition = MusicUtils.getMusicList().size() - 1;
        } else {
            tempPosition = mCurrentPosition - 1;
        }
        play(tempPosition);
        return tempPosition;
    }

    @Override
    public int next() {
        int tempPosition = 0;
        if (mCurrentPosition <= MusicUtils.getMusicList().size()) {
            tempPosition = mCurrentPosition + 1;
        }
        play(tempPosition);
        return tempPosition;
    }

    @Override
    public int duration() {
        return mPlayer.getDuration();
    }

    @Override
    public int position() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public void seek(int pos) {
        mPlayer.seekTo(pos);
    }

    @Override
    public String getTrackName() {
        return null;
    }

    @Override
    public String getAlbumName() {
        return null;
    }

    @Override
    public long getAlbumId() {
        return 0;
    }

    @Override
    public String getArtistName() {
        return null;
    }

    @Override
    public long getArtistId() {
        return 0;
    }

    @Override
    public int loopNextSong() {
        return 0;
    }

    public interface IServiceDataToActivity {
        public void refreshSongViewInfo(int position);
    }

    public class MyBinder extends Binder {
        PlayService getService() {
            return PlayService.this;
        }

        public void setServiceDataToActivity(IServiceDataToActivity serviceDataToActivity){
            mServiceDataToActivity = serviceDataToActivity;
        }
    }
}
