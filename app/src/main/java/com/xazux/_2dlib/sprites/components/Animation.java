package com.xazux._2dlib.sprites.components;

import android.graphics.Bitmap;

import com.xazux._2dlib.components.GameTime;

public class Animation extends Texture {

	public enum AnimationType 
	{
	    FORWARD_LOOP, 
	    BACKWARD_LOOP,
	    FORWARD_ONCE,
	    BACKWARD_ONCE,
	    FORWARD_BACKWARD_LOOP,
	    BACKWARD_FORWARD_LOOP,
	    FORWARD_BACKWARD_ONCE,
	    BACKWARD_FORWARD_ONCE
	}
	
	public enum AnimationState
	{
		Stopped,
		Playing,
		Paused,
		Finished
	}
	
	private int m_iTotalFrames, m_iCurrentFrameIndex, m_iStartFrame;
	private boolean m_bForwardDirection;
	private AnimationType m_animationType;	
	
	private float m_fInterval, m_fElapsed;
	
	private CRect m_rInnerRect;
	
	private AnimationState m_state;
	
	private Bitmap m_bitmapEntire; // used to hold a frame if rendering with a matrix
	
	public final static float DEFAULT_INTERVAL = 0.5f;
	
	public Animation(Bitmap bitmapImage, int totalFrames, AnimationType animationType, float interval) 
	{
		super(null);
		m_bitmapEntire = bitmapImage;
		// set how many frames in image
		m_iTotalFrames = totalFrames;
		if (m_iTotalFrames<1)
			throw new IllegalArgumentException("total frames must be greater than 0. Value is {" + m_iTotalFrames + "}");
		// set animation type
		m_animationType = animationType;
		// set interval
		m_fInterval = interval;
		if (m_fInterval < 0.0001f)
			throw new IllegalArgumentException("interval cannot be really, really small. Value is {" + m_fInterval + "}");
		// set inner rectangle 
		m_rInnerRect = new CRect(0, 0, m_bitmapEntire.getWidth()/m_iTotalFrames, m_bitmapEntire.getHeight());
		m_fElapsed = 0.0f;
		determineDirection();
		// recalculate the m_bitmap used
		recalculateBitmap();
		m_state = AnimationState.Playing;
	}

	private void determineDirection()
	{
		// determine direction
		if (m_animationType == AnimationType.FORWARD_LOOP ||
				m_animationType == AnimationType.FORWARD_ONCE ||
				m_animationType == AnimationType.FORWARD_BACKWARD_LOOP ||
				m_animationType == AnimationType.FORWARD_BACKWARD_ONCE)
		{
			m_bForwardDirection = true;
			// set start frame
			m_iStartFrame = 0;
		}
		else
		{
			m_bForwardDirection = false;
			// set start frame
			m_iStartFrame = m_iTotalFrames - 1;
		}
		m_iCurrentFrameIndex = m_iStartFrame;
	}
	
