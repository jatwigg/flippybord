package com.xazux.flippy_bord;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.xazux._2dlib.Helper;
import com.xazux._2dlib._2DGameActivity;
import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.sprites.components.CCircle;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.CollisionArea;
import com.xazux._2dlib.sprites.components.Texture;
import com.xazux._2dlib.touch.Touchable;

/**
 * Created by josh on 06/01/15.
 */
public class Splash extends _2DGameActivity {
    private float _elapsedTime = 0;
    private Paint _textPaint;

    private Texture _background;
    private CRect _backgroundRect;

    private CCircle _circle;
    private Touchable _circleTouch = new Touchable() {
        @Override
        public CollisionArea getCollisionArea() {
            return _circle;
        }

        @Override
        public void OnTouchMove(MotionEvent event, float x, float y) {
            _circle.offsetSoCenterIs((int)(x+0.5f), (int)(y+0.5f));
        }

        @Override
        public void OnTouchStart(MotionEvent event, float x, float y) {

        }

        @Override
        public void OnTouchOver(MotionEvent event, float x, float y) {

        }

        @Override
        public void OnTouchCancel(MotionEvent event) {

        }
    };

    @Override
    public void onInit() {
        Log.d(getClass().getSimpleName(), "onInit()");
        _textPaint = new Paint();
        _textPaint.setTextSize(70);
        _textPaint.setColor(Color.WHITE);

        _background = new Texture(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        _backgroundRect = Helper.GetScreenDimensions(this);

        _circle = new CCircle(_backgroundRect.getCenterX(), _backgroundRect.getCenterY(), 100);
        getTouchHandler().RegisterTouchable(_circleTouch);
    }

    @Override
    public void onUpdate(GameTime gameTime) {
        Log.d(getClass().getSimpleName(), "onUpdate()");
        _elapsedTime += gameTime.getElapsedSeconds();
    }

    @Override
    public void onDraw(Canvas canvas) {
        Log.d(getClass().getSimpleName(), "onDraw()");
        _background.render(canvas, _backgroundRect);
        canvas.drawText(String.format("HELLO WORLD, IT'S BEEN %.2f SECONDS.", _elapsedTime), 200, 200, _textPaint);

        _background.render(canvas, _circle);
    }

    @Override
    public void onDestroy() {
        Log.d(getClass().getSimpleName(), "byebye()");
    }
}