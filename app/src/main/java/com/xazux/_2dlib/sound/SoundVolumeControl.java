package com.xazux._2dlib.sound;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Vector;

public class SoundVolumeControl extends BroadcastReceiver
{
	protected static Handler _Handler;
	protected AudioManager m_AudioManager;
	protected boolean m_bIsMute;
	protected int m_iMaxVolume;
	protected float m_fVolumePercentage;
	protected Vector<Runnable> m_onMediaChangeRunnables;
	private ContentObserver mSettingsContentObserver;
	private Context m_context;
	
	public SoundVolumeControl(Context context)
	{
		m_context = context;
		m_AudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		m_iMaxVolume = m_AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		m_bIsMute = false;
		determinePercentVolume();
		mSettingsContentObserver = new ContentObserver
				((_Handler==null?_Handler = new Handler(Looper.getMainLooper()):_Handler))
		{
			@Override
			public boolean deliverSelfNotifications() 
			{
			     return super.deliverSelfNotifications(); 
			}

			@Override
			public void onChange(boolean selfChange) 
			{
				Log.d("SoundVolumeControl", "Change detected. Volume is now " + m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
				
				if (m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0)
					m_bIsMute = true;
				else
					m_bIsMute = false;
				
				for (Runnable r: m_onMediaChangeRunnables)
					r.run();
			}
		}; 
		context.getContentResolver().registerContentObserver( 
		    android.provider.Settings.System.CONTENT_URI, true, 
		    mSettingsContentObserver );
		m_onMediaChangeRunnables = new Vector<Runnable>();
	}

	private void determinePercentVolume()
	{
		m_fVolumePercentage = (m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / 
				(float)m_iMaxVolume) * 100.0f;
	}
	
	public void setVolumePercentage(float percent)
	{
		if (percent < 0.0f || percent > 100.0f)
			return;
		m_fVolumePercentage = percent;
		int volume = (int)(((m_iMaxVolume / 100.0f) * m_fVolumePercentage) + 0.5f);
		m_AudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_VIBRATE);
	}
	
	public float getVolumePercentage()
	{
		return m_fVolumePercentage;
	}
	
	public void toggleMute()
	{
		m_bIsMute = !m_bIsMute;
		setMute(m_bIsMute);
	}
	
	public boolean isMute()
	{
		return m_bIsMute;
	}
	
	public void setMute(boolean b)
	{
		m_bIsMute = b;
		if (m_bIsMute)
		{
			determinePercentVolume();
			m_AudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_VIBRATE);
		}
		else
		{
			int volume = (int)(((m_iMaxVolume / 100.0f) * m_fVolumePercentage) + 0.5f);
			m_AudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_VIBRATE);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.d("SoundVolumeControl","received broadcast!");
		if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) 
		{
			Log.d("SoundVolumeControl","broadcast was media button! volume is now:" + m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
			
			if (m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0)
				m_bIsMute = true;
			else
				m_bIsMute = false;
			for (Runnable r: m_onMediaChangeRunnables)
				r.run();
        }
	}
	
	public void registerOnMediaVolumeChange(Runnable r)
	{
		m_onMediaChangeRunnables.add(r);
	}
	
	public void unregisterOnMediaVolumeChange(Runnable r)
	{
		m_onMediaChangeRunnables.remove(r);
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		if (m_AudioManager != null)
		{
			try
			{
				m_context.getContentResolver().unregisterContentObserver(
					    mSettingsContentObserver);
			}
			catch (Exception e)
			{
				Log.e("SoundVolumeControl", "could not unregister content observer", e);
			}
		}
		super.finalize();
	}
}
