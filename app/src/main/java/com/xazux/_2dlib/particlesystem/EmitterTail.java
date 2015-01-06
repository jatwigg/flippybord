package com.xazux._2dlib.particlesystem;

import android.graphics.Paint;
import android.util.FloatMath;

import com.xazux._2dlib.JMath;
import com.xazux._2dlib.components.Vector2D;
import com.xazux._2dlib.sprites.components.Texture;

public class EmitterTail extends Emitter
{
	protected final float MAX_VELOCITY_PS = 4.0f, MIN_VELOCITY_PS = 2.0f;
	protected EmitterTailOwner m_owner;
	
	public EmitterTail(Texture particle, EmitterTailOwner owner, float particleFreqfloat, float particleLifeSpan)
	{
		super(particle, Vector2D.Zero(), new Paint(), particleFreqfloat, particleLifeSpan);
		m_owner = owner;
	}
	
	@Override
	protected void addParticle()
	{
		m_vPosition = m_owner.getPosition();
		super.addParticle();
	}

	@Override
	protected Vector2D generateVelocity()
	{
		float x, y, radians = m_owner.getVelocity().getScale(-1.0f).angleInRad(), length;
		length = ((MAX_VELOCITY_PS - MIN_VELOCITY_PS) * JMath.GetRandom().nextFloat()) + MIN_VELOCITY_PS;
		x = FloatMath.cos(radians) * length;
		y = FloatMath.sin(radians) * length;
		Vector2D velocity = new Vector2D(x, y);
		return velocity;
	}

	public EmitterTail Clone(EmitterTailOwner owner)
	{
		return new EmitterTail(m_texture, owner, m_addParticleTimer.getTimeSpecified(), m_fLifeOfParticle);
	}
}
