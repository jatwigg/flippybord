package com.xazux._2dlib.time;

import android.os.SystemClock;

public class GameTime implements IGameTime {
    protected long _lastTime;
    protected long _currentDeltaMills;
    protected float _currentDeltaSeconds;

    public GameTime() {
        _lastTime = SystemClock.elapsedRealtime();
        _currentDeltaMills = 0;
        _currentDeltaSeconds = 0;
    }

    public float getElapsedSeconds() {
        return _currentDeltaSeconds;
    }

    public long getElapsedMills() {
        return _currentDeltaMills;
    }

    //TODO: interface this without clear() then make everything beyond the MainThreads use interface not the class
    /*
     * ONLY CALL THIS FROM MAINTHREAD.JAVA
	 * */
    public void refresh() {
        long time = SystemClock.elapsedRealtime();
        _currentDeltaMills = time - _lastTime;
        _currentDeltaSeconds = _currentDeltaMills / 1000.0f;
        _lastTime = time;
    }

    public long getLastRecordedMills() {
        return _lastTime;
    }

    public long whatIsTimePlz() {
        return SystemClock.elapsedRealtime();
    }
}
