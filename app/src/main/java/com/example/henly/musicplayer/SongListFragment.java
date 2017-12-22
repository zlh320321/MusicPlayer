package com.example.henly.musicplayer;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SongListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongListFragment extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SongListAdapter mSongListAdapter;
    private SongItemClickListener mSongItemClickListener;
    private ListView mListView;
    public SongListFragment() {
        // Required empty public constructor
    }

    public interface SongItemClickListener {
        public void songListItemClick(Song song);
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongListFragment newInstance(String param1, String param2) {
        SongListFragment fragment = new SongListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);
        mListView = (ListView) view.findViewById(R.id.song_list);
        mSongListAdapter = new SongListAdapter(getActivity(),R.layout.song_item_layout,MusicUtils.getMusicList());
        mListView.setAdapter(mSongListAdapter);
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor currentCursor = (Cursor) mSongListAdapter.getItem(position);
        Song song = new Song();
        song.mSongName = currentCursor.getString(currentCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
        song.mSongArtist = currentCursor.getString(currentCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
        song.mSongPath = currentCursor.getString(currentCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
        song.mSoneDuration = currentCursor.getInt(currentCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
        song.mSongSize = currentCursor.getLong(currentCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
        Log.i("zhanglh","onItemClick:"+song);
        mSongItemClickListener.songListItemClick(song);
        mSongListAdapter.setmSelectItem(position);
        mSongListAdapter.notifyDataSetInvalidated();
    }

    public void registerSongItemClickListener(SongItemClickListener listener){
        mSongItemClickListener = listener;
    }
    public Song getDefaultPlaySong(){
        Cursor defaultCursor = (Cursor) mSongListAdapter.getItem(0);
        Song song = new Song();
        song.mSongName = defaultCursor.getString(defaultCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
        song.mSongArtist = defaultCursor.getString(defaultCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
        song.mSongPath = defaultCursor.getString(defaultCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
        song.mSoneDuration = defaultCursor.getInt(defaultCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
        song.mSongSize = defaultCursor.getLong(defaultCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
        return song;
    }
}
