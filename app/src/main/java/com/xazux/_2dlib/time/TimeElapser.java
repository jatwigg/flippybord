package com.xazux._2dlib.time;

public class TimeElapser {
    protected float m_fTimerLengthSeconds;
    protected float m_fElapsedTime;
    protected boolean m_bHasStarted;

    public TimeElapser(float durationInSeconds) {
        m_fTimerLengthSeconds = durationInSeconds;
        m_bHasStarted = false;
    }

    public boolean UpdateAndHasTimeElapsed(GameTime gameTime) {
        if (!m_bHasStarted)
            return false;
        return (m_fElapsedTime += gameTime.getElapsedSeconds()) > m_fTimerLengthSeconds ? true : false;
    }

    public void ResetAndStart() {
        m_fElapsedTime = 0.0f;
        m_bHasStarted = true;
    }

    public void Start() {
        if (!m_bHasStarted)
            ResetAndStart();
    }

    public float getPercentageComplete(GameTime gameTime) {
        float percent = m_fElapsedTime / m_fTimerLengthSeconds;

        percent = percent * 100.0f;
        return (percent > 100.0f ? 100.0f : (percent < 0.0f ? 0.0f : percent));
    }

    public float getTimeSpecified() {
        return m_fTimerLengthSeconds;
    }
}
