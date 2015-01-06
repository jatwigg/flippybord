package com.xazux._2dlib.touch;

import java.util.Vector;

import android.view.MotionEvent;

public class MainTouchHandle
{
	protected Vector<TouchControl> m_touchControl = new Vector<TouchControl>();
	protected Object m_touchableLock = new Object();

	public boolean onTouchEvent(final MotionEvent event) 
	{
		synchronized (m_touchableLock)
		{
			synchronized (event)
			{
				switch (event.getAction() & MotionEvent.ACTION_MASK) 
				{
				case MotionEvent.ACTION_POINTER_DOWN: 
				case MotionEvent.ACTION_DOWN: 
				{
					for (int i=0; i< m_touchControl.size(); ++i)
						m_touchControl.get(i).TouchStarted(event);
					break;
				}

				case MotionEvent.ACTION_MOVE: 
				{
					for (int i=0; i< m_touchControl.size(); ++i)
						m_touchControl.get(i).TouchMove(event);
					break;
				} 

				case MotionEvent.ACTION_CANCEL: 
				{
					for (int i=0; i< m_touchControl.size(); ++i)
						m_touchControl.get(i).TouchCancel(event);
					break;
				}

				case MotionEvent.ACTION_UP: 
				case MotionEvent.ACTION_POINTER_UP: 
				{
					// Extract the index of the pointer that left the touch sensor
					final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) 
							>> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
							final int pointerId = event.getPointerId(pointerIndex);
							float x = event.getX(pointerIndex), y = event.getY(pointerIndex);
							
							for (int i=0; i< m_touchControl.size(); ++i)
								m_touchControl.get(i).TouchOver(event, pointerId, x, y);
							break;
				}
				}
			}
		}
		return true;
	}

	/**
	 * Register a touchable. This will get notified when a touch event happens.
	 * @param touchable
	 */
	public void RegisterTouchable(final Touchable touchable)
	{
		synchronized (m_touchableLock)
		{
			m_touchControl.add(new TouchControl(touchable));
		}
	}

	/**
	 * Unregister a touchable. Once unregistered it will not be notified about TouchEvents.
	 * @param touchable
	 */
	public void UnregisterTouchable(final Touchable touchable)
	{
		synchronized (m_touchableLock)
		{
			int i=0;
			while (i < m_touchControl.size())
			{
				if (m_touchControl.get(i).getOwner() == touchable)
				{
					m_touchControl.remove(i);
					break;
				}
				++i;
			}
		}
	}
}
