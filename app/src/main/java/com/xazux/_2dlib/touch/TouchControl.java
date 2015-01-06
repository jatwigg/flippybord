package com.xazux._2dlib.touch;

import android.view.MotionEvent;

public class TouchControl
{
	protected Touchable m_touchable;
	protected int m_iPointerID;
	protected boolean m_bIsPressed;
	
	public TouchControl(Touchable touchableOwner)
	{
		m_touchable = touchableOwner;
		m_bIsPressed = false;
	}
	
	public void TouchStarted(MotionEvent event)
	{
		if (m_bIsPressed)
			return; // already pressed by another pointer
		
		final int pointerCount = event.getPointerCount();
	    for (int p = 0; p < pointerCount; p++) 
	    {
	    	 if (m_touchable.getCollisionArea().containsPoint((int)event.getX(p),
	    			 (int)event.getY(p)))
	    	 {
	    		 m_iPointerID = event.getPointerId(p);
	    		 m_bIsPressed = true;
	    		 m_touchable.OnTouchStart(event, event.getX(p), event.getY(p));
	    	 }
	     }
	}
	
	public void TouchOver(MotionEvent event, int pointerID, float x, float y)
	{
		if (!m_bIsPressed || m_iPointerID != pointerID)
			return; // not pressed by any pointer anyway
		m_iPointerID = -1;
		m_bIsPressed = false;
		m_touchable.OnTouchOver(event, x, y);
	}
	
	public void TouchCancel(MotionEvent event)
	{
		m_iPointerID = -1;
		m_bIsPressed = false;
		m_touchable.OnTouchCancel(event);
	}
	
	public void TouchMove(MotionEvent event)
	{
		if (!m_bIsPressed)
			return; // not pressed
		
		final int pointerCount = event.getPointerCount();
	    for (int p = 0; p < pointerCount; p++) 
	    {
	    	 if (event.getPointerId(p) == m_iPointerID)
	    		 m_touchable.OnTouchMove(event, event.getX(p), event.getY(p));
	    }
	}
	
	public boolean isCurrentlyPressed()
	{
		return m_bIsPressed;
	}
	
	public int getPointerID()
	{
		return m_iPointerID;
	}
	
	public Touchable getOwner()
	{
		return m_touchable;
	}
}
