package com.example.henly.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Henly on 2017/11/1.
 */

public class MusicUtils {
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
                Log.i("scanMusic",song.toString());
                mSongList.add(song);
            } while (cursor.moveToNext());
            mScanFinished = true;
            handler.sendEmptyMessage(MainActivity.SCAN_MUSIC_FINISHED);
        }
        cursor.close();
    }

    public static ArrayList<Song> getMusicList(){
        return mSongList;
    }

}
