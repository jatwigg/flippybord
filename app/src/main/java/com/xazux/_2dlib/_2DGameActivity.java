package com.xazux._2dlib;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.touch.MainTouchHandle;

public abstract class _2DGameActivity extends Activity
{
	private MainThread _thread;
    private _2DSurfaceView _surfaceView;
    private boolean _bInitalized = false;
    private MainTouchHandle _mainTouchHandle = new MainTouchHandle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _thread = new MainThread(this);
        _surfaceView = new _2DSurfaceView(this);
        setContentView(_surfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!_thread.running) {
            //TODO: this is bad because you may have two thread competing/updating stuff, fix
            _thread = new MainThread(this);
            _thread.setRunning(true);
            _thread.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //TODO: this is not a good solution because lockscreen or something, research this.

        if (_thread.running) {
            _thread.setRunning(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        _mainTouchHandle.onTouchEvent(event);
        return true;
    }

    public MainTouchHandle getTouchHandler() {
        return _mainTouchHandle;
    }

    void doInit()
    {
        if (_bInitalized)
            Log.e(this.getClass().getSimpleName(), "doInit() has been called more than once!");
        Log.d(this.getClass().getSimpleName(), "Begin initalizing activity.");
        onInit();
        _bInitalized = true;
        Log.d(this.getClass().getSimpleName(), "Finished initalizing activity.");
    }

    public abstract void onInit();

    public abstract void onUpdate(GameTime gameTime);

    public abstract void onDraw(Canvas canvas);

    public abstract void onFinished();

    public void setDesiredFPS(int fps) {
        _thread.setFPS(fps);
    }

    public SurfaceHolder getHolder()
    {
        return _surfaceView.getHolder();
    }

    public boolean isInitalized() {
        return _bInitalized;
    }
}
