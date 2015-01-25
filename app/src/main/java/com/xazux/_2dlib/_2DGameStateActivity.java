package com.xazux._2dlib;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.xazux._2dlib.states.GameState;
import com.xazux._2dlib.states.StateStore;
import com.xazux._2dlib.time.IGameTime;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by josh on 23/01/15.
 */
public abstract class _2DGameStateActivity extends _2DGameActivityBase implements I2DGameContext, I2DGameActivity, Runnable {

    private final ArrayList<Class<?>> _states = new ArrayList<>();
    private final ArrayList<Tuple<Class<?>, Class<?>>> _transitionStates = new ArrayList<>(); //T.1 = transition, T.2 = state to show transition before
    private volatile Class<?> _desiredState = null;
    private volatile GameState _currentState = null;
    private volatile GameState _transitionState = null;
    private volatile boolean _currentlySwitchingStates = false;
    private StateStore _stateStore = new StateStore();

    ///region game state handling

    protected void RegisterGameState(Class<?> state) {
        if (_states.size() == 0) {
            switchState(state);
        }
        _states.add(state);
    }

    public void switchState(Class<?> toState) {
        if (toState == null)
            finish();
        _desiredState = toState;
    }

    private GameState instantiate(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> ctor = clazz.getConstructor(_2DGameStateActivity.class); //this class is the constructor param
        return (GameState) ctor.newInstance(new Object[]{this});
    }


    protected void RegisterTransition(Class<?> transition, Class<?> afterTransition) {
        _transitionStates.add(new Tuple<Class<?>, Class<?>>(transition, afterTransition));
    }

    boolean isPendingStateSwitch() {
        return _desiredState != null;
    }

    public void beginStateSwitch() {
        _currentlySwitchingStates = true;
        //check for transitional state
        for (Tuple<Class<?>, Class<?>> tuple : _transitionStates) {
            if (tuple.Item2.equals(_desiredState)) { //todo: check if this is the correct way to compare Class<?>
                try {
                    _transitionState = instantiate(tuple.Item1);
                    _transitionState.initialize();
                }
                catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "exception raised trying to create transition.", e);
                }
                break;
            }
        }

        // destroy old state and initialise the new state in another thread
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public final void run() {
        if (_currentState != null) {
            _currentState.destroy();
            getTouchHandler().clear(); //remove any touch listeners
            _currentState = null;
        }
        try {
            Log.d(getClass().getSimpleName(), "instantiating the desired state " + _desiredState);
            _currentState = instantiate(_desiredState);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception raised trying to change state:\n" + e.toString());
        }
        _currentState.initialize();
        _desiredState = null;
        _currentlySwitchingStates = false;
    }

    ///endregion

    public StateStore getStateStore() {
        return _stateStore;
    }

    public abstract void onInit();

    @Override
    public void onUpdate(IGameTime gameTime) {
        if (isPendingStateSwitch()) {
            if (!_currentlySwitchingStates) {
                beginStateSwitch();
            }

            if (_transitionState != null) {
                _transitionState.update(gameTime);
            }
        }
        else {
            if (_transitionState != null) {
                _transitionState = null;
            }
            _currentState.update(gameTime);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (isPendingStateSwitch()) {
            if (_transitionState != null) {
                _transitionState.render(canvas);
            }
        } else {
            _currentState.render(canvas);
        }
    }

    @Override
    public final void onFinished() { } // unused

    @Override
    public void onBackPressed() {
        if (_currentState != null && _currentState.onBackPressed()) {
            return; // current state returned true, so it handled it
        }
        finish();
    }

    @Deprecated
    public Bitmap loadBitmap(int cloud) {
        throw new RuntimeException("Use the state current state's loadBitmap function.");
    }
}