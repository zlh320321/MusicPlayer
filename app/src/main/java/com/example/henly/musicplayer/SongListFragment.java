package com.example.henly.musicplayer;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
    private static final String TAG = "SongListFragment";

    private SongListAdapter mSongListAdapter;
    private SongItemClickListener mSongItemClickListener;
    private ListView mListView;

    public SongListFragment() {
    }

    public interface SongItemClickListener {
        public void songListItemClick(int position);
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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSongListAdapter = new SongListAdapter(getActivity(), MusicUtils.getMusicList());
        mListView.setAdapter(mSongListAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSongItemClickListener.songListItemClick(position);
    }

    public void registerSongItemClickListener(SongItemClickListener listener) {
        mSongItemClickListener = listener;
    }

    public void refresh() {
        if (mSongListAdapter != null) {
            mSongListAdapter.notifyDataSetChanged();
        }
    }

    public void refreshSelectItem(int position) {
        Log.i(TAG, "refreshSelectItem position :" + position + "  mSongListAdapter:" + mSongListAdapter);
        if (mSongListAdapter != null) {
            mSongListAdapter.setmSelectItem(position);
            mListView.smoothScrollToPosition(position);
            mSongListAdapter.notifyDataSetInvalidated();
        }
    }
}
