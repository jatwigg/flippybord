package com.xazux._2dlib.sprites.components;

import com.xazux._2dlib.JMath;
import com.xazux._2dlib.components.Vector2D;

public class CCircle implements CollisionArea
{
	protected int m_iLeft, m_iTop, m_iRight, m_iBottom, m_iCenterX, m_iCenterY, m_iRadius;
	protected float m_fRotation;
	protected Vector2D m_vOrigin;
	
	public CCircle(int centerX, int centerY, int radius)
	{
		m_iCenterX = centerX;
		m_iCenterY = centerY;
		m_vOrigin = new Vector2D(0.5f,0.5f);
		m_iRadius = radius;
		m_fRotation = 0.0f;
		updateSides();
	}

	private void updateSides()
	{
		m_iLeft = m_iCenterX - m_iRadius;
		m_iTop = m_iCenterY - m_iRadius;
		m_iRight = m_iCenterX + m_iRadius;
		m_iBottom = m_iCenterY + m_iRadius;
	}
	
	public boolean containsPoint(int x, int y)
	{
		Vector2D v = new Vector2D(x-m_iCenterX, y-m_iCenterY);
		
		if (v.length() < m_iRadius)
			return true;
		
		return false;
	}
	
	public boolean containsPoint(Vector2D position)
	{
		return containsPoint(position.getRoundedX(), position.getRoundedY());
	}

	public int getWidth()
	{
		return m_iRadius*2;
	}

	public int getHeight()
	{
		return getWidth();
	}
	
	public float getRotationDegrees()
	{
		return m_fRotation;
	}
	
	public void setRotationDegrees(float rotation)
	{
		if (rotation > 360.0f)
			m_fRotation = rotation % 360.0f;
		else if (rotation < 0.0f)
			m_fRotation = (rotation % -360.0f) + 360.0f;
		else
			m_fRotation = rotation;
	}

	public void offsetTo(int x, int y)
	{
		m_iCenterX = x + m_iRadius;
		m_iCenterY = y + m_iRadius;
		updateSides();
	}
	
	public void offsetBy(int x, int y)
	{
		m_iLeft += x;
		m_iRight += x;
		m_iCenterX += x;
		m_iTop += y;
		m_iBottom += y;
		m_iCenterY += y;
	}

	public void offsetSoCenterIs(int x, int y)
	{
		m_iCenterX = x;
		m_iCenterY = y;
		updateSides();
	}

	public int getCenterX()
	{
		return m_iCenterX;
	}

	public int getCenterY()
	{
		return m_iCenterY;
	}

	public int getLeft()
	{
		return m_iLeft;
	}

	public int getTop()
	{
		return m_iTop;
	}

	public int getRight()
	{
		return m_iRight;
	}

	public int getBottom()
	{
		return m_iBottom;
	}

	public int getRadius()
	{
		return m_iRadius;
	}

	public boolean intersects(CRect rect)
	{
		// Find the closest point to the circle within the rectangle
		float closestX = JMath.Clamp(m_iCenterX, rect.getLeft(), rect.getRight());
		float closestY = JMath.Clamp(m_iCenterY, rect.getTop(), rect.getBottom());

		// Calculate the distance between the circle's center and this closest point
		float distanceX = m_iCenterX - closestX;
		float distanceY = m_iCenterY - closestY;

		// If the distance is less than the circle's radius, an intersection occurs
		float distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
		return (distanceSquared < (m_iRadius * m_iRadius));
	}

	public boolean intersects(CCircle circle)
	{
		Vector2D distance = new Vector2D(m_iCenterX - circle.m_iCenterX, m_iCenterY - circle.m_iCenterY);
		if (distance.length() <= m_iRadius + circle.m_iRadius)
		{
			return true;
		}
		return false;
	}

	public Vector2D getOrigin()
	{
		return m_vOrigin;
	}

	public void setOrigin(Vector2D origin)
	{
		m_vOrigin = origin;
	}

}
