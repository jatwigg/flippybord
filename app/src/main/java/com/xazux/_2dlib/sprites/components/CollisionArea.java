package com.xazux._2dlib.sprites.components;

import com.xazux._2dlib.components.Vector2D;

public interface CollisionArea 
{
	public boolean containsPoint(int x, int y);
	public int getWidth();
	public int getHeight();
	public void offsetTo(int x, int y);
	public void offsetSoCenterIs(int x, int y);
	public void offsetBy(int x, int y);
	public int getCenterX();
	public int getCenterY();
	public int getLeft();
	public int getTop();
	public int getRight();
	public int getBottom();
	public float getRotationDegrees();
	public void setRotationDegrees(float rotation);
	public boolean intersects(CRect rect);
	public boolean intersects(CCircle circle);
	public Vector2D getOrigin();
	public void setOrigin(Vector2D origin);
	public boolean containsPoint(Vector2D position);
}
