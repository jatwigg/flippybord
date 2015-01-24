package com.xazux._2dlib;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.states.GameState;
import com.xazux._2dlib.states.StateStore;
import com.xazux._2dlib.touch.MainTouchHandle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by josh on 23/01/15.
 */
public abstract class _2DGameActivityWithStates extends Activity implements I2DGameContext {

    private final ArrayList<Class<?>> _states = new ArrayList<>();
    private Class<?> _desiredState = null;
    private GameState _currentState = null;

    private MainThreadStates _thread;
    private _2DSurfaceView _surfaceView;
    private boolean _bInitalized = false;
    private MainTouchHandle _mainTouchHandle = new MainTouchHandle();
    private StateStore _stateStore = new StateStore();
    private Paint _paint = new Paint();
    private Paint _paintFore = new Paint();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _thread = new MainThreadStates(this);
        _surfaceView = new _2DSurfaceView(this);
        setContentView(_surfaceView);

        //TODO: remove these and use the user defined loading screen
        _paint.setColor(Color.GRAY);
        _paintFore.setColor(Color.WHITE);
        _paint.setTextSize(50);
        _paintFore.setTextSize(50);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!_thread.running) {
            //TODO: this is bad because you may have two thread competing/updating stuff, fix
            _thread = new MainThreadStates(this);
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

    public CRect getScreenDimensions() {
        return Helper.GetScreenDimensions(this);
    }

    protected void RegisterGameState(Class<?> state) {
        if (_states.size() == 0) {
            switchState(state);
        }
        _states.add(state);
    }

    public void switchState(Class<?> toState) {
        _desiredState = toState;
    }

    private GameState invoke(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> ctor = clazz.getConstructor(_2DGameActivityWithStates.class); //this class is the constructor param
        return (GameState) ctor.newInstance(new Object[] { this });
    }

    public StateStore getStateStore()
    {
        return _stateStore;
    }

    boolean isPendingStateSwitch()
    {
        return _desiredState != null;
    }

    public void beginStateSwitch() {
        if (_currentState != null) {
            _currentState.destroy();
            _currentState = null;
        }
        try {
            _currentState = invoke(_desiredState);
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Exception raised trying to change state:\n" + e.toString());
        }
        _currentState.initialize();
        _desiredState = null;
    }

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

    void onUpdate(GameTime gameTime)
    {
        _currentState.update(gameTime);
    }

    void onDraw(Canvas canvas) {
        if (isPendingStateSwitch())
        {
            canvas.drawColor(Color.BLACK);
            Helper.RenderTextCenterCRectShadow(canvas, _paint, _paintFore, "Loading, please wait.", getScreenDimensions());
        }
        else {
            _currentState.render(canvas);
        }
    }

    public abstract void onInit();

    void doInit()
    {
        if (_bInitalized)
            Log.e(this.getClass().getSimpleName(), "doInit() has been called more than once!");
        Log.d(this.getClass().getSimpleName(), "Begin initalizing activity.");
        onInit();
        _bInitalized = true;
        Log.d(this.getClass().getSimpleName(), "Finished initalizing activity.");
    }

    @Override
    public void onBackPressed() {
        if (_currentState != null && _currentState.onBackPressed()){
            return; // current state returned true, so it handled it
        }
        finish();
    }

    @Deprecated
    public Bitmap loadBitmap(int cloud)
    {
        throw new RuntimeException("Use the state current state's loadBitmap function.");
    }
}
