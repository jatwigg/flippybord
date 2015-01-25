package com.xazux._2dlib;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.xazux._2dlib.sprites.components.PaintBuilder;
import com.xazux._2dlib.time.IGameTime;

/**
 * Created by josh on 25/01/15.
 */
public abstract class _2DGameActivityDebug extends _2DGameActivity {
    private Paint _paint;
    private float _elapsedTime;
    private int _frameCount, _lastFrameCount, _secondCount;

    @Override
    public void initialise() {
        super.initialise();
        _paint = PaintBuilder.create().setAntiAlias(true).setColor(Color.MAGENTA).setTextSize(20f).getPaint();
        _elapsedTime = 0.0f;
        _frameCount = _lastFrameCount = _secondCount = 0;
    }

    @Override
    public void update(IGameTime gameTime) {
        super.update(gameTime);
        if ( (_elapsedTime += gameTime.getElapsedSeconds() ) > 1f) {
            _elapsedTime = _elapsedTime % 1f;
            _lastFrameCount = _frameCount;
            _frameCount = 0;
            _secondCount++;
        }
    }

    @Override
    public void render(Canvas canvas) {
        super.render(canvas);
        canvas.drawText("Elapsed: " + _secondCount + " FPS:" + _lastFrameCount, 20, 20, _paint);
        _frameCount++;
    }
}
