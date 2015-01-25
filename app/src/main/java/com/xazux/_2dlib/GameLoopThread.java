package com.xazux._2dlib;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import com.xazux._2dlib.time.GameTime;

/**
 * Created by josh on 24/01/15.
 */
public class GameLoopThread implements Runnable {
    private final int CORN_FLOUR_BLUE = Color.rgb(100, 149, 237); // thanks XNA
    // timings
    private int _maxFPS = 32;
    private int _maxFrameSkips = 5;
    private long _framePeriodMills = 1000 / _maxFPS;
    // context and state
    private Thread _thread;
    private final I2DGameActivity _gameActivity;
    private volatile boolean _isRunning = true; // volatile, so 'setting' thread and 'this' thread don't have an inconsistent value for it

    public GameLoopThread(I2DGameActivity activity) {
        _gameActivity = activity;
        _thread = new Thread(this);
        _thread.start();
    }

    public GameLoopThread(I2DGameActivity activity, GameLoopThread joinThread) {
        _gameActivity = activity;
        _thread = new Thread(this);
        try {
            joinThread._thread.join();
        } catch (InterruptedException e) { }
        _thread.start();
    }

    @Override
    public void run() {
        Log.d(this.getClass().getSimpleName(), "Thread start");

        if (!_gameActivity.isInitialised()) {
            this._gameActivity.initialise();
        }

        Canvas canvas = null;
        long timeDiff, sleepTime;
        int framesSkipped;

        GameTime gameTime = new GameTime();

        while (_isRunning) {
            // refresh time
            gameTime.refresh();
            // update game logic
            _gameActivity.update(gameTime);
            try {
                // get canvas
                try {
                    if (!_gameActivity.getHolder().getSurface().isValid()
                            || (canvas = _gameActivity.getHolder().lockCanvas()) == null) {
                        Log.d(getClass().getSimpleName(), "Surface not valid, or canvas is null, sleeping...");
                        Thread.sleep(100, 0);
                        continue;
                    }
                } catch (InterruptedException e) { /* thread woken */ }

                synchronized (this._gameActivity) { //TODO: investigate if this needs to be synchronized, as there should just be the one thread
                    // draw stuff
                    canvas.drawColor(CORN_FLOUR_BLUE);
                    _gameActivity.render(canvas);

                    // unlock canvas to not create a starvation issue
                    _gameActivity.getHolder().unlockCanvasAndPost(canvas);
                    canvas = null; //so we don't end up doing it in finally

                    // work out time taken then sleep time
                    timeDiff = gameTime.whatIsTimePlz() - gameTime.getLastRecordedMills();
                    sleepTime = _framePeriodMills - timeDiff;

                    if (sleepTime > 0) {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) { }
                    }
                    else if (sleepTime < 0) {
                        framesSkipped = 0;
                        while (sleepTime < 0 && framesSkipped < _maxFrameSkips && _isRunning) {
                            // we need to catch up
                            gameTime.refresh();
                            // update without rendering
                            _gameActivity.update(gameTime);
                            sleepTime += _framePeriodMills;
                        }
                    }
                }
            } finally {
                if (canvas != null) {
                    _gameActivity.getHolder().unlockCanvasAndPost(canvas);
                }
            }
        }
        Log.d(this.getClass().getSimpleName(), "/Thread");
    }

    public boolean isRunning() {
        return _isRunning;
    }

    public void finish() {
        _isRunning = false;
    }

    public void setMaxFPS(int fps) {
        _maxFPS = fps;
        _framePeriodMills = 1000 / _maxFPS;
        Log.d(getClass().getSimpleName(), "Frames per second changed to: " + _maxFPS + ". period: " + _framePeriodMills);
    }
}
