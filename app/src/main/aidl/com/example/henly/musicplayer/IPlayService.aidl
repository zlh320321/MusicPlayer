// IPlayService.aidl
package com.example.henly.musicplayer;

// Declare any non-default types here with import statements

interface IPlayService {
    boolean isPlaying();
        void stop();
        void pause();
        void play(int position);
        void prev();
        void next();
        int duration();
        int position();
        void seek(int pos);
        String getTrackName();
        String getAlbumName();
        long getAlbumId();
        String getArtistName();
        long getArtistId();
}
