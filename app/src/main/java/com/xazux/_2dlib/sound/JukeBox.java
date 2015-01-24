package com.xazux._2dlib.sound;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.Vector;

public class JukeBox {
    protected static final float MAX_VOLUME = 0.7f;
    protected Vector<MediaPlayer> m_mediaPlayers;
    protected MediaPlayer m_currentMedia = null;
    protected Context m_context;
    private boolean m_bIsPlaying = false;

    public JukeBox(Context context) {
        m_context = context;
        m_mediaPlayers = new Vector<MediaPlayer>();
    }

    public int registerPlayer(int resourceID) {
        MediaPlayer m = MediaPlayer.create(m_context, resourceID);
        m.setVolume(MAX_VOLUME, MAX_VOLUME);
        m.setLooping(true);
        m_mediaPlayers.add(m);
        return m_mediaPlayers.size() - 1;
    }

    public void play(boolean fromBegining) {
        if (m_currentMedia != null) {
            if (fromBegining)
                m_currentMedia.seekTo(0);
            m_currentMedia.start();
            m_bIsPlaying = true;
        }
    }

    public void pause() {
        if (m_currentMedia != null) {
            m_currentMedia.pause();
            m_bIsPlaying = false;
        }
    }

    public void stop() {
        if (m_currentMedia != null) {
            m_currentMedia.pause();
            m_currentMedia.seekTo(0);
            m_bIsPlaying = false;
        }
    }

    public void setCurrentPlayer(int registeredID, boolean play) {
        if (registeredID > -1 && registeredID < m_mediaPlayers.size()) {
            stop();
            m_currentMedia = m_mediaPlayers.get(registeredID);
            if (play)
                play(false);
        } else
            Log.e("JukeBox", "Media player not found: id was " + registeredID);
    }

    public boolean isPlaying() {
        return m_bIsPlaying;
    }
}
