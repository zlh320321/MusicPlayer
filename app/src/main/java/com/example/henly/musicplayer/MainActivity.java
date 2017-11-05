package com.example.henly.musicplayer;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements SongItemClickListener{
    public static final String SONG_LIST_FRAGMENT = "song_list_fragment";
    public PlayService mPlayService;

    public ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayService= (PlayService) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent(this,PlayService.class);
        bindService(serviceIntent,mServiceConnection, Service.BIND_AUTO_CREATE);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment songListFragment = new SongListFragment();
        ft.add(R.id.fragment_container,songListFragment,SONG_LIST_FRAGMENT);
        ft.commitAllowingStateLoss();
    }
}
