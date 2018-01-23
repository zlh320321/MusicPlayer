package com.example.henly.musicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

/**
 * Created by Henly on 2017/12/20.
 */

public class SongListAdapter extends BaseAdapter {
    ArrayList<Song> mSongList = null;
    private int mSelectItem;
    private Context mContext;

    public SongListAdapter(Context context,ArrayList<Song> mSongList) {
        mContext = context;
        this.mSongList = mSongList;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.song_item_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tv_song_name = (TextView) view.findViewById(R.id.song_name);
            viewHolder.tv_song_singer = (TextView) view.findViewById(R.id.song_singer);
            viewHolder.song_item_container = (RelativeLayout) view.findViewById(R.id.song_item_container);
            view.setTag(viewHolder);
        } else {
            ViewHolder viewHolder = (ViewHolder)view.getTag();
            Song song = getItem(position);
            viewHolder.tv_song_name.setText(song.mSongName);
            viewHolder.tv_song_singer.setText(song.mSongName);
        }
        return view;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Song getItem(int position) {
        return mSongList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setmSelectItem(int selectItem) {
        this.mSelectItem = selectItem;
    }
}
