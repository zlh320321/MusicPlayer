package com.example.henly.musicplayer;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SongListFragment.SongItemClickListener, View.OnClickListener {
    public static final String SONG_LIST_FRAGMENT = "song_list_fragment";
    public PlayService.PlayBinder mPlayServiceBinder;
    private SongListFragment mSongListFragment;
    private boolean mIsPausing = false;
    private final static String MUSIC_PLAYER_SHARRED_PREFERENCE = "music_player_shared_preference";
    private final static String CURRENT_POSITION = "current_position";
    private final static String CURRENT_SONG_PATH = "current_song_path";
    private Song mCurrentSong;
    private SharedPreferences mSharedPreference;
    private ProgressBar mSongProgressBar;
    private TextView mSongCurrentDuration;
    private TextView mSongDuration;
    private ImageButton mPlayOrPauseButton;
    private ImageButton mPreviousButton;
    private ImageButton mNextButton;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        }
    };

    private Runnable mUpdateProgressBarRunnable = new Runnable() {
        @Override
        public void run() {
            String currentSongDuration = convertSongDuration(mPlayServiceBinder.getCurrentPosition());
            mSongCurrentDuration.setText(currentSongDuration);
            mSongProgressBar.setProgress(mPlayServiceBinder.getCurrentPosition());
            mHandler.postDelayed(mUpdateProgressBarRunnable, 1000);
        }
    };

    public ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayServiceBinder = (PlayService.PlayBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent(this, PlayService.class);
        bindService(serviceIntent, mServiceConnection, Service.BIND_AUTO_CREATE);

        mSharedPreference = getSharedPreferences(MUSIC_PLAYER_SHARRED_PREFERENCE, Context.MODE_PRIVATE);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mSongListFragment = new SongListFragment();
        mSongListFragment.registerSongItemClickListener(this);
        ft.add(R.id.fragment_container, mSongListFragment, SONG_LIST_FRAGMENT);
        ft.commitAllowingStateLoss();
        Fragment songControlFragment = fm.findFragmentById(R.id.song_control_fragment);
        mSongProgressBar = (ProgressBar) songControlFragment.getView().findViewById(R.id.song_progress);
        mSongCurrentDuration = (TextView) songControlFragment.getView().findViewById(R.id.song_current_duration);
        mSongDuration = (TextView) songControlFragment.getView().findViewById(R.id.song_duration);
        mPlayOrPauseButton = (ImageButton) songControlFragment.getView().findViewById(R.id.song_play_or_pause);
        mPlayOrPauseButton.setOnClickListener(this);
        mPreviousButton = (ImageButton) songControlFragment.getView().findViewById(R.id.song_previous);
        mPreviousButton.setOnClickListener(this);
        mNextButton = (ImageButton) songControlFragment.getView().findViewById(R.id.song_next);
        mNextButton.setOnClickListener(this);

        String currentSongPath = mSharedPreference.getString(CURRENT_SONG_PATH,"");
        int currentSongPosition = mSharedPreference.getInt(CURRENT_POSITION,0);
        if (!TextUtils.isEmpty(currentSongPath)) {
            Song song = new Song();
            song.mSongPath = currentSongPath;
            play(song,currentSongPosition);
        }
    }

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putInt(CURRENT_POSITION,mPlayServiceBinder.getCurrentPosition());
        editor.putString(CURRENT_SONG_PATH,mCurrentSong.mSongPath);
        editor.apply();
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    @Override
    public void songListItemClick(Song song) {
        startPlayOrPause(song);
    }

    private void startPlayOrPause(Song song) {
        if (song != null) {
            play(song,0);
        } else {
            if (mPlayServiceBinder.isPlaying() && !mIsPausing) {
                mPlayServiceBinder.pause();
                mIsPausing = true;
                mPlayOrPauseButton.setBackgroundResource(R.mipmap.play_btn);
            } else if (!mPlayServiceBinder.isPlaying() && mIsPausing) {
                mPlayServiceBinder.play(null,0);
            } else {
                Song defaultPlaySong = mSongListFragment.getDefaultPlaySong();
                play(defaultPlaySong,0);
            }
        }
    }

    private void play(Song song,int currentPosition) {
        mPlayServiceBinder.play(song,currentPosition);
        mCurrentSong = song;
        updatePlayProgress(currentPosition);
    }

    private void updatePlayProgress(int currentPosition) {
        if (mPlayServiceBinder.isPlaying()) {
            mPlayOrPauseButton.setBackgroundResource(R.mipmap.pause_btn);
            mSongProgressBar.setMax(mPlayServiceBinder.getSongDuration());
            String songDuration = convertSongDuration(mPlayServiceBinder.getSongDuration());
            mSongDuration.setText(songDuration);
            if (currentPosition != 0) {
                mSongProgressBar.setProgress(currentPosition);
            }
            mHandler.postDelayed(mUpdateProgressBarRunnable, 1000);
        }
    }
    private String convertSongDuration(int duration) {
        int totalTime = Math.round(duration / 1000);
        String str = String.format("%02d:%02d", totalTime / 60, totalTime % 60);
        return str;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.song_play_or_pause:
                startPlayOrPause(null);
                break;
            default:
                break;
        }

    }

}
