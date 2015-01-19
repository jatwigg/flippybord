package com.xazux.flippy_bord;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.xazux._2dlib.Helper;
import com.xazux._2dlib._2DGameActivity;
import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.CollisionArea;
import com.xazux._2dlib.sprites.components.Texture;
import com.xazux._2dlib.touch.TouchState;
import com.xazux._2dlib.touch.Touchable;

/**
 * Created by josh on 06/01/15.
 */
public class Splash extends _2DGameActivity {
    private Texture _background;

    private CRect _backgroundRect;
    private Matrix _backgroundMatrix;

    private boolean _startTouched;
    private Paint _startPaintPressed;
    private Texture _startTx;
    private CRect _startRect;
    private Touchable _startTouch = new Touchable() {
        @Override
        public CollisionArea getCollisionArea() {
            return _startRect;
        }

        @Override
        public void OnTouchMove(TouchState event) {
            _startTouched = _startRect.containsPoint(event.X, event.Y);
        }

        @Override
        public void OnTouchStart(TouchState event) {
            _startTouched = true;
        }

        @Override
        public void OnTouchOver(TouchState event) {
            _startTouched = false;
            if (_startRect.containsPoint(event.X, event.Y)) { //are these co-ords correct
                //Toast.makeText(Splash.this, "lets start", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Splash.this, Game.class));
                overridePendingTransition(R.anim.enter_right, R.anim.leave_right);
            }
        }

        @Override
        public void OnTouchCancel() {
            _startTouched = false;
        }
    };

    @Override
    public void onInit() {
        Log.d(getClass().getSimpleName(), "onInit()");
        _startPaintPressed = new Paint();
        _startPaintPressed.setColor(Color.WHITE);
        ColorFilter filter = new LightingColorFilter(Color.RED, 0);
        _startPaintPressed.setColorFilter(filter);


        _background = new Texture(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        _background.setStretch(false);
        _backgroundRect = Helper.GetScreenDimensions(this);

        _startTx = new Texture(BitmapFactory.decodeResource(getResources(), R.drawable.start));
        _startTx.getPaint().setColor(Color.BLACK);
        float w = _backgroundRect.getWidth() * 0.5f, h = _startTx.getHeightIfWidthIs((int)(w + 0.5f));

        //TODO: remove (int) casts when CRectF is implemented
        _startRect = new CRect(
                (int)(_backgroundRect.getCenterX() - (w * 0.5f)),
                (int)(_backgroundRect.getCenterY() - (h * 0.5f)),
                (int)(_backgroundRect.getCenterX() + (w * 0.5f)),
                (int)(_backgroundRect.getCenterY() + (h * 0.5f)));

        // [---===---]

        //9 / 3 = 3

        //TODO: this looks okay but is it right? i think there needs to be some common value that both the X and the Y get increased by
        float x = _backgroundRect.getWidth() / (float)_background.getWidth(), y  = _backgroundRect.getHeight() / (float)_background.getHeight();
        y = x > y? x: y;
        _backgroundMatrix = new Matrix();
        _backgroundMatrix.preTranslate(x * 0.5f, x * 0.5f);
        _backgroundMatrix.preScale(y, y);
        _backgroundMatrix.preTranslate(x * -0.5f, x * -0.5f);

        getTouchHandler().RegisterTouchable(_startTouch);
    }

    @Override
    public void onUpdate(GameTime gameTime) {
    }

    @Override
    public void onDraw(Canvas canvas) {
        //_background.render(canvas, _backgroundRect);
        _background.render(canvas, _backgroundMatrix);
        //canvas.drawText(String.format("HELLO WORLD, IT'S BEEN %.2f SECONDS.", _elapsedTime), 200, 200, _textPaint);
        if (_startTouched)
            _startTx.render(canvas, _startRect, _startPaintPressed);
        else
            _startTx.render(canvas, _startRect);
    }

    @Override
    public void onFinished() {
        Log.d(getClass().getSimpleName(), "byebye()");
    }
}
