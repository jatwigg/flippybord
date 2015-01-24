package com.xazux._2dlib.sprites;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class HealthBar {
    protected int m_iMaxHealth;
    protected int m_iCurrentHealth;
    protected int m_iStep;
    protected boolean m_bIsDead, m_bRender;
    protected float m_fLifePercent;
    protected Rect m_rectBack;
    protected Rect m_rectFront;
    protected Paint m_paintBack;
    protected Paint m_paintFront;
    protected int m_iBorder;

    public HealthBar(int max, int step) {
        m_iMaxHealth = max;
        m_iCurrentHealth = max;
        m_iStep = step;
        m_bIsDead = false;
        m_fLifePercent = 100.0f;
        m_bRender = false;
    }

    public HealthBar(int max, int step, Rect rect) {
        this(max, step);
        m_bRender = true;
        m_iBorder = (int) ((rect.height() / 8.0f) + 0.5f);
        m_rectBack = rect;
        m_rectFront = new Rect(m_rectBack.left + m_iBorder, m_rectBack.top + m_iBorder,
                m_rectBack.right - m_iBorder, m_rectBack.bottom - m_iBorder);
        m_paintBack = new Paint();
        m_paintBack.setARGB(150, 0, 0, 0);
        m_paintFront = new Paint();
        m_paintFront.setARGB(150, 0, 255, 0);
    }

    public boolean reduceHealth() {
        return reduceHealth(1);
    }

    public boolean reduceHealth(int byFactor) {
        m_iCurrentHealth -= (m_iStep * byFactor);

        m_fLifePercent = (m_iCurrentHealth / (float) m_iMaxHealth) * 100.0f;
        if (m_bRender) {
            float newWidth = ((float) (m_rectBack.width() - (m_iBorder * 2)) * (m_fLifePercent / 100.0f));
            m_rectFront.set(m_rectBack.left + m_iBorder, m_rectBack.top + m_iBorder,
                    m_rectBack.left + m_iBorder + (int) newWidth, m_rectBack.bottom - m_iBorder);
        }

        if (m_iCurrentHealth == 0)
            return (m_bIsDead = true);
        return false;
    }

    public float percentLife() {
        return m_fLifePercent;
    }

    public boolean IsDead() {
        return m_bIsDead;
    }

    public void render(Canvas canvas) {
        if (!m_bRender)
            return;
        canvas.drawRect(m_rectBack, m_paintBack);
        canvas.drawRect(m_rectFront, m_paintFront);
    }

    public void reset() {
        m_iCurrentHealth = m_iMaxHealth;
        reduceHealth(0);
        m_bIsDead = false;
    }

    public void addHealthUnits(int units) {
        m_iCurrentHealth += units;
        if (m_iCurrentHealth > m_iMaxHealth)
            m_iCurrentHealth = m_iMaxHealth;
        reduceHealth(0);
    }
}
