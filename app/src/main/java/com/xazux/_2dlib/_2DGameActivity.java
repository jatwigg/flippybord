package com.xazux._2dlib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;

public abstract class _2DGameActivity extends _2DGameActivityBase implements I2DGameContext, I2DGameActivity {

    private HashMap<Integer, Bitmap> _bitmapMap = new HashMap<>();

    public Bitmap loadBitmap(int id) {
        if (_bitmapMap.containsKey(id))
            return _bitmapMap.get(id);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
        _bitmapMap.put(id, bitmap);
        return bitmap;
    }

    @Override
    public void switchState(Class<?> toState) {
        throw new RuntimeException("switchState is unsupported with " + getClass().getSimpleName() + ". Use " + _2DGameStateActivity.class.getSimpleName() + " for state support.");
    }
}