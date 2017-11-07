package com.example.henly.musicplayer;

/**
 * Created by Henly on 2017/11/5.
 */

public class Song {
    public String mSongName;
    public String mSongArtist;
    public String mSongLyric;
    public String mSongPath;
    public long mSongSize;
    public int mSoneDuration;

    @Override
    public String toString() {
        return "Song{" +
                "mSongName='" + mSongName + '\'' +
                ", mSongArtist='" + mSongArtist + '\'' +
                ", mSongLyric='" + mSongLyric + '\'' +
                ", mSongPath='" + mSongPath + '\'' +
                ", mSongSize=" + mSongSize +
                ", mSoneDuration=" + mSoneDuration +
                '}';
    }
}
