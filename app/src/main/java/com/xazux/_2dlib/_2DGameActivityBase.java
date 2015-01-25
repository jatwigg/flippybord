package com.xazux._2dlib;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.time.IGameTime;
import com.xazux._2dlib.touch.MainTouchHandle;

/**
 * Created by josh on 25/01/15.
 */
abstract class _2DGameActivityBase extends Activity implements I2DGameContext, I2DGameActivity {

    private _2DSurfaceView _surfaceView;
    private GameLoopThread _thread = null;
    private boolean _bInitialised = false;
    private MainTouchHandle _mainTouchHandle = new MainTouchHandle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _surfaceView = new _2DSurfaceView(this);
        setContentView(_surfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (_thread == null) {
            _thread = new GameLoopThread(this);
        }
        else if (!_thread.isRunning()) {
            _thread = new GameLoopThread(this, _thread);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //TODO: this may not be a good solution because lockscreen or something, research this.
        if (_thread.isRunning()) {
            _thread.finish();
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

    public CRect getScreenDimensions() {
        int Measuredwidth = 0;
        int Measuredheight = 0;
        Point size = new Point();
        WindowManager w = getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            w.getDefaultDisplay().getSize(size);
            Measuredwidth = size.x;
            Measuredheight = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }
        return new CRect(0, 0, Measuredwidth, Measuredheight);
    }

    public void setDesiredFPS(int fps) {
        _thread.setMaxFPS(fps);
    }

    public SurfaceHolder getHolder() {
        return _surfaceView.getHolder();
    }

    @Override
    public boolean isInitialised() {
        return _bInitialised;
    }

    public abstract void onInit();

    public abstract void onUpdate(IGameTime gameTime);

    public abstract void onDraw(Canvas canvas);

    public abstract void onFinished();

    @Override
    public void initialise() {
        Log.d(this.getClass().getSimpleName(), "Begin initialising activity.");
        onInit();
        _bInitialised = true;
        Log.d(this.getClass().getSimpleName(), "Finished initialising activity.");
    }

    @Override
    public void update(IGameTime gameTime) {
        getTouchHandler().onUpdate();
        onUpdate(gameTime);
    }

    @Override
    public void render(Canvas canvas) {
        onDraw(canvas);
    }


    @Override
    public void onBackPressed() {
        onFinished();
        super.onBackPressed();
    }
}