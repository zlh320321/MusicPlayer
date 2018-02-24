package com.example.henly.musicplayer;

import android.Manifest;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SongListFragment.SongItemClickListener, View.OnClickListener {
    private static final String TAG = "MainActivity";
    public static final String SONG_LIST_FRAGMENT = "song_list_fragment";
    public static final String SPLASH_FRAGMENT = "splash_fragment";
    public static final int SCAN_MUSIC_FINISHED = 1000;
    private static final int TAB_INDEX_COUNT = 2;
    private static final int TAB_INDEX_SONG_LIST = 0;
    private static final int TAB_INDEX_SONG_LYRIC = 1;
    public PlayService mPlayServiceBinder;
    private SongListFragment mSongListFragment;
    private SongLyricFragment mSongLyricFragment;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private SplashScreen mSplashScreen;
    private final static String MUSIC_PLAYER_SHARRED_PREFERENCE = "music_player_shared_preference";
    private final static String CURRENT_POSITION = "current_position";
    private final static String CURRENT_SONG_PATH = "current_song_path";
    public static int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private int mCurrentSong;
    private SharedPreferences mSharedPreference;
    private SeekBar mSongProgressBar;
    private TextView mSongCurrentDuration;
    private TextView mSongDuration;
    private ImageButton mPlayOrPauseButton;
    private ImageButton mPreviousButton;
    private ImageButton mNextButton;
    private ImageButton mQuitButton;
    private FragmentManager mFragmentManager;
    private boolean mIsPlaying = false;
    private boolean mShouldPauseUpdateProcess = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCAN_MUSIC_FINISHED:
                    mSplashScreen.removeSplashScreen();
                    mSongListFragment.refresh();
                    break;
            }

        }
    };
    private Runnable mScanMusicThread = new Runnable(){
        @Override
        public void run() {
            MusicUtils.scanMusic(getApplicationContext(),mHandler);
        }
    };

    private Runnable mUpdateProgressBarRunnable = new Runnable() {
        @Override
        public void run() {
            if (!mShouldPauseUpdateProcess) {
                int currentPosition = 0;
                currentPosition = mPlayServiceBinder.position();
                String currentSongDuration = convertSongDuration(currentPosition);
                mSongCurrentDuration.setText(currentSongDuration);
                mSongProgressBar.setProgress(currentPosition);
                mHandler.postDelayed(mUpdateProgressBarRunnable, 1000);
            }
        }
    };

    public ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayService.MyBinder myBinder = (PlayService.MyBinder)service;
            mPlayServiceBinder = myBinder.getService();
            myBinder.setServiceDataToActivity(mServiceDataToActivity);
            mServiceDataToActivity.refreshSongViewInfo(mCurrentSong);
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
        Intent serviceIntent = new Intent(this, PlayService.class);
        bindService(serviceIntent, mServiceConnection, Service.BIND_AUTO_CREATE);
        mFragmentManager = getSupportFragmentManager();
        mSongListFragment = new SongListFragment();
        mSongLyricFragment = new SongLyricFragment();

        ActionBar actionBar =  getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setUpViewPager();
        mSplashScreen = new SplashScreen(this);
        if (ContextCompat.checkSelfPermission(this , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            mSplashScreen.showSplash(R.mipmap.splash,SplashScreen.SLIDE_LEFT);
            mHandler.post(mScanMusicThread);
        }
        mSongListFragment.registerSongItemClickListener(this);
        Fragment songControlFragment = mFragmentManager.findFragmentById(R.id.song_control_fragment);
        mSongProgressBar = (SeekBar) songControlFragment.getView().findViewById(R.id.song_progress);
        mSongProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mShouldPauseUpdateProcess = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mShouldPauseUpdateProcess = false;
                mPlayServiceBinder.seek(seekBar.getProgress());
                mHandler.post(mUpdateProgressBarRunnable);
            }
        });
        mSongCurrentDuration = (TextView) songControlFragment.getView().findViewById(R.id.song_current_duration);
        mSongDuration = (TextView) songControlFragment.getView().findViewById(R.id.song_duration);
        mPlayOrPauseButton = (ImageButton) songControlFragment.getView().findViewById(R.id.song_play_or_pause);
        mPlayOrPauseButton.setOnClickListener(this);
        mPreviousButton = (ImageButton) songControlFragment.getView().findViewById(R.id.song_previous);
        mPreviousButton.setOnClickListener(this);
        mNextButton = (ImageButton) songControlFragment.getView().findViewById(R.id.song_next);
        mNextButton.setOnClickListener(this);
        mQuitButton = (ImageButton) songControlFragment.getView().findViewById(R.id.song_exit);
        mQuitButton.setOnClickListener(this);
        if (savedInstanceState != null) {
            mCurrentSong = savedInstanceState.getInt("currentSong");
        }

    }

    private void setUpViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.fragment_container);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mViewPagerAdapter);
    }

    @Override
    public void songListItemClick(int position) {
        startPlayOrPause(position,true);
    }

    private String convertSongDuration(int duration) {
        int totalTime = Math.round(duration / 1000);
        String str = String.format("%02d:%02d", totalTime / 60, totalTime % 60);
        return str;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish();
            } else {
                mSplashScreen.showSplash(R.mipmap.splash,SplashScreen.SLIDE_LEFT);
                mHandler.post(mScanMusicThread);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentSong",mCurrentSong);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSplashScreen.removeSplashScreen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.song_play_or_pause:
                startPlayOrPause(mCurrentSong,false);
                break;
            case R.id.song_next:
                    mCurrentSong = mPlayServiceBinder.next();
                break;
            case R.id.song_previous:
                    mCurrentSong = mPlayServiceBinder.prev();
                break;
            case R.id.song_exit:
                mPlayServiceBinder.stop();
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Toast.makeText(this,"keyCode:"+keyCode+" KeyEvent: "+event,Toast.LENGTH_LONG).show();
        return super.onKeyDown(keyCode, event);
    }

    public void startPlayOrPause(int position, boolean change) {
            if (mPlayServiceBinder != null) {
                if (!mIsPlaying || change) {
                    mPlayServiceBinder.play(position);
                    mIsPlaying = true;
                    mServiceDataToActivity.refreshSongViewInfo(position);
                } else {
                    mPlayServiceBinder.pause();
                    mPlayOrPauseButton.setBackgroundResource(R.mipmap.play_btn);
                    mIsPlaying = false;
                }
            }
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case TAB_INDEX_SONG_LIST:
                    return mSongListFragment;
                case TAB_INDEX_SONG_LYRIC:
                    return mSongLyricFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return TAB_INDEX_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case TAB_INDEX_SONG_LIST:
                    return "Song_List";
                case TAB_INDEX_SONG_LYRIC:
                    return "Song_Lyric";
            }
            return super.getPageTitle(position);
        }
    }

    private PlayService.IServiceDataToActivity mServiceDataToActivity = new PlayService.IServiceDataToActivity() {
        @Override
        public void refreshSongViewInfo(int position) {
            Log.i(TAG,"refreshSongViewInfo mCurrentSong :"+position);
            mCurrentSong = position;
            mSongListFragment.refreshSelectItem(position);
            int songDuration = mPlayServiceBinder.duration();
            mSongProgressBar.setMax(songDuration);
            mSongDuration.setText(convertSongDuration(songDuration));
            mPlayOrPauseButton.setBackgroundResource(R.mipmap.pause_btn);
            mHandler.post(mUpdateProgressBarRunnable);
        }
    };
}
