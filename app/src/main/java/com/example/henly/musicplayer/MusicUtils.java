package com.example.henly.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

/**
 * Created by Henly on 2017/11/1.
 */

public class MusicUtils {
    public static Cursor scanMusic(Context context) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        return cursor;
    }
}
