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
        long duration();
        long position();
        long seek(long pos);
        String getTrackName();
        String getAlbumName();
        long getAlbumId();
        String getArtistName();
        long getArtistId();
}
