package com.xazux._2dlib.touch;

import android.view.MotionEvent;

import com.xazux._2dlib.Triplet;

import java.util.ArrayList;

/**
 * Created by josh on 19/01/15.
 */
public class TouchState {

    public static final int TOUCH_STARTED = 1;
    public static final int TOUCH_MOVE = 2;
    public static final int TOUCH_OVER = 3;
    public static final int TOUCH_CANCELED = 4;

    public final int ACTION, P_ID;
    public final float X, Y;

    protected TouchState(int action, int pID, float x, float y) {
        ACTION = action;
        P_ID = pID;
        X = x;
        Y = y;
    }

    public static TouchState actionDown(MotionEvent event) {
        return new TouchState(TOUCH_STARTED, event.getPointerId(0), event.getX(), event.getY());
    }

    public static TouchState actionPointerDown(MotionEvent event) {
        int pIndex = extractIndex(event);
        return new TouchState(TOUCH_STARTED, event.getPointerId(pIndex), event.getX(pIndex), event.getY(pIndex));
    }

    public static ArrayList<TouchState> actionMove(MotionEvent event) {
        // should i use historical points?

        //final int historySize = event.getHistorySize();
        final int pointerCount = event.getPointerCount();
        /*for (int h = 0; h < historySize; h++) {
            System.out.printf("At time %d:", event.getHistoricalEventTime(h));
            for (int p = 0; p < pointerCount; p++) {
                System.out.printf("  pointer %d: (%f,%f)",
                        event.getPointerId(p), event.getHistoricalX(p, h), event.getHistoricalY(p, h));
            }
        }*/
        ArrayList<TouchState> pointers = new ArrayList<>();
        for (int p = 0; p < pointerCount; p++) {
            pointers.add(new TouchState(TOUCH_MOVE, event.getPointerId(p), event.getX(p), event.getY(p)));
        }
        return pointers;
    }

    public static TouchState actionUp(MotionEvent event) {
        return new TouchState(TOUCH_OVER, event.getPointerId(0), event.getX(), event.getY());
    }

    public static TouchState actionPointerUp(MotionEvent event) {
        final int pIndex = extractIndex(event);
        return new TouchState(TOUCH_OVER, event.getPointerId(pIndex), event.getX(pIndex), event.getY(pIndex));
    }

    public static TouchState actionCancel(MotionEvent event) {
        return new TouchState(TOUCH_CANCELED, -1, 0, 0); // ie home button pressed
    }

    private static int extractIndex(MotionEvent event) { // Extract the index of the pointer that left the touch sensor
        return (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
    }

}
