package com.xazux.flippy_bord.stategame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.xazux._2dlib._2DGameStateActivity;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.PaintBuilder;
import com.xazux._2dlib.states.GameState;
import com.xazux._2dlib.time.IGameTime;

/**
 * Created by josh on 24/01/15.
 */
public class LoadingGame extends GameState {
    private Paint _paintTextBack;
    private Paint _paintTextFore;
    private String _loadingText;
    private CRect _screenDimensions;
    private float _elapsedSeconds;

    public LoadingGame(_2DGameStateActivity context) {
        super(context);
    }

    @Override
    public void initialize() {
        _paintTextBack = PaintBuilder.create().setColor(Color.GRAY).setAntiAlias(true).setTextSize(30).getPaint();
        _paintTextFore = PaintBuilder.create().setColor(Color.WHITE).setAntiAlias(true).setTextSize(30).getPaint();
        _loadingText = "Loading";
        _screenDimensions = getScreenDimensions();
        _elapsedSeconds = 0.0f;
    }

    @Override
    public void render(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        _screenDimensions.renderTextCenterCRectShadow(canvas, _paintTextBack, _paintTextFore, _loadingText);
    }

    @Override
    public void update(IGameTime gameTime) {
        _elapsedSeconds += gameTime.getElapsedSeconds();
        if (_elapsedSeconds > 1) {
            _elapsedSeconds = _elapsedSeconds % 1.0f;
            if (_loadingText.length() == "Loading . . .".length())
                _loadingText = "Loading";
            else
                _loadingText += " .";
        }
    }

    @Override
    public void destroy() { }
}