	@Override
	public void update(GameTime gameTime)
	{
		// return if state is not playing
		if (m_state == AnimationState.Finished ||
				m_state == AnimationState.Paused ||
				m_state == AnimationState.Stopped)
			return;
		// increment elapsed time and return if below interval
		if ((m_fElapsed += gameTime.getElapsedSeconds())<m_fInterval)
			return;
		// still here? then reset time and do animation step
		m_fElapsed = 0.0f;
		if (m_bForwardDirection)
			++m_iCurrentFrameIndex;
		else
			--m_iCurrentFrameIndex;
		// determine consequences of the step
		switch(m_animationType)
		{
		/* ** FORWARD ANIMATIONS ** */
		case FORWARD_LOOP: // if frame index is equal to total then reset to start
			if (m_iCurrentFrameIndex == m_iTotalFrames)
				m_iCurrentFrameIndex = 0;
			break;
		case FORWARD_BACKWARD_LOOP: // if moving forwards and index is equal to start, move backwards
			if (m_bForwardDirection && m_iCurrentFrameIndex == m_iTotalFrames)
			{
				m_iCurrentFrameIndex -= 2;
				m_bForwardDirection = false;
			}
			else if (!m_bForwardDirection && m_iCurrentFrameIndex == -1)
			{
				m_iCurrentFrameIndex += 2; // set frame to next image
				m_bForwardDirection = true;
			}
			break;
		case FORWARD_ONCE:
			if (m_iCurrentFrameIndex == m_iTotalFrames)
			{
				--m_iCurrentFrameIndex; // set frame index to the one used last
				FinishCycle(); // fini
			}
			break;
		case FORWARD_BACKWARD_ONCE:
			if (m_bForwardDirection && m_iCurrentFrameIndex == m_iTotalFrames)
			{
				m_iCurrentFrameIndex -= 2;
				m_bForwardDirection = false;
			}
			else if (!m_bForwardDirection && m_iCurrentFrameIndex == -1)
			{
				++m_iCurrentFrameIndex; // set frame index to last image
				FinishCycle(); // finish
			}
			break;
		/* ** BACKWARD ANIMATIONS ** */
		case BACKWARD_LOOP: // if frame index is equal to total then reset to start
			if (m_iCurrentFrameIndex == -1)
				m_iCurrentFrameIndex = m_iTotalFrames-1;
			break;
		case BACKWARD_FORWARD_LOOP: // if moving forwards and index is equal to start, move backwards
			if (!m_bForwardDirection && m_iCurrentFrameIndex == -1)
			{
				m_iCurrentFrameIndex += 2;
				m_bForwardDirection = true;
			}
			else if (m_bForwardDirection && m_iCurrentFrameIndex == m_iTotalFrames)
			{
				m_iCurrentFrameIndex -= 2; // set frame to next image
				m_bForwardDirection = false;
			}
			break;
		case BACKWARD_ONCE:
			if (m_iCurrentFrameIndex == -1)
			{
				++m_iCurrentFrameIndex; // set frame index to the one used last
				FinishCycle(); // finish
			}
			break;
		case BACKWARD_FORWARD_ONCE:
			if (!m_bForwardDirection && m_iCurrentFrameIndex == -1)
			{
				m_iCurrentFrameIndex += 2;
				m_bForwardDirection = true;
			}
			else if (m_bForwardDirection && m_iCurrentFrameIndex == m_iTotalFrames)
			{
				--m_iCurrentFrameIndex; // set frame index to last image
				FinishCycle(); // finish
			}
			break;
		}
		recalculateBitmap();
        //Log.d(getClass().getSimpleName(), "current animation frame:" + m_iCurrentFrameIndex);
    }
	
	protected void recalculateBitmap()
	{
		m_rInnerRect.offsetTo(m_rInnerRect.getWidth() * m_iCurrentFrameIndex, 0);
		m_bitmap = Bitmap.createBitmap(m_bitmapEntire, (int)m_rInnerRect.getLeft(), (int)m_rInnerRect.getTop(), (int)m_rInnerRect.getWidth(), (int)m_rInnerRect.getHeight());
	}
	
	public boolean HasFinishedCompleteCycle()
	{
		return (m_state==AnimationState.Finished?true:false);	
	}
	
	private void FinishCycle() 
	{
		m_state = AnimationState.Finished;
	}
	
	public void Stop()
	{
		m_state =  AnimationState.Stopped;
		m_fElapsed = 0.0f;
	}
	
	public void Pause()
	{
		m_state = AnimationState.Paused;
	}
	
	public void Play()
	{
		if (m_state == AnimationState.Paused)
		{
			m_state = AnimationState.Playing;
		}
		else if (m_state == AnimationState.Stopped || m_state == AnimationState.Finished)
		{
			determineDirection();
			
			m_state = AnimationState.Playing;
		}
	}

	public void ChangeType(AnimationType toType)
	{
		m_animationType = toType;
		Stop();
		determineDirection();
		recalculateBitmap();
		m_state = AnimationState.Playing;
	}
}