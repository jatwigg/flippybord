package com.xazux._2dlib.components;

import android.content.res.Resources;
import android.graphics.Canvas;

public class StateTransition extends TimeElapser
{
	protected boolean m_bComplete;
	
	public StateTransition(float durationInSeconds)
	{
		super(durationInSeconds);
	}

	@Override
	public final void Start()
	{
		ResetAndStart();
	}
	
	@Override
	public void ResetAndStart()
	{
		m_bComplete = false;
		onStart();
		super.ResetAndStart();
	}
	
	
	/**
	 * override this and call it to load resources into this class.
	 * @param resources
	 */
	public void LoadContent(Resources resources)
	{
		
	}
	
	/**
	 * Update the transition - calls onUpdate.
	 * @param gameTime
	 * @return true if transition is running, else false.
	 */
	public final boolean Update(GameTime gameTime)
	{
		if (!m_bHasStarted || m_bComplete)
			return false;
		onUpdate(gameTime);
		if (UpdateAndHasTimeElapsed(gameTime) && !m_bComplete) // ensures only run once
		{
			m_bComplete = true;
			onTransitionFinished();
		}
		return true;
	}
	
	/**
	 * Call this in your main render loop if you are planning on overriding onRender.
	 * @param canvas
	 */
	public final void Render(Canvas canvas)
	{
		if (!m_bHasStarted || m_bComplete)
			return;
		onRender(canvas);
	}

	/**
	 * Override this to run some code when the transition starts.
	 */
	public void onStart()
	{
		
	}
	
	/**
	 * Override this to run some update code.
	 */
	public void onUpdate(GameTime gameTime)
	{
		
	}
	
	/**
	 * Override this to do some render code.
	 * @param canvas
	 */
	public void onRender(Canvas canvas)
	{
		
	}
	
	/**
	 * Override this to implement code when the transition has finished, eg to change gamestate.
	 */
	public void onTransitionFinished()
	{
		
	}

	public boolean hasCompleted()
	{
		return m_bComplete;
	}

	public boolean hasBeenStarted()
	{
		return m_bHasStarted;
	}

	public void Reset()
	{
		m_bComplete = false;
		m_fElapsedTime = 0.0f;
		m_bHasStarted = false;
	}
}
