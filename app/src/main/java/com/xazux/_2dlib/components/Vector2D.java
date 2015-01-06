package com.xazux._2dlib.components;

import com.xazux._2dlib.JMath;

import android.util.FloatMath;

public class Vector2D
{
	private static final float RADIANS_IN_CIRCLE = 6.283185f;
	private float m_flX, m_flY;
	
	public Vector2D(float x, float y)
	{
		m_flX = x;
		m_flY = y;
	}
	
	public float getX()
	{
		return m_flX;
	}
	
	public float getY()
	{
		return m_flY;
	}
	
	public Vector2D getCrossOrthogonal()
	{
		return new Vector2D(m_flY, -m_flX);
	}
	
	public float getCross(Vector2D other)
	{
		//CrossProductAnalog1(U,V)=(U.x*V.y-U.y*V.x)
		return (m_flX * other.m_flY) - (m_flY * other.m_flX);
	}
	
	public int getRoundedX()
	{
		return (int)(m_flX + 0.5f);
	}
	
	public int getRoundedY()
	{
		return (int)(m_flY + 0.5f);
	}
	
	public Vector2D getAdd(Vector2D v)
	{
		return new Vector2D(v.m_flX + m_flX, v.m_flY + m_flY);
	}

	public Vector2D getSubtract(Vector2D v)
	{
		return new Vector2D(m_flX - v.m_flX, m_flY - v.m_flY);
	}

	public Vector2D getScale(float value)
	{
		return new Vector2D(m_flX * value, m_flY * value);
	}

	public float dot(Vector2D v)
	{
		return m_flX * v.m_flX + m_flY * v.m_flY;
	}

	public float length()
	{
		return (float)FloatMath.sqrt((m_flX*m_flX) + (m_flY*m_flY) );
	}
	
	public Vector2D getNormalise()
	{
		float len = length();
		return new Vector2D(m_flX / len, m_flY / len);
	}
	
	public float angleInRad()
	{
		float angle = (float)Math.atan2(m_flY, m_flX);
		if (angle < 0.0f)
			angle += RADIANS_IN_CIRCLE;
		return angle;
	}
	
	public float angleInDeg()
	{
		return (float)Math.toDegrees(angleInRad());
	}

	public void set(float x, float y)
	{
		m_flX = x;
		m_flY = y;
	}
	
	public void setX(float x)
	{
		m_flX = x;
	}
	
	public void setY(float y)
	{
		m_flY = y;
	}

	public void add(Vector2D addThis)
	{
		m_flX += addThis.m_flX;
		m_flY += addThis.m_flY;
	}

	public static Vector2D Zero()
	{
		return new Vector2D(0, 0);
	}

	public void setAngleRadian(float radian)
	{
		float length = length();
		m_flX = FloatMath.cos(radian) * length;
		m_flY = FloatMath.sin(radian) * length;
	}
	
	public void setAngleDeg(float degree)
	{
		setAngleRadian(JMath.DegreeToRadian(degree));
	}
	
}
