package com.xazux._2dlib;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import com.xazux._2dlib.states.GameState;
import com.xazux._2dlib.states.StateStore;
import com.xazux._2dlib.time.IGameTime;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by josh on 23/01/15.
 */
public abstract class _2DGameStateActivity extends _2DGameActivityBase implements I2DGameContext, I2DGameActivity {

    private final ArrayList<Class<?>> _states = new ArrayList<>();
    private final ArrayList<Tuple<Class<?>, Class<?>>> _transitionStates = new ArrayList<>(); //T.1 = transition, T.2 = state to show transition before
    private Class<?> _desiredState = null;
    private GameState _currentState = null;

    private StateStore _stateStore = new StateStore();

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
        Constructor<?> ctor = clazz.getConstructor(_2DGameStateActivity.class); //this class is the constructor param
        return (GameState) ctor.newInstance(new Object[]{this});
    }


    protected void RegisterTransition(Class<?> transition, Class<?> afterTransition) {
        _transitionStates.add(new Tuple<Class<?>, Class<?>>(transition, afterTransition));
        //TODO: implement transitions
    }

    public StateStore getStateStore() {
        return _stateStore;
    }

    boolean isPendingStateSwitch() {
        return _desiredState != null;
    }

    public void beginStateSwitch() {
        if (_currentState != null) {
            _currentState.destroy();
            getTouchHandler().clear(); //remove any touch listeners
            _currentState = null;
        }
        try {
            _currentState = invoke(_desiredState);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception raised trying to change state:\n" + e.toString());
        }
        _currentState.initialize();
        _desiredState = null;
    }

    @Override
    public void onUpdate(IGameTime gameTime) {
        if (isPendingStateSwitch()) {
            beginStateSwitch(); //TODO: fire this off in another thread
            //TODO: update transitional state here
        }
        else {
            _currentState.update(gameTime);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (isPendingStateSwitch()) {
            canvas.drawColor(Color.BLACK);
            //TODO: render the transition state
        } else {
            _currentState.render(canvas);
        }
    }

    public abstract void onInit();

    @Override
    public void onBackPressed() {
        if (_currentState != null && _currentState.onBackPressed()) {
            return; // current state returned true, so it handled it
        }
        finish();
    }

    @Override
    public void onFinished() { }

    @Deprecated
    public Bitmap loadBitmap(int cloud) {
        throw new RuntimeException("Use the state current state's loadBitmap function.");
    }

}