package com.xazux.flippy_bord.stategame;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

import com.xazux._2dlib._2DGameActivityWithStates;
import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.sprites.Sprite;
import com.xazux._2dlib.sprites.TouchableSprite;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.Texture;
import com.xazux._2dlib.states.GameState;
import com.xazux._2dlib.touch.MainTouchHandle;
import com.xazux._2dlib.touch.TouchState;
import com.xazux.flippy_bord.Game;
import com.xazux.flippy_bord.R;

/**
 * Created by josh on 23/01/15.
 */
public class StateSplash extends GameState {
    private Sprite _background;
    private TouchableSprite _startButton;

    public StateSplash(_2DGameActivityWithStates context) {
        super(context);
    }

    @Override
    public void initialize() {
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
                    switchState(StateScrollGame.class);
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
    public void render(Canvas canvas) {
        _background.render(canvas);
        _startButton.render(canvas);
    }

    @Override
    public void update(GameTime gameTime) {

    }

    @Override
    public void destroy() {

    }
}
