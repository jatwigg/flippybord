package com.xazux._2dlib.components;

import com.xazux._2dlib.MainThread;

import android.os.SystemClock;

public class GameTime 
{
	
	protected long m_lOffset;
	
	public GameTime() 
	{
		m_lOffset= SystemClock.elapsedRealtime();
	}

	public float getElapsedSeconds()
	{
		return (float)((SystemClock.elapsedRealtime() - m_lOffset) / 1000.0);
	}
	
	public float getElapsedMills()
	{
		return (float)(SystemClock.elapsedRealtime() - m_lOffset);
	}
	
	public static long getTimeSinceSystemBoot()
	{
		return SystemClock.elapsedRealtime();
	}

	/*
	 * ONLY CALL THIS FROM MAINTHREAD.JAVA
	 * */
	public void Clear()
	{
		m_lOffset = SystemClock.elapsedRealtime();
	}

}
