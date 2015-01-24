package com.xazux._2dlib.sprites.components;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.xazux._2dlib.particlesystem.Vector2D;

public interface CollisionArea {
    public boolean containsPoint(float x, float y);

    public float getWidth();

    public float getHeight();

    public void offsetTo(float x, float y);

    public void offsetSoCenterIs(float x, float y);

    public void offsetBy(float x, float y);

    public float getCenterX();

    public float getCenterY();

    public float getLeft();

    public float getTop();

    public float getRight();

    public float getBottom();

    public float getRotationDegrees();

    public void setRotationDegrees(float rotation);

    public boolean intersects(CRect rect);

    public boolean intersects(CCircle circle);

    public Vector2D getOrigin();

    public void setOrigin(Vector2D origin);

    public boolean containsPoint(Vector2D position);

    void render(Canvas canvas, Paint paint);
}
