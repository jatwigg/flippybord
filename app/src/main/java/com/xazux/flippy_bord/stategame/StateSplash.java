package com.xazux.flippy_bord.stategame;

import android.graphics.Canvas;
import android.graphics.Color;

import com.xazux._2dlib.JMath;
import com.xazux._2dlib._2DGameStateActivity;
import com.xazux._2dlib.sprites.Sprite;
import com.xazux._2dlib.sprites.TouchableSprite;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.PaintBuilder;
import com.xazux._2dlib.sprites.components.Texture;
import com.xazux._2dlib.states.GameState;
import com.xazux._2dlib.time.IGameTime;
import com.xazux.flippy_bord.R;
import com.xazux.flippy_bord.components.StartButton;

/**
 * Created by josh on 23/01/15.
 */
public class StateSplash extends GameState {
    private Sprite _background;
    private TouchableSprite _startButton;

    public StateSplash(_2DGameStateActivity context) {
        super(context);
    }

    @Override
    public void initialize() {
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
                switchState(StateScrollGame.class);
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
    public void update(IGameTime gameTime) {

    }

    @Override
    public void destroy() {

    }
}
