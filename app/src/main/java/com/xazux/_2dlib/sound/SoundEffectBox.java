package com.xazux._2dlib.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

public class SoundEffectBox {
    private SoundPool m_SoundPool;
    private SparseIntArray m_SoundPoolMap;
    private AudioManager m_AudioManager;
    private Context m_Context;
    private int m_iSoundIDIndex = 1;

    public SoundEffectBox(Context theContext) {
        m_Context = theContext;
        m_SoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        m_SoundPoolMap = new SparseIntArray();
        m_AudioManager = (AudioManager) m_Context.getSystemService(Context.AUDIO_SERVICE);
    }

    public int addSound(int SoundID) {
        m_SoundPoolMap.put(m_iSoundIDIndex, m_SoundPool.load(m_Context, SoundID, 1));
        return m_iSoundIDIndex++;
    }

    public void playSound(int index) {
        float streamVolume = m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / m_AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        m_SoundPool.play((Integer) m_SoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
    }

    public void playLoopedSound(int index) {
        float streamVolume = m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / m_AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        m_SoundPool.play((Integer) m_SoundPoolMap.get(index), streamVolume, streamVolume, 1, -1, 1f);
    }

    public void stopSound(int index) {
        m_SoundPool.stop((Integer) m_SoundPoolMap.get(index));
    }
}
