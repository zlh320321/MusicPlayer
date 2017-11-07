package com.example.henly.musicplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SongControlFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SongControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongControlFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton mPlayOrPauseBtn;
    private ImageButton mNextBtn;
    private ImageButton mPreviousBtn;

    public SongControlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongControlFragment newInstance(String param1, String param2) {
        SongControlFragment fragment = new SongControlFragment();
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
        View view = inflater.inflate(R.layout.fragment_song_control, container, false);
        mPlayOrPauseBtn = (ImageButton) view.findViewById(R.id.song_play_or_pause);
        mNextBtn = (ImageButton) view.findViewById(R.id.song_next);
        mPreviousBtn = (ImageButton) view.findViewById(R.id.song_previous);
        return view;
    }
}
