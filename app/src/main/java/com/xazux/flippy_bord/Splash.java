package com.xazux.flippy_bord;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.util.Log;

import com.xazux._2dlib._2DGameActivity;
import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.sprites.Sprite;
import com.xazux._2dlib.sprites.TouchableSprite;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.Texture;
import com.xazux._2dlib.touch.TouchState;

/**
 * Created by josh on 06/01/15.
 */
public class Splash extends _2DGameActivity {
    private Sprite _background;
    private TouchableSprite _startButton;

    @Override
    public void onInit() {
        Texture backTx = new Texture(loadBitmap(R.drawable.background), false);
        _background = new Sprite(backTx, getScreenDimensions());

        CRect ss = getScreenDimensions();

        Texture startTx = new Texture(loadBitmap(R.drawable.start));
        startTx.getPaint().setColor(Color.BLACK);
        startTx.getPaint().setAntiAlias(true);
        float w = ss.getWidth() * 0.5f, h = startTx.getHeightIfWidthIs((int)(w + 0.5f));

        CRect startRect = new CRect(
                ss.getCenterX() - (w * 0.5f),
                ss.getCenterY() - (h * 0.5f),
                ss.getCenterX() + (w * 0.5f),
                ss.getCenterY() + (h * 0.5f));

        final Paint startPaintPressed = new Paint();
        startPaintPressed.setAntiAlias(true);
        startPaintPressed.setColor(Color.WHITE);
        ColorFilter filter = new LightingColorFilter(Color.RED, 0);
        startPaintPressed.setColorFilter(filter);

        _startButton = new TouchableSprite(startTx, startRect) {
            private boolean startTouched = false;
            @Override
            public void OnTouchMove(TouchState event) {
                startTouched = getCollisionArea().containsPoint(event.X, event.Y);
            }

            @Override
            public void OnTouchStart(TouchState event) {
                startTouched = true;
            }

            @Override
            public void OnTouchOver(TouchState event) {
                startTouched = false;
                if (getCollisionArea().containsPoint(event.X, event.Y)) {
                    startActivity(new Intent(Splash.this, Game.class));
                    overridePendingTransition(R.anim.enter_right, R.anim.leave_right);
                }
            }

            @Override
            public void OnTouchCancel() {
                startTouched = false;
            }

            @Override
            public void render(Canvas canvas) {
                if (startTouched)
                    getTexture().render(canvas, getCollisionArea(), startPaintPressed);
                else
                    getTexture().render(canvas, getCollisionArea());
            }
        };

        getTouchHandler().RegisterTouchable(_startButton);
    }

    @Override
    public void onUpdate(GameTime gameTime) {

    }

    @Override
    public void onDraw(Canvas canvas) {
        _background.render(canvas);
        _startButton.render(canvas);
    }

    @Override
    public void onFinished() {
        Log.d(getClass().getSimpleName(), "byebye()");
    }
}
