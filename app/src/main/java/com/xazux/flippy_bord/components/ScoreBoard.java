package com.xazux.flippy_bord.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.xazux._2dlib.I2DGameContext;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.PaintBuilder;
import com.xazux._2dlib.sprites.components.Texture;
import com.xazux.flippy_bord.R;

/**
 * Created by josh on 17/01/15.
 */
public class ScoreBoard {
    private CRect _position;
    private int _score = 0;
    private Paint _paint, _paintForeground;
    private Texture _background;
    private Texture _medalNone, _medalBronze, _medalSilver, _medalGold;
    private CRect _backgroundPosition, _medalPosition, _textPosition;

    public ScoreBoard(I2DGameContext context) {
        CRect screenSize = context.getScreenDimensions();
        float hW = screenSize.getWidth() * 0.1f, hH = hW;
        _position = CRect.CreateUsingWidthAndHeight(screenSize.getCenterX() - hW, screenSize.getHeight() * 0.25f, hW * 2, hH * 2);
        _paint = PaintBuilder.create().setAntiAlias(true).setColor(Color.BLACK).setTextSize(_position.getHeight()).getPaint();
        _paintForeground = PaintBuilder.create().setAntiAlias(true).setColor(Color.WHITE).setTextSize(_position.getHeight()).getPaint();

        _background = new Texture(context.loadBitmap(R.drawable.highscore_back), true);
        _medalNone = new Texture(context.loadBitmap(R.drawable.no_medal), true);
        _medalBronze = new Texture(context.loadBitmap(R.drawable.bronze_medal), true);
        _medalSilver = new Texture(context.loadBitmap(R.drawable.silver_medal), true);
        _medalGold = new Texture(context.loadBitmap(R.drawable.gold_medal), true);

        _backgroundPosition = new CRect(0, 0, screenSize.getCenterX(), screenSize.getCenterX() / 2);
        _backgroundPosition.offsetSoCenterIs(screenSize.getCenterX(), screenSize.getCenterY());
        _medalPosition = new CRect(0, 0, screenSize.getCenterX() / 2, screenSize.getCenterX() / 2);
        _medalPosition.offsetSoCenterIs(screenSize.getCenterX() * 1.25f, screenSize.getCenterY() );
        _textPosition = _medalPosition.clone();
        _textPosition.offsetSoCenterIs(screenSize.getCenterX() * 0.75f, screenSize.getCenterY());
    }

    public void render(Canvas canvas) {
        _position.renderTextCenterCRectShadow(canvas, _paint, _paintForeground, (_score / 2) + "");
    }

    public void renderFinalBoard(Canvas canvas) {
        _background.render(canvas, _backgroundPosition);
        _medalNone.render(canvas, _medalPosition);
        _textPosition.renderTextCenterCRectShadow(canvas, _paint, _paintForeground, (_score / 2) + "");
    }

    public void scorePipePass() {
        _score++;
    }
}
