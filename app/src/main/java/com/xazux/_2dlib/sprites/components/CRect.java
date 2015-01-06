package com.xazux._2dlib.sprites.components;

import com.xazux._2dlib.JMath;
import com.xazux._2dlib.components.Vector2D;

import android.graphics.Rect;

public class CRect implements CollisionArea 
{
	protected int m_iLeft, m_iTop, m_iRight, m_iBottom, m_iWidth, m_iHeight, m_iCenterX, m_iCenterY;
	protected float m_fRotation;
	protected Vector2D m_vOrigin;
	
	public CRect(int left, int top, int right, int bottom)
	{
		m_iLeft = left;
		m_iTop = top;
		m_iRight = right;
		m_iBottom = bottom;
		m_iWidth = m_iRight - m_iLeft;
		m_iHeight = m_iBottom - m_iTop;
		m_fRotation = 0.0f;
		m_vOrigin = new Vector2D(0.5f,0.5f);
		setCenter();
	}
	
	public CRect(Rect screenRect)
	{
		this(screenRect.left,screenRect.top,screenRect.right,screenRect.bottom);
	}

	public CRect(CRect cloneThis)
	{
		m_iLeft = cloneThis.m_iLeft;
		m_iTop = cloneThis.m_iTop;
		m_iRight = cloneThis.m_iRight;
		m_iBottom = cloneThis.m_iBottom;
		m_iWidth =  cloneThis.m_iWidth;
		m_iHeight = cloneThis.m_iHeight;
		m_fRotation = cloneThis.m_fRotation;
		m_vOrigin = new Vector2D(cloneThis.m_vOrigin.getX(),cloneThis.m_vOrigin.getY());
		setCenter();
	}

	public boolean containsPoint(int x, int y) 
	{
		//if (m_fRotation < 0.001f)
		return (x>m_iLeft && x< m_iRight && y > m_iTop && y < m_iBottom)?true:false;
		//else
		//{
			// check if lines intersect
			
		//}
	}

	public boolean containsPoint(Vector2D position)
	{
		return containsPoint(position.getRoundedX(), position.getRoundedY());
	}
	
	public int getWidth() 
	{
		return m_iWidth;
	}

	public int getHeight() 
	{
		return m_iHeight;
	}

	public void offsetTo(int x, int y)
	{
		m_iLeft = x;
		m_iRight = x + m_iWidth;
		m_iTop = y;
		m_iBottom = y + m_iHeight;
		setCenter();
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

	private void setCenter()
	{
		m_iCenterX = m_iLeft + (m_iWidth/2);
		m_iCenterY = m_iTop + (m_iHeight/2);
	}

	public void offsetSoCenterIs(int x, int y)
	{
		m_iLeft = x - (m_iWidth/2);
		m_iRight = x + (m_iWidth/2);
		m_iTop = y - (m_iHeight/2);
		m_iBottom = y +(m_iHeight/2);
		setCenter();
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

	public boolean intersects(CRect otherRect)
	{
		return !(otherRect.m_iRight < m_iLeft || otherRect.m_iLeft > m_iRight
				|| otherRect.m_iBottom < m_iTop || otherRect.m_iTop > m_iBottom);
	}

	public boolean intersects(CCircle circle)
	{
		// Find the closest point to the circle within the rectangle
		float closestX = JMath.Clamp(circle.m_iCenterX, m_iLeft, m_iRight);
		float closestY = JMath.Clamp(circle.m_iCenterY, m_iTop, m_iBottom);

		// Calculate the distance between the circle's center and this closest point
		float distanceX = circle.m_iCenterX - closestX;
		float distanceY = circle.m_iCenterY - closestY;

		// If the distance is less than the circle's radius, an intersection occurs
		float distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
		return (distanceSquared < (circle.m_iRadius * circle.m_iRadius));
	}

	public float getRotationDegrees()
	{
		return m_fRotation;
	}
	
	public void setRotationDegrees(float rotation)
	{
		m_fRotation = (rotation > 360.0f? rotation - 360.0f:rotation);
	}

	public Vector2D getOrigin()
	{
		return m_vOrigin;
	}

	public void setOrigin(Vector2D origin)
	{
		m_vOrigin = origin;
	}
	
	public CRect clone()
	{
		return new CRect(this);
	}

	public static Rect ConvertToRect(CollisionArea carea)
	{
		return new Rect(carea.getLeft(), carea.getTop(),
				carea.getRight(), carea.getBottom());
	}

	public static CRect CreateUsingWidthAndHeight(float left, float top, float width, float height)
	{
		return new CRect((int)(left + 0.5f), (int)(top + 0.5f), 
				(int)(left + width + 0.5f), (int)(top + height + 0.5f));
	}

	public static CRect ConvertFromCollisionArea(CollisionArea cArea)
	{
		return new CRect(cArea.getLeft(), cArea.getTop(), cArea.getRight(), cArea.getBottom());
	}
}
