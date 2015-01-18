package com.xazux.flippy_bord;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.xazux._2dlib.Helper;
import com.xazux._2dlib.sprites.components.CRect;

/**
 * Created by josh on 17/01/15.
 */
public class ScoreBoard {
    private CRect _position;
    private int _score = 0;
    private Paint _paint, _paintForeground;
    private Paint _debugPaint = new Paint();

    public ScoreBoard(Resources resources, CRect screenSize) {
        float hW = screenSize.getWidth() * 0.1f, hH = hW;
        _position = CRect.CreateUsingWidthAndHeight(screenSize.getCenterX() - hW, screenSize.getHeight() * 0.25f, hW*2, hH*2);

        _paint = new Paint();
        _paint.setColor(Color.BLACK);
        _paint.setTextSize(_position.getHeight());

        _paintForeground = new Paint();
        _paintForeground.setColor(Color.WHITE);
        _paintForeground.setTextSize(_position.getHeight());

        _debugPaint.setColor(Color.GREEN);
    }

    public void render(Canvas canvas)
    {
        Helper.RenderTextCenterCRectShadow(canvas, _paint, _paintForeground, (_score/2) + "", _position);
    }

    public void scorePipePass() {
        _score++;
    }
}
