package com.xazux._2dlib.components;

import android.os.SystemClock;
import android.util.Log;

public class GameTime 
{
	protected long _lastTime;
    protected long _currentDeltaMills;
    protected float _currentDeltaSeconds;
	//protected long m_lOffset;
	
	public GameTime() 
	{
		_lastTime = SystemClock.elapsedRealtime();
        _currentDeltaMills = 0;
        _currentDeltaSeconds = 0;
	}

	public float getElapsedSeconds()
	{
		//return (float)((SystemClock.elapsedRealtime() - m_lOffset) / 1000.0);
        return _currentDeltaSeconds;
	}
	
	public long getElapsedMills()
	{
		//return (float)(SystemClock.elapsedRealtime() - m_lOffset);
        return _currentDeltaMills;
	}
	
	/*public static long getTimeSinceSystemBoot()
	{
		return SystemClock.elapsedRealtime();
	}*/

	/*
	 * ONLY CALL THIS FROM MAINTHREAD.JAVA
	 * */
	public void Clear()
	{
        long time = SystemClock.elapsedRealtime();
        _currentDeltaMills = time - _lastTime;
        _currentDeltaSeconds = _currentDeltaMills / 1000.0f;
        _lastTime = time;
	}

    public long getLastRecordedMills() {
        return _lastTime;
    }

    public long whatIsTimePlz()
    {
        return SystemClock.elapsedRealtime();
    }
}
