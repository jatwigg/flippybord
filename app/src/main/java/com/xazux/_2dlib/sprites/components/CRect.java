package com.xazux._2dlib.sprites.components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.xazux._2dlib.JMath;
import com.xazux._2dlib.particlesystem.Vector2D;

public class CRect implements CollisionArea {
    protected float m_fLeft, m_fTop, m_fRight, m_fBottom, m_fWidth, m_fHeight, m_fCenterX, m_fCenterY;
    protected float m_fRotation;
    protected Vector2D m_vOrigin;

    public CRect(float left, float top, float right, float bottom) {
        m_fLeft = left;
        m_fTop = top;
        m_fRight = right;
        m_fBottom = bottom;
        m_fWidth = m_fRight - m_fLeft;
        m_fHeight = m_fBottom - m_fTop;
        m_fRotation = 0.0f;
        m_vOrigin = new Vector2D(0.5f, 0.5f);
        setCenter();
    }

    public CRect(Rect r) {
        this(r.left, r.top, r.right, r.bottom);
    }

    public CRect(CRect cloneThis) {
        m_fLeft = cloneThis.m_fLeft;
        m_fTop = cloneThis.m_fTop;
        m_fRight = cloneThis.m_fRight;
        m_fBottom = cloneThis.m_fBottom;
        m_fWidth = cloneThis.m_fWidth;
        m_fHeight = cloneThis.m_fHeight;
        m_fRotation = cloneThis.m_fRotation;
        m_vOrigin = new Vector2D(cloneThis.m_vOrigin.getX(), cloneThis.m_vOrigin.getY());
        setCenter();
    }

    public boolean containsPoint(float x, float y) {
        //if (m_fRotation < 0.001f)
        return (x > m_fLeft && x < m_fRight && y > m_fTop && y < m_fBottom) ? true : false;
        //else
        //{
        // check if lines intersect

        //}
    }

    public boolean containsPoint(Vector2D position) {
        return containsPoint(position.getRoundedX(), position.getRoundedY());
    }

    @Override
    public void render(Canvas canvas, Paint paint) {
        canvas.drawRect(m_fLeft, m_fTop, m_fRight, m_fBottom, paint);
    }

    public float getWidth() {
        return m_fWidth;
    }

    public float getHeight() {
        return m_fHeight;
    }

    public void offsetTo(float x, float y) {
        m_fLeft = x;
        m_fRight = x + m_fWidth;
        m_fTop = y;
        m_fBottom = y + m_fHeight;
        setCenter();
    }

    public void offsetBy(float x, float y) {
        m_fLeft += x;
        m_fRight += x;
        m_fCenterX += x;
        m_fTop += y;
        m_fBottom += y;
        m_fCenterY += y;
    }

    private void setCenter() {
        m_fCenterX = m_fLeft + (m_fWidth * 0.5f);
        m_fCenterY = m_fTop + (m_fHeight * 0.5f);
    }

    public void offsetSoCenterIs(float x, float y) {
        m_fLeft = x - (m_fWidth * 0.5f);
        m_fRight = x + (m_fWidth * 0.5f);
        m_fTop = y - (m_fHeight * 0.5f);
        m_fBottom = y + (m_fHeight * 0.5f);
        setCenter();
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

    public boolean intersects(CRect otherRect) {
        return !(otherRect.m_fRight < m_fLeft || otherRect.m_fLeft > m_fRight
                || otherRect.m_fBottom < m_fTop || otherRect.m_fTop > m_fBottom);
    }

    public boolean intersects(CCircle circle) {
        // Find the closest point to the circle within the rectangle
        float closestX = JMath.Clamp(circle.m_fCenterX, m_fLeft, m_fRight);
        float closestY = JMath.Clamp(circle.m_fCenterY, m_fTop, m_fBottom);

        // Calculate the distance between the circle's center and this closest point
        float distanceX = circle.m_fCenterX - closestX;
        float distanceY = circle.m_fCenterY - closestY;

        // If the distance is less than the circle's radius, an intersection occurs
        float distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
        return (distanceSquared < (circle.m_fRadius * circle.m_fRadius));
    }

    public float getRotationDegrees() {
        return m_fRotation;
    }

    public void setRotationDegrees(float rotation) {
        m_fRotation = (rotation > 360.0f ? rotation - 360.0f : rotation);
    }

    public Vector2D getOrigin() {
        return m_vOrigin;
    }

    public void setOrigin(Vector2D origin) {
        m_vOrigin = origin;
    }

    public CRect clone() {
        return new CRect(this);
    }

    public static CRect CreateUsingWidthAndHeight(float left, float top, float width, float height) {
        return new CRect(left, top, left + width, top + height);
    }

    public static CRect ConvertFromCollisionArea(CollisionArea cArea) {
        return new CRect(cArea.getLeft(), cArea.getTop(), cArea.getRight(), cArea.getBottom());
    }

    @Override
    public String toString() {
        return "CRect l:" + getLeft() + ",t:" + getTop() + ",r:" + getRight() + ",b:" + getBottom() + ".";
    }

    private static RectF bounds = new RectF();

    public void renderTextCenterCRect(Canvas canvas, Paint paint, String text) {
        bounds = new RectF(getLeft(), getTop(), getRight(), getBottom());
        // measure text width
        bounds.right = paint.measureText(text, 0, text.length());
        // measure text height
        bounds.bottom = paint.descent() - paint.ascent();

        bounds.left += (getWidth() - bounds.right) / 2.0f;
        bounds.top += (getHeight() - bounds.bottom) / 2.0f;
        canvas.drawText(text, bounds.left, bounds.top - paint.ascent(), paint);
    }

    public void renderTextCenterCRectShadow(Canvas canvas, Paint paint, Paint paintFore, String text) {
        bounds = new RectF(getLeft(), getTop(), getRight(), getBottom());
        // measure text width
        bounds.right = paint.measureText(text, 0, text.length());
        // measure text height
        bounds.bottom = paint.descent() - paint.ascent();

        bounds.left += (getWidth() - bounds.right) / 2.0f;
        bounds.top += (getHeight() - bounds.bottom) / 2.0f;
        for (int i = -5; i < 5; ++i)
            canvas.drawText(text, bounds.left + i, (bounds.top - paint.ascent()) + i, paint);
        canvas.drawText(text, bounds.left + 5, (bounds.top - paint.ascent()) + 5, paintFore);
    }
}
