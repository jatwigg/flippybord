package com.xazux.flippy_bord.activitygame;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import com.xazux._2dlib.JMath;
import com.xazux._2dlib._2DGameActivity;
import com.xazux._2dlib.sprites.Sprite;
import com.xazux._2dlib.sprites.TouchableSprite;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.PaintBuilder;
import com.xazux._2dlib.sprites.components.Texture;
import com.xazux._2dlib.time.IGameTime;
import com.xazux.flippy_bord.R;
import com.xazux.flippy_bord.components.StartButton;

/**
 * Created by josh on 06/01/15.
 */
public class Splash extends _2DGameActivity {
    private Sprite _background;
    private TouchableSprite _startButton;

    @Override
    public void onInit() {
        // background image
        Texture backTx = new Texture(loadBitmap(R.drawable.background), false);
        _background = new Sprite(backTx, getScreenDimensions());

        // start button
        Texture startTx = new Texture(loadBitmap(R.drawable.start),
                PaintBuilder.create().setColor(Color.BLACK).setAntiAlias(true).getPaint());

        CRect screenDimensions = getScreenDimensions();

        float width = screenDimensions.getWidth() * 0.5f;
        float height = startTx.getHeightIfWidthIs(JMath.Round(width));
        CRect startRect = new CRect(0, 0, width, height);
        startRect.offsetSoCenterIs(screenDimensions.getCenterX(), screenDimensions.getCenterY());

        _startButton = new StartButton(startTx, startRect) {
            @Override
            public void onPress() {
                startActivity(new Intent(Splash.this, Game.class));
                overridePendingTransition(R.anim.enter_right, R.anim.leave_right);
            }
        };

        getTouchHandler().RegisterTouchable(_startButton);
    }

    @Override
    public void onUpdate(IGameTime gameTime) {

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
