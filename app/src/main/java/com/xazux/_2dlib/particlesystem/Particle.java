package com.xazux._2dlib.particlesystem;

import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.components.TimeElapser;
import com.xazux._2dlib.components.Vector2D;
import com.xazux._2dlib.sprites.components.CollisionArea;

 /**
 * Stores location and velocity of a particle
 * @author Josh
 *
 */
public class Particle
{
	protected Vector2D m_vVelocityPS;
	protected CollisionArea m_areaOnScreen;
	protected TimeElapser m_lifeSpanElapser;
	protected float m_fPositionX, m_fPositionY;
	
	/**
	 * Construct a particle
	 * @param position location on screen
	 * @param velocityPerSecond distance it will travel in 1 second
	 */
	public Particle(CollisionArea areaOnScreen, Vector2D velocityPerSecond, float lifeSpanInSeconds)
	{
		m_areaOnScreen = areaOnScreen;
		m_vVelocityPS = velocityPerSecond;
		m_fPositionX = areaOnScreen.getCenterX();
		m_fPositionY = areaOnScreen.getCenterY();
		m_lifeSpanElapser = new TimeElapser(lifeSpanInSeconds);
		m_lifeSpanElapser.Start();
	}
	
	/**
	 * update the particle
	 * @return true if it has expired and needs removing.
	 */
	public boolean update(GameTime gameTime)
	{
		m_fPositionX += m_vVelocityPS.getX() * gameTime.getElapsedSeconds();
		m_fPositionY += m_vVelocityPS.getY() * gameTime.getElapsedSeconds();
		m_areaOnScreen.offsetTo(m_fPositionX, m_fPositionY);
		return m_lifeSpanElapser.UpdateAndHasTimeElapsed(gameTime);
	}

	public CollisionArea getCArea()
	{
		return m_areaOnScreen;
	}
}
