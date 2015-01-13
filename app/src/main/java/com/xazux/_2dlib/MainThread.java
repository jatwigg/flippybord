package com.xazux._2dlib;

import com.xazux._2dlib.components.GameTime;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class MainThread extends Thread 
{
	private static final String TAG = MainThread.class.getSimpleName();

    private final boolean DEBUG = true;
    private Paint _debugInfo;
    private int _debugInfoBack;

    // desired fps
	private int MAX_FPS = 60; //TODO: something is wrong with this: reports wrong fps in debug AND bord jumps higher when this value is higher...
	// maximum number of frames to be skipped
	private int MAX_FRAME_SKIPS = 5;
	// the frame period
	private int FRAME_PERIOD = 1000 / MAX_FPS;

	private GameTime m_gameTime;
	
	// Surface holder that can access the physical surface
	private _2DGameActivity _gameActivity;
	
	// flag to hold game state 
	volatile boolean running; //TODO: I made this volatile, was that the right choice?
	private boolean hasFinished = false;
	
	public void setRunning(boolean run) 
	{
		this.running = run;
	}

	public MainThread(_2DGameActivity gameActivity) {
		super();
		this._gameActivity = gameActivity;
		m_gameTime = new GameTime();
        if (DEBUG) {
            _debugInfo = new Paint();
            _debugInfo.setColor(Color.MAGENTA);
            _debugInfo.setTextSize(50);
            _debugInfoBack = Color.argb(35, 0, 255, 255);
        }
	}

	@Override
	public void run() {
        Log.d(TAG, "Doing init");
        if (!this._gameActivity.isInitalized()) {
            this._gameActivity.doInit();
        }

		Canvas canvas;
		Log.d(TAG, "Starting game loop");

		long beginTime;		// the time when the cycle begun
		long timeDiff;		// the time it took for the cycle to execute
		int sleepTime;		// ms to sleep (<0 if we're behind)
		int framesSkipped;	// number of frames being skipped

        int framePS = 0;      //frame count
        int skippedPS = 0;
        int lastFPS = 0;
		float elapsedPS = 0.0f;

		while (running) 
		{
            beginTime = System.currentTimeMillis();
			canvas = null;
			// try locking the canvas for exclusive pixel editing
			// in the surface
			try {
				if (!_gameActivity.getHolder().getSurface().isValid()) {
                    Log.d(getClass().getSimpleName(), "Surface not valid, sleeping...");
                    try {
                        Thread.sleep(100, 0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                if ((canvas = _gameActivity.getHolder().lockCanvas()) == null) {
                    Log.d(getClass().getSimpleName(), "Canvas is null, sleeping...");
                    try {
                        Thread.sleep(100, 0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
				synchronized (this._gameActivity) {
					//beginTime = System.currentTimeMillis();
					framesSkipped = 0;	// resetting the frames skipped
					// update game state 
					this._gameActivity.onUpdate(m_gameTime);
					m_gameTime.Clear();
					// render state to the screen
					// draws the canvas on the panel
                    canvas.drawColor(Color.BLACK); //TODO: replace with cornflour blue
					this._gameActivity.onDraw(canvas);

                    if (DEBUG) {
                        framePS++;
                        elapsedPS += m_gameTime.getElapsedSeconds();
                        if (elapsedPS > 1.0f) {
                            lastFPS = framePS;
                            elapsedPS -= 1.0f;
                            skippedPS = framePS = 0;
                        }
                        canvas.drawColor(_debugInfoBack);
                        canvas.drawText("DEBUG FPS:" + lastFPS + ".", 10,60, _debugInfo);
                    }

					// calculate how long did the cycle take
					timeDiff = System.currentTimeMillis() - beginTime;
					// calculate sleep time
					sleepTime = (int)(FRAME_PERIOD - timeDiff);
					
					if (sleepTime > 0) 
					{
						// if sleepTime > 0 we're OK
						try 
						{
							// send the thread to sleep for a short period
							// very useful for battery saving
							Thread.sleep(sleepTime);	
						} catch (InterruptedException e) {}
					}
					
					while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS && running) 
					{
						// we need to catch up
						this._gameActivity.onUpdate(m_gameTime); // update without rendering
						m_gameTime.Clear();
						sleepTime += FRAME_PERIOD;	// add frame period to check if in next frame
						framesSkipped++;
					}
				}
			} finally {
				// in case of an exception the surface is not left in 
				// an inconsistent state
				if (canvas != null) {
                    _gameActivity.getHolder().unlockCanvasAndPost(canvas);
				}
			}	// end finally
			hasFinished = true;
		}
		Log.d(TAG, "Ended game loop.");
	}

	public boolean hasFinished() 
	{
		return hasFinished;
	}

    public void setFPS(int fps) {
        MAX_FPS = fps;
        FRAME_PERIOD = 1000 / MAX_FPS;
    }
}
