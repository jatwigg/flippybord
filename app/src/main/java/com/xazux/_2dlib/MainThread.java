package com.xazux._2dlib;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.xazux._2dlib.components.GameTime;

public class MainThread extends Thread 
{
	private static final String TAG = MainThread.class.getSimpleName();

    private final boolean DEBUG = true;
    private Paint _debugInfo;

    // desired fps
	private int MAX_FPS = 32;
	// maximum number of frames to be skipped
	private int MAX_FRAME_SKIPS = 5;
	// the frame period
	private long FRAME_PERIOD_MILLS = 1000 / MAX_FPS;

	private GameTime m_gameTime;
	
	// Surface holder that can access the physical surface
	private _2DGameActivity _gameActivity;
	
	// flag to hold game state 
	volatile boolean running; //TODO: I made this volatile, was that the right choice?
	private boolean hasFinished = false;
    private final int CORNFLOURBLUE = Color.rgb(100, 149, 237);

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
            _debugInfo.setAntiAlias(true);
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

		long timeDiff;		// the time it took for the cycle to execute
		long sleepTime;		// ms to sleep (<0 if we're behind)
		int framesSkipped;	// number of frames being skipped

        int framePS = 0;      //frame count
        int skippedPS = 0;
        int lastFPS = 0;
		long elapsedPS = 0;
        long debugCount = 0;

		while (running) 
		{
			canvas = null;
            this._gameActivity.getTouchHandler().onUpdate();
            this._gameActivity.onUpdate(m_gameTime);
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

                    m_gameTime.Clear();

					framesSkipped = 0;	// resetting the frames skipped
					// update game state 
/*
					this._gameActivity.onUpdate(m_gameTime);
					m_gameTime.Clear();
 */
					// render state to the screen
					// draws the canvas on the panel
                    canvas.drawColor(CORNFLOURBLUE); //TODO: replace with cornflour blue
					this._gameActivity.onDraw(canvas);

                    if (DEBUG) {
                        framePS++;
                        elapsedPS += m_gameTime.getElapsedMills();
                        if (elapsedPS > 1000) {
                            lastFPS = framePS;
                            elapsedPS -= 1000;
                            skippedPS = framePS = 0;
                            debugCount++;
                        }
                        canvas.drawText("DEBUG(" + debugCount + ") FPS:" + lastFPS + ". SKIPPED:" + skippedPS, 10,60, _debugInfo);
                    }

                    // unlock canvas to not create a starvation issue
                    _gameActivity.getHolder().unlockCanvasAndPost(canvas);
                    canvas = null; //so we don't end up doing it in finally


					// calculate how long did the cycle take
					timeDiff = m_gameTime.whatIsTimePlz() - m_gameTime.getLastRecordedMills();
					// calculate sleep time
					sleepTime = FRAME_PERIOD_MILLS - timeDiff;
					
					if (sleepTime > 0) 
					{
						// if sleepTime > 0 we're OK
						try 
						{
							// send the thread to sleep for a short period
							// very useful for battery saving
                            Log.d("SLEEP", sleepTime + ".");
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {}
					}
					
					while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS && running) {
                        Log.d(getClass().getSimpleName(), "sleepTime=" + sleepTime + ". framePeriod=" + FRAME_PERIOD_MILLS + ". timeDiff=" + timeDiff);
                        // we need to catch up
                        m_gameTime.Clear();
                        this._gameActivity.onUpdate(m_gameTime); // update without rendering
                        if (DEBUG) {
                            elapsedPS += m_gameTime.getElapsedMills();
                        }
                        sleepTime += FRAME_PERIOD_MILLS;	// add frame period to check if in next frame
						framesSkipped++;
                        skippedPS++;
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
        FRAME_PERIOD_MILLS = 1000 / MAX_FPS;
    }
}
