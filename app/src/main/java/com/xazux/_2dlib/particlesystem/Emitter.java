package com.xazux._2dlib.particlesystem;

import java.util.ArrayList;

import com.xazux._2dlib.JMath;
import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.components.TimeElapser;
import com.xazux._2dlib.components.Vector2D;
import com.xazux._2dlib.sprites.components.CCircle;
import com.xazux._2dlib.sprites.components.CollisionArea;
import com.xazux._2dlib.sprites.components.Texture;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.FloatMath;

public class Emitter
{
	protected final static float DEFAULT_PARTICLE_FREQ = 0.04f, DEFAULT_PARTICLE_LIFE = 2.0f,
			DEFAULT_MAX_VELOCITY_PER_SECOND = 10.0f, DEFAULT_MIN_VELOCITY_PER_SECOND = 5.0f,
			DEGREES_IN_CIRCLE = 360.0f;
	
	protected Texture m_texture;
	protected ArrayList<Particle> m_particleList;
	protected TimeElapser m_addParticleTimer;
	protected Vector2D m_vPosition;
	protected Paint m_paint;
	protected boolean m_bIsEnabled;
	protected float m_fLifeOfParticle, m_fMinVelocity, m_fMaxVelocity;
	
	public Emitter(Texture particle, Vector2D position)
	{
		this(particle, position, new Paint());
	}
	
	public Emitter(Texture particle, Vector2D position, Paint paint)
	{
		this(particle, position, paint, DEFAULT_PARTICLE_FREQ);
	}
	
	public Emitter(Texture particle, Vector2D position, Paint paint, float particlefrequency)
	{
		this(particle, position, paint, DEFAULT_PARTICLE_FREQ, DEFAULT_PARTICLE_LIFE);
	}
	
	public Emitter(Texture particle, Vector2D position, Paint paint, float particlefrequency, float particleLifeSpan)
	{
		this(particle, position, paint, particlefrequency, particleLifeSpan, DEFAULT_MIN_VELOCITY_PER_SECOND, DEFAULT_MAX_VELOCITY_PER_SECOND);
	}
	
	public Emitter(Texture particle, Vector2D position, Paint paint, float particlefrequency, float particleLifeSpan, float minVelocity, float maxVelocity)
	{
		m_texture = particle;
		m_particleList = new ArrayList<Particle>();
		m_addParticleTimer = new TimeElapser(particlefrequency);
		m_addParticleTimer.Start();
		m_vPosition = position;
		m_paint = paint;
		m_bIsEnabled = true;
		m_fLifeOfParticle = particleLifeSpan;
		m_fMinVelocity = minVelocity;
		m_fMaxVelocity = maxVelocity;
	}
	
	public Emitter Clone(Vector2D position)
	{
		return new Emitter(m_texture, position, m_paint, m_addParticleTimer.getTimeSpecified());
	}

	public void render(Canvas canvas)
	{
		for(int i =0;i<m_particleList.size();++i)
			m_texture.render(canvas, m_particleList.get(i).getCArea(), m_paint);
	}
	
	public void update(GameTime gameTime)
	{
		updateParticlesInList(gameTime);
		if (m_bIsEnabled && m_addParticleTimer.UpdateAndHasTimeElapsed(gameTime))
			addParticle();
	}

	protected void addParticle()
	{
		CollisionArea c = new CCircle(m_vPosition.getRoundedX(), m_vPosition.getRoundedY(), m_texture.getWidth());
		m_particleList.add(new Particle(c, generateVelocity(), m_fLifeOfParticle));
		m_addParticleTimer.ResetAndStart();
	}

	protected Vector2D generateVelocity()
	{
		float x, y, radians, length;
		radians = (float)Math.toRadians(DEGREES_IN_CIRCLE * JMath.GetRandom().nextFloat());
		length = ((m_fMaxVelocity - m_fMinVelocity) * JMath.GetRandom().nextFloat()) + m_fMinVelocity;
		x = FloatMath.cos(radians) * length;
		y = FloatMath.sin(radians) * length;
		Vector2D velocity = new Vector2D(x, y);
		return velocity;
	}

	protected void updateParticlesInList(GameTime gameTime)
	{
		for(int i =0;i<m_particleList.size();++i)
			if (m_particleList.get(i).update(gameTime))
				m_particleList.remove(i--);
	}

	public int particleCount()
	{
		return m_particleList.size();
	}

	public void disable()
	{
		m_bIsEnabled = false;
	}
	
	public void enable()
	{
		m_bIsEnabled = true;
	}
	
	public boolean isEnabled()
	{
		return m_bIsEnabled;
	}
}
