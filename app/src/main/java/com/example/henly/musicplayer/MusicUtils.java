package com.example.henly.musicplayer;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;

/**
 * Created by Henly on 2017/11/1.
 */

public class MusicUtils {
    private static final String TAG = "MusicUitls";
    public static ArrayList<Song> mSongList = new ArrayList<Song>();
    public static boolean mScanFinished = false;
    public static void scanMusic(Context context, Handler handler) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor.moveToNext()) {
            do {
                Song song = new Song();
                song.mSongName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                song.mSongArtist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                song.mSongPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                song.mSoneDuration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                song.mSongSize = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                mSongList.add(song);
            } while (cursor.moveToNext());
        }
        mScanFinished = true;
        copyLyricFile("/sdcard/netease/cloudmusic/Download/Lyric/");
        handler.sendEmptyMessage(MainActivity.SCAN_MUSIC_FINISHED);
        cursor.close();
    }

    public static ArrayList<Song> getMusicList(){
        return mSongList;
    }

    public static void copyLyricFile(String path) {
        File lyricFilePath = new File(path);
        if(lyricFilePath.isDirectory()) {
            File[] lyricFiles = lyricFilePath.listFiles();
            for(File lyric : lyricFiles) {
                try {
                    FileReader fileReader = new FileReader(lyric);
                    BufferedReader bf = new BufferedReader(fileReader);
                    String lyricText = bf.readLine();
                    int startIndex = lyricText.indexOf("[ti:");
                    int endIndex = lyricText.indexOf("]");
                    String title = null;
                    if (startIndex > 0) {
                        title = lyricText.substring(startIndex+4, endIndex);
                    }
                    Log.i(TAG,"fileName: "+lyric.getName()+"  title: "+title);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
