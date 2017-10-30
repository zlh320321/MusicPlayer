package com.example.henly.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Henly on 2017/10/30.
 */

public class SongListAdapter extends CursorAdapter {

    public SongListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        String songSinger = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        viewHolder.tv_song_name.setText(songName);
        viewHolder.tv_song_singer.setText(songSinger);
        super.bindView(view, context, cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.song_item_layout,parent,false);
        viewHolder.tv_song_name = (TextView) view.findViewById(R.id.song_name);
        viewHolder.tv_song_singer = (TextView) view.findViewById(R.id.song_singer);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
