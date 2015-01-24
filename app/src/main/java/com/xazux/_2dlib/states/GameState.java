package com.xazux._2dlib.states;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.xazux._2dlib.I2DGameContext;
import com.xazux._2dlib._2DGameActivityWithStates;
import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.touch.MainTouchHandle;

import java.util.HashMap;

/**
 * Created by josh on 23/01/15.
 */
public abstract class GameState implements I2DGameContext {
    private final HashMap<Integer, Bitmap> _bitmapMap = new HashMap<>();
    private final _2DGameActivityWithStates _context;

    public GameState(_2DGameActivityWithStates context) {
        _context = context;
    }

    public _2DGameActivityWithStates getContext() {
        return _context;
    }

    public Bitmap loadBitmap(int id) {
        if (_bitmapMap.containsKey(id))
            return _bitmapMap.get(id);
        Bitmap bitmap = BitmapFactory.decodeResource(_context.getResources(), id);
        _bitmapMap.put(id, bitmap);
        return bitmap;
    }

    public MainTouchHandle getTouchHandler() {
        return getContext().getTouchHandler();
    }

    public CRect getScreenDimensions() {
        return _context.getScreenDimensions();
    }

    public abstract void initialize();

    public abstract void render(Canvas canvas);

    public abstract void update(GameTime gameTime);

    public abstract void destroy();

    public boolean onBackPressed()
    {
        return false;
    }

    public void switchState(Class<?> toState) {
        getContext().switchState(toState);
    }
}
