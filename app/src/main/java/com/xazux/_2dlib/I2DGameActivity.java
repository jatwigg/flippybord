package com.xazux._2dlib;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.xazux._2dlib.time.GameTime;
import com.xazux._2dlib.time.IGameTime;

/**
 * Created by josh on 24/01/15.
 */
public interface I2DGameActivity {

    SurfaceHolder getHolder();

    boolean isInitialised();

    void initialise();

    void update(IGameTime gameTime);

    void render(Canvas canvas);
}
