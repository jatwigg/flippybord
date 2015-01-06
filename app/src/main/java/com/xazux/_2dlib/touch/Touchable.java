package com.xazux._2dlib.touch;

import android.view.MotionEvent;

import com.xazux._2dlib.sprites.components.CollisionArea;

public interface Touchable
{
	public CollisionArea getCollisionArea();
	public void OnTouchMove(MotionEvent event, float x, float y);
	public void OnTouchStart(MotionEvent event, float x, float y);
	public void OnTouchOver(MotionEvent event, float x, float y);
	public void OnTouchCancel(MotionEvent event);
}
