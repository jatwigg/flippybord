package com.xazux._2dlib.sprites.components;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.xazux._2dlib.JMath;
import com.xazux._2dlib.particlesystem.Vector2D;

public class CCircle implements CollisionArea {
    protected float m_fLeft, m_fTop, m_fRight, m_fBottom, m_fCenterX, m_fCenterY, m_fRadius;
    protected float m_fRotation;
    protected Vector2D m_vOrigin;

    public CCircle(float centerX, float centerY, float radius) {
        m_fCenterX = centerX;
        m_fCenterY = centerY;
        m_vOrigin = new Vector2D(0.5f, 0.5f);
        m_fRadius = radius;
        m_fRotation = 0.0f;
        updateSides();
    }

    private void updateSides() {
        m_fLeft = m_fCenterX - m_fRadius;
        m_fTop = m_fCenterY - m_fRadius;
        m_fRight = m_fCenterX + m_fRadius;
        m_fBottom = m_fCenterY + m_fRadius;
    }

    public boolean containsPoint(float x, float y) {
        Vector2D v = new Vector2D(x - m_fCenterX, y - m_fCenterY);

        if (v.length() < m_fRadius)
            return true;

        return false;
    }

    public boolean containsPoint(Vector2D position) {
        return containsPoint(position.getRoundedX(), position.getRoundedY());
    }

    @Override
    public void render(Canvas canvas, Paint paint) {
        canvas.drawCircle(m_fCenterX, m_fCenterY, m_fRadius, paint);
    }

    public float getWidth() {
        return m_fRadius * 2;
    }

    public float getHeight() {
        return getWidth();
    }

    public float getRotationDegrees() {
        return m_fRotation;
    }

    public void setRotationDegrees(float rotation) {
        if (rotation > 360.0f)
            m_fRotation = rotation % 360.0f;
        else if (rotation < 0.0f)
            m_fRotation = (rotation % -360.0f) + 360.0f;
        else
            m_fRotation = rotation;
    }

    public void offsetTo(float x, float y) {
        m_fCenterX = x + m_fRadius;
        m_fCenterY = y + m_fRadius;
        updateSides();
    }

    public void offsetBy(float x, float y) {
        m_fLeft += x;
        m_fRight += x;
        m_fCenterX += x;
        m_fTop += y;
        m_fBottom += y;
        m_fCenterY += y;
    }

    public void offsetSoCenterIs(float x, float y) {
        m_fCenterX = x;
        m_fCenterY = y;
        updateSides();
    }

    public float getCenterX() {
        return m_fCenterX;
    }

    public float getCenterY() {
        return m_fCenterY;
    }

    public float getLeft() {
        return m_fLeft;
    }

    public float getTop() {
        return m_fTop;
    }

    public float getRight() {
        return m_fRight;
    }

    public float getBottom() {
        return m_fBottom;
    }

    public float getRadius() {
        return m_fRadius;
    }

    public boolean intersects(CRect rect) {
        // Find the closest point to the circle within the rectangle
        float closestX = JMath.Clamp(m_fCenterX, rect.getLeft(), rect.getRight());
        float closestY = JMath.Clamp(m_fCenterY, rect.getTop(), rect.getBottom());

        // Calculate the distance between the circle's center and this closest point
        float distanceX = m_fCenterX - closestX;
        float distanceY = m_fCenterY - closestY;

        // If the distance is less than the circle's radius, an intersection occurs
        float distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
        return (distanceSquared < (m_fRadius * m_fRadius));
    }

    public boolean intersects(CCircle circle) {
        Vector2D distance = new Vector2D(m_fCenterX - circle.m_fCenterX, m_fCenterY - circle.m_fCenterY);
        if (distance.length() <= m_fRadius + circle.m_fRadius) {
            return true;
        }
        return false;
    }

    public Vector2D getOrigin() {
        return m_vOrigin;
    }

    public void setOrigin(Vector2D origin) {
        m_vOrigin = origin;
    }

}
