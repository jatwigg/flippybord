package com.xazux._2dlib;

/**
 * Created by josh on 24/01/15.
 */
public class StateChangeThread extends Thread {
    private boolean _hasComplete = false;
    private _2DGameStateActivity _context;

    public StateChangeThread(_2DGameStateActivity context) {
        _context = context;
    }

    @Override
    public void run() {
        _context.beginStateSwitch();
        _hasComplete = true;
    }

    public boolean hasComplete() {
        return _hasComplete;
    }
}
