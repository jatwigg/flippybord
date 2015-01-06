package com.xazux._2dlib;

import java.util.Random;

public final class JMath
{
	private static final Random m_random = new Random();
	public static final float PI = 3.141592654f;
	/**
     * Get a Random instance.
     * @return
     */
	public static Random GetRandom()
	{
		return m_random;
	}
	
	public static float Clamp(float value, float min, float max)
	{
		return (value<min?min:(value>max?max:value));
	}
	
	public static int Clamp(int value, int min, int max)
	{
		return (value<min?min:(value>max?max:value));
	}
	
	public static boolean LineSegmentsCollide(int a1x, int a1y, int a2x, int a2y,
			int b1x, int b1y, int b2x, int b2y)
	{
		return false;//TODO
	}
	
	public static float RadianToDegree(float radian)
	{
		return radian * (180.0f / PI);
	}
	
	public static float DegreeToRadian(float degree)
	{
		return degree * (PI / 180.0f);
	}
	
	public static float AddPositiveToReachAngleDeg(float angleToMove, float desiredAngle)
	{
		if (desiredAngle < angleToMove)
		{
			if (((desiredAngle + 360.0f) - angleToMove) < (angleToMove - desiredAngle))
				return (desiredAngle + 360.0f) - angleToMove;
			else
				return -(angleToMove - desiredAngle);
		}
		else
		{
			if ((desiredAngle - angleToMove) < ((angleToMove + 360.0f) - desiredAngle))
				return desiredAngle - angleToMove;
			else
				return -((angleToMove + 360.0f) - desiredAngle);
		}
	}
}
