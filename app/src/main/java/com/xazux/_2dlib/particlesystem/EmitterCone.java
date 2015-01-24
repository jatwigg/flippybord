package com.xazux._2dlib.particlesystem;

import android.graphics.Paint;
import android.util.FloatMath;

import com.xazux._2dlib.JMath;
import com.xazux._2dlib.sprites.components.Texture;

public class EmitterCone extends Emitter {
    protected float m_fLowerCone, m_fUpperCone, m_fConeDifference;

    public EmitterCone(Texture particle, Vector2D position, float lowerAngleDegrees, float upperAngleDegrees) {
        this(particle, position, new Paint(), DEFAULT_PARTICLE_FREQ, lowerAngleDegrees, upperAngleDegrees);
    }

    public EmitterCone(Texture particle, Vector2D position, Paint paint, float lowerAngleDegrees, float upperAngleDegrees) {
        this(particle, position, paint, DEFAULT_PARTICLE_FREQ, lowerAngleDegrees, upperAngleDegrees);
    }

    public EmitterCone(Texture particle, Vector2D position, Paint paint, float particleFreq, float lowerAngleDegrees,
                       float upperAngleDegrees) {
        this(particle, position, paint, DEFAULT_PARTICLE_FREQ, DEFAULT_PARTICLE_LIFE, lowerAngleDegrees, upperAngleDegrees);
    }

    public EmitterCone(Texture particle, Vector2D position, Paint paint, float particleFreq, float particleLife, float lowerAngleDegrees,
                       float upperAngleDegrees) {
        super(particle, position, paint, particleFreq);
        m_fLowerCone = lowerAngleDegrees;
        m_fUpperCone = upperAngleDegrees;
        m_fConeDifference = m_fUpperCone - m_fLowerCone;
    }

    @Override
    public Emitter Clone(Vector2D position) {
        return new EmitterCone(m_texture, position, m_paint, m_fLowerCone, m_fUpperCone, m_addParticleTimer.getTimeSpecified());
    }

    @Override
    protected Vector2D generateVelocity() {
        float x, y, radians, length;
        radians = (float) Math.toRadians((m_fConeDifference * JMath.GetRandom().nextFloat()) + m_fLowerCone);
        length = ((DEFAULT_MAX_VELOCITY_PER_SECOND - DEFAULT_MIN_VELOCITY_PER_SECOND) * JMath.GetRandom().nextFloat()) + DEFAULT_MIN_VELOCITY_PER_SECOND;
        x = FloatMath.cos(radians) * length;
        y = FloatMath.sin(radians) * length;
        Vector2D velocity = new Vector2D(x, y);
        return velocity;
    }
}
