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
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SongListFragment.SongItemClickListener, View.OnClickListener {
    public static final String SONG_LIST_FRAGMENT = "song_list_fragment";
    public static final String SPLASH_FRAGMENT = "splash_fragment";
    public static final int SCAN_MUSIC_FINISHED = 1000;
    public IPlayService mPlayServiceBinder;
    private SongListFragment mSongListFragment;
    private SplashFragment mSplashFragment;
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
    private FragmentManager mFragmentManager;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCAN_MUSIC_FINISHED:
//                    mFragmentTrans.replace(R.id.fragment_container,mSongListFragment);
//                    mFragmentTrans.commitAllowingStateLoss();
                    break;
            }

        }
    };
    private Runnable mScanMusicThread = new Runnable(){
        Handler handler;
        @Override
        public void run() {
            MusicUtils.scanMusic(getApplicationContext(),mHandler);
        }
    };

    private Runnable mUpdateProgressBarRunnable = new Runnable() {
        @Override
        public void run() {
//            String currentSongDuration = convertSongDuration(mPlayServiceBinder.getCurrentPosition());
//            mSongCurrentDuration.setText(currentSongDuration);
//            mSongProgressBar.setProgress(mPlayServiceBinder.getCurrentPosition());
            mHandler.postDelayed(mUpdateProgressBarRunnable, 1000);
        }
    };

    public ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayServiceBinder = IPlayService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayServiceBinder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent(this, IPlayService.class);
        bindService(serviceIntent, mServiceConnection, Service.BIND_AUTO_CREATE);

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        mSongListFragment = new SongListFragment();
        mSongListFragment.registerSongItemClickListener(this);
        mSplashFragment = new SplashFragment();
        ft.add(R.id.fragment_container, mSplashFragment, SPLASH_FRAGMENT);
        ft.add(R.id.fragment_container, mSongListFragment, SONG_LIST_FRAGMENT);
        ft.hide(mSongListFragment);
        Fragment songControlFragment = mFragmentManager.findFragmentById(R.id.song_control_fragment);
        ft.hide(songControlFragment);
        ft.commit();
        mHandler.postDelayed(mScanMusicThread,3000);
//        mSongProgressBar = (ProgressBar) songControlFragment.getView().findViewById(R.id.song_progress);
//        mSongCurrentDuration = (TextView) songControlFragment.getView().findViewById(R.id.song_current_duration);
//        mSongDuration = (TextView) songControlFragment.getView().findViewById(R.id.song_duration);
//        mPlayOrPauseButton = (ImageButton) songControlFragment.getView().findViewById(R.id.song_play_or_pause);
//        mPlayOrPauseButton.setOnClickListener(this);
//        mPreviousButton = (ImageButton) songControlFragment.getView().findViewById(R.id.song_previous);
//        mPreviousButton.setOnClickListener(this);
//        mNextButton = (ImageButton) songControlFragment.getView().findViewById(R.id.song_next);
//        mNextButton.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    @Override
    public void songListItemClick(Song song) {
        //startPlayOrPause(song);
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
                //startPlayOrPause(null);
                break;
            default:
                break;
        }

    }

}
