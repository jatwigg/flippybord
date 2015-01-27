package com.xazux._2dlib;

import android.graphics.Bitmap;

import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.touch.MainTouchHandle;

/**
 * Created by josh on 24/01/15.
 */
public interface I2DGameContext {
    CRect getScreenDimensions();
    Bitmap loadBitmap(int resourceID);
    void switchState(Class<?> toState); //2d activity without states breaks this interface, is it worth removing that activity all together?
    MainTouchHandle getTouchHandler();
}
