package com.xazux._2dlib;

import android.graphics.Bitmap;

import com.xazux._2dlib.sprites.components.CRect;

/**
 * Created by josh on 24/01/15.
 */
public interface I2DGameContext {
    CRect getScreenDimensions();
    Bitmap loadBitmap(int cloud);
}
