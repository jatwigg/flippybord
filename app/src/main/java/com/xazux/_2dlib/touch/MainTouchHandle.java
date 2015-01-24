package com.xazux._2dlib.touch;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Vector;

public class MainTouchHandle {
    protected Object m_touchableLock = new Object();
    protected Vector<TouchControl> m_touchControl = new Vector<TouchControl>();

    ArrayList<TouchState> _eventQ_Started = new ArrayList<>();
    ArrayList<TouchState> _eventQ_Moved = new ArrayList<>();
    ArrayList<TouchState> _eventQ_Over = new ArrayList<>();
    ArrayList<TouchState> _eventQ_Cancelled = new ArrayList<>();

    public boolean onTouchEvent(final MotionEvent event) {
        synchronized (m_touchableLock) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    _eventQ_Started.add(TouchState.actionDown(event));
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    _eventQ_Started.add(TouchState.actionPointerDown(event));
                    break;
                case MotionEvent.ACTION_MOVE:
                    _eventQ_Moved.addAll(TouchState.actionMove(event));
                    break;
                case MotionEvent.ACTION_UP:
                    _eventQ_Over.add(TouchState.actionUp(event));
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    _eventQ_Over.add(TouchState.actionPointerUp(event));
                    break;
                case MotionEvent.ACTION_CANCEL:
                    _eventQ_Cancelled.add(TouchState.actionCancel(event));
                    break;
            }
        }
        return true;
    }

    /**
     * Register a touchable. This will get notified when a touch event happens.
     *
     * @param touchable
     */
    public void RegisterTouchable(final Touchable touchable) {
        synchronized (m_touchableLock) {
            m_touchControl.add(new TouchControl(touchable));
        }
    }

    /**
     * Unregister a touchable. Once unregistered it will not be notified about TouchEvents.
     *
     * @param touchable
     */
    public void UnregisterTouchable(final Touchable touchable) {
        synchronized (m_touchableLock) {
            int i = 0;
            while (i < m_touchControl.size()) {
                if (m_touchControl.get(i).getOwner() == touchable) {
                    m_touchControl.remove(i);
                    break;
                }
                ++i;
            }
        }
    }

    public void onUpdate() {
        // this is called by main thread before it does a game update and game draw. the intention is to
        //  make the touch updates synchronous to the game thread.
        synchronized (m_touchableLock) {
            for (TouchState state : _eventQ_Started) {
                for (TouchControl control : m_touchControl)
                    control.TouchStarted(state);
            }
            _eventQ_Started.clear();

            for (TouchState state : _eventQ_Moved) {
                for (TouchControl control : m_touchControl)
                    control.TouchMove(state);
            }
            _eventQ_Moved.clear();

            for (TouchState state : _eventQ_Over) {
                for (TouchControl control : m_touchControl)
                    control.TouchOver(state);
            }
            _eventQ_Over.clear();

            for (TouchState state : _eventQ_Cancelled) {
                for (TouchControl control : m_touchControl)
                    control.TouchCancel(state);
            }
            _eventQ_Cancelled.clear();
        }
    }

    public void clear() {
        synchronized (m_touchableLock) {
            m_touchControl.clear();
        }
    }
}
