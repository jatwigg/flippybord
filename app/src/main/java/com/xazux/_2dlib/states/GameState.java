package com.xazux._2dlib.states;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.xazux._2dlib.I2DGameContext;
import com.xazux._2dlib._2DGameStateActivity;
import com.xazux._2dlib.sound.JukeBox;
import com.xazux._2dlib.sound.SoundEffectBox;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.time.IGameTime;
import com.xazux._2dlib.touch.MainTouchHandle;

import java.util.HashMap;

/**
 * Created by josh on 23/01/15.
 */
public abstract class GameState implements I2DGameContext {
    private final HashMap<Integer, Bitmap> _bitmapMap = new HashMap<>();
    private final _2DGameStateActivity _context;
    private final SoundEffectBox _soundEffectBox;
    private final JukeBox _jukeBox;

    public GameState(_2DGameStateActivity context) {
        _context = context;
        _soundEffectBox = new SoundEffectBox(context);
        _jukeBox = new JukeBox(context);
    }

    public _2DGameStateActivity getContext() {
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

    public abstract void update(IGameTime gameTime);

    public abstract void destroy();

    public boolean onBackPressed() {
        return false;
    }

    public void switchState(Class<?> toState) {
        getContext().switchState(toState);
    }

    public SoundEffectBox soundEffectBox() {
        return _soundEffectBox;
    }

    public JukeBox jukeBox() {
        return  _jukeBox;
    }
}
