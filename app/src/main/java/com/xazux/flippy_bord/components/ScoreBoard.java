package com.xazux.flippy_bord.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.xazux._2dlib.I2DGameContext;
import com.xazux._2dlib.JMath;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.PaintBuilder;
import com.xazux._2dlib.sprites.components.Texture;
import com.xazux._2dlib.time.IGameTime;
import com.xazux.flippy_bord.R;
import com.xazux.flippy_bord.stategame.StateScrollGame;
import com.xazux.flippy_bord.stategame.StateSplash;

/**
 * Created by josh on 17/01/15.
 */
public class ScoreBoard {
    private CRect _position;
    private int _score = 0;
    private Paint _paint, _paintForeground;
    private Texture _background;
    private Texture _medalToRender, _medalNone, _medalBronze, _medalSilver, _medalGold;
    private CRect _backgroundPosition, _medalPosition, _textPosition;
    private float _elapsedTime = 0.0f;
    private String _scoreString;
    private TextButton _okayButton, _retryButton;
    private Paint _btnPaintFore;//, _btnPaintBack;
    private boolean _renderButtons = false;
    private I2DGameContext _context;
    private float _alpha;

    public ScoreBoard(final I2DGameContext context) {
        _context = context;
        CRect screenSize = context.getScreenDimensions();
        float hW = screenSize.getWidth() * 0.1f, hH = hW;
        _position = CRect.CreateUsingWidthAndHeight(screenSize.getCenterX() - hW, screenSize.getHeight() * 0.25f, hW * 2, hH * 2);
        _paint = PaintBuilder.create().setAntiAlias(true).setColor(Color.BLACK).setTextSize(_position.getHeight() * 0.8f).getPaint();
        _paintForeground = PaintBuilder.create(_paint).setColor(Color.WHITE).getPaint();

        _background = new Texture(context.loadBitmap(R.drawable.highscore_back), true);
        _medalNone = new Texture(context.loadBitmap(R.drawable.no_medal), true);
        _medalBronze = new Texture(context.loadBitmap(R.drawable.bronze_medal), true);
        _medalSilver = new Texture(context.loadBitmap(R.drawable.silver_medal), true);
        _medalGold = new Texture(context.loadBitmap(R.drawable.gold_medal), true);
        _medalToRender = _medalNone;

        _backgroundPosition = new CRect(0, 0, screenSize.getCenterX(), screenSize.getCenterX() / 2);
        _backgroundPosition.offsetSoCenterIs(screenSize.getCenterX(), screenSize.getCenterY());
        _medalPosition = new CRect(0, 0, screenSize.getCenterX() / 2, screenSize.getCenterX() / 2);
        _medalPosition.offsetSoCenterIs(screenSize.getCenterX() * 1.25f, screenSize.getCenterY() );
        _textPosition = _medalPosition.clone();
        _textPosition.offsetSoCenterIs(screenSize.getCenterX() * 0.75f, screenSize.getCenterY());

        _scoreString = "0";

        _btnPaintFore = PaintBuilder.create().setAlpha(1).setAntiAlias(true).setColor(Color.RED).setTextSize(_position.getHeight() / 3).getPaint();
        _alpha = 0;

        CRect okayArea = new CRect(_textPosition.getLeft(), _textPosition.getTop(), _textPosition.getRight(), _textPosition.getCenterY());
        okayArea.offsetBy(0, _textPosition.getHeight());
        _okayButton = new TextButton("Finish", null, okayArea, _btnPaintFore) {
            @Override
            public void onPress() {
                context.switchState(StateSplash.class);
            }
        };

        CRect retryArea = okayArea.clone();
        retryArea.offsetBy(okayArea.getWidth(), 0);
        _retryButton = new TextButton("Retry", null, retryArea, _btnPaintFore) {
            @Override
            public void onPress() {
                context.switchState(StateScrollGame.class);
            }
        };
    }

    public void render(Canvas canvas) {
        _position.renderTextCenterCRectShadow(canvas, _paint, _paintForeground, (_score / 2) + "");
    }

    public void renderFinalBoard(Canvas canvas) {
        _background.render(canvas, _backgroundPosition);
        _medalToRender.render(canvas, _medalPosition);
        _textPosition.renderTextCenterCRectShadow(canvas, _paint, _paintForeground, _scoreString);
        if (_renderButtons) {
            _retryButton.render(canvas);
            _okayButton.render(canvas);
        }
    }

    public void update(IGameTime gameTime) {
        _elapsedTime += gameTime.getElapsedSeconds();
        if (_elapsedTime < 1f) {
            _scoreString = "" + (int) ((_score / 2) * _elapsedTime);
        }
        else {
            if (!_renderButtons) {
                _context.getTouchHandler().RegisterTouchable(_okayButton); // don't bother unregistering these,
                _context.getTouchHandler().RegisterTouchable(_retryButton);// changing state will do it for us.
                _renderButtons = true;
            }
            else
            {
                // fade those buttons in
                if (_alpha < 255) {
                    _alpha += (255 * gameTime.getElapsedSeconds());
                    int a = JMath.Clamp(JMath.Round(_alpha), 0, 255);
                    _btnPaintFore.setAlpha(a);
                }
            }

            _scoreString = "" + (_score  / 2);
        }
    }

    public void scorePipePass() {
        if (++_score >= 100) {
            _medalToRender = _medalBronze;
        }
        else if (_score >= 200) {
            _medalToRender = _medalSilver;
        }
        else if (_score >= 400) {
            _medalToRender = _medalGold;
        }
    }
}
