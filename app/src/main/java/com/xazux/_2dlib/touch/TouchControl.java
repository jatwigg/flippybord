package com.xazux._2dlib.touch;

public class TouchControl
{
	protected Touchable m_touchable;
	protected int m_iPointerID;
	protected boolean m_bIsPressed;
	
	public TouchControl(Touchable touchableOwner) {
		m_touchable = touchableOwner;
		m_bIsPressed = false;
	}
	
	public void TouchStarted(TouchState event) {
		if (m_bIsPressed)
			return; // already pressed by another pointer

        if (m_touchable.getCollisionArea().containsPoint(event.X, event.Y)) {
            m_iPointerID = event.P_ID;
            m_bIsPressed = true;
            m_touchable.OnTouchStart(event);
        }
	}
	
	public void TouchOver(TouchState event) {
		if (!m_bIsPressed || m_iPointerID != event.P_ID)
			return; // not pressed by any pointer anyway
		m_iPointerID = -1;
		m_bIsPressed = false;
		m_touchable.OnTouchOver(event);
	}
	
	public void TouchCancel(TouchState event) {
        //TODO: do i need to send this if it's not pressed?
		m_iPointerID = -1;
		m_bIsPressed = false;
		m_touchable.OnTouchCancel();
	}
	
	public void TouchMove(TouchState event) {
		if (!m_bIsPressed)
			return; // not pressed

        if (event.P_ID == m_iPointerID) {
            m_touchable.OnTouchMove(event);
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
