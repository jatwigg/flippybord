package com.xazux._2dlib.particlesystem;

import java.util.ArrayList;

import android.graphics.Canvas;

import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.components.TimeElapser;
import com.xazux._2dlib.components.Vector2D;

public class ParticleEmitterManager
{
	/**
	 * Contains emitter and default life span for emitter.
	 * @author Josh
	 *
	 */
	class EmitterElement
	{
		public EmitterElement(float defaultLifespan, Emitter emitter)
		{
			DefaultLifespan = defaultLifespan;
			EmitterToClone = emitter;
		}
		public float DefaultLifespan;
		public Emitter EmitterToClone;
	}
	
	/**
	 * Represents emitter instance that will expire.
	 * @author Josh
	 *
	 */
	class EmitterLifeTimer extends TimeElapser
	{
		public Emitter EmitterInstance;
		public EmitterLifeTimer(float duration, Emitter emitter)
		{
			super(duration);
			EmitterInstance = emitter;
		}
		@Override
		public boolean UpdateAndHasTimeElapsed(GameTime gameTime)
		{
			// if time has expired wait till all particles have been removed before returning true
			if (super.UpdateAndHasTimeElapsed(gameTime))
			{
				EmitterInstance.disable();
				if (EmitterInstance.particleCount() == 0)
					return true;
			}
			EmitterInstance.update(gameTime);
			return false;
		}
	}
	
	protected ArrayList<EmitterElement> m_emittersAvailable;
	protected ArrayList<EmitterLifeTimer> m_emittersAlive;
	
	public ParticleEmitterManager()
	{
		m_emittersAvailable = new ArrayList<EmitterElement>();
		m_emittersAlive = new ArrayList<ParticleEmitterManager.EmitterLifeTimer>();
	}
	
	/**
	 * Register an instance of an emitter that will be retained to clone later.
	 * @param instanceToClone the instance to retain for cloning.
	 * @param lifespanSeconds how long the clones of this emitter will exist for.
	 * @return
	 */
	public int RegisterEmitterInstance(Emitter instanceToClone, float lifespanSeconds)
	{
		m_emittersAvailable.add(new EmitterElement(lifespanSeconds, instanceToClone));
		return m_emittersAvailable.size() - 1;
	}
	
	/**
	 * Create a new emitter and add it to the emittersAlive list. You must pass in an int that was returned by RegisterEmitterInstance
	 * @param typeToClone the emitter to clone's unique ID
	 * @param position the position on screen to place the emitter object
	 */
	public void CreateEmitter(int typeToClone, Vector2D position)
	{
		EmitterElement element = m_emittersAvailable.get(typeToClone);
		EmitterLifeTimer timer = new EmitterLifeTimer(element.DefaultLifespan,
				element.EmitterToClone.Clone(position));
		m_emittersAlive.add(timer);
		timer.Start();
	}
	
	/**
	 * Create a new emitter and add it to the emittersAlive list. You must pass in an int that was returned by RegisterEmitterInstance
	 * @param typeToClone the emitter to clone's unique ID
	 * @param lifespanSeconds a duration in seconds that the emitter will be alive
	 * @param position position on screen to place the emitter object
	 */
	public void CreateEmitter(int typeToClone, float lifespanSeconds, Vector2D position)
	{
		EmitterElement element = m_emittersAvailable.get(typeToClone);
		EmitterLifeTimer timer = new EmitterLifeTimer(lifespanSeconds,
				element.EmitterToClone.Clone(position));
		m_emittersAlive.add(timer);
		timer.Start();
	}
	
	/**
	 * update emitters
	 * @param gameTime
	 */
	public void Update(GameTime gameTime)
	{
		for (int i=0; i<m_emittersAlive.size(); ++i)
			if (m_emittersAlive.get(i).UpdateAndHasTimeElapsed(gameTime))
				m_emittersAlive.remove(i--);
	}
	
	/*
	 * render emitters
	 */
	public void render(Canvas canvas)
	{
		for (EmitterLifeTimer emitter : m_emittersAlive)
			emitter.EmitterInstance.render(canvas);
	}

	public void clearAllAlive()
	{
		m_emittersAlive.clear();
	}
}
