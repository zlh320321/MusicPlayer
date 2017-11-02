package com.example.henly.musicplayer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    public static final String SONG_LIST_FRAGMENT = "song_list_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment songListFragment = new SongListFragment();
        ft.add(R.id.fragment_container,songListFragment,SONG_LIST_FRAGMENT);
        ft.commitAllowingStateLoss();
    }
}
