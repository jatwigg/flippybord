package com.xazux.flippy_bord;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.xazux._2dlib.Helper;
import com.xazux._2dlib._2DGameActivity;
import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.sprites.components.Animation;
import com.xazux._2dlib.sprites.components.CCircle;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.CollisionArea;
import com.xazux._2dlib.touch.TouchState;
import com.xazux._2dlib.touch.Touchable;

/**
 * Created by josh on 08/01/15.
 */
public class Game extends _2DGameActivity implements Touchable {
    private CRect _screenSize;
    private CloudyBackground _background;
    private Bord _bord;
    private PipeGenerator _pipes;
    private ScoreBoard _scoreBoard;
    private boolean _started = false;
    private boolean _gameover = false;

    @Override
    public void onInit() {
        // background
        _screenSize = Helper.GetScreenDimensions(this);
        _background = new CloudyBackground(getResources(), _screenSize);

        // bord
        Animation anim = new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.bord), 10, Animation.AnimationType.FORWARD_LOOP, 0.1f);
        CCircle cCircle = new CCircle(_screenSize.getWidth() * 0.375f, _screenSize.getCenterY(), _screenSize.getWidth() * 0.075f);
        _bord = new Bord(anim, cCircle, _screenSize.getHeight());

        // score board
        _scoreBoard = new ScoreBoard(getResources(), _screenSize);

        // pipe generator
        _pipes = new PipeGenerator(getResources(), _screenSize, _bord.getCollisionArea().getRight(), _scoreBoard);

        // we want to flap when touched
        getTouchHandler().RegisterTouchable(this);
    }

    @Override
    public void onUpdate(GameTime gameTime) {
        if (_started && !_gameover) {
            _pipes.update(gameTime);
            _gameover = _pipes.intersects(_bord.getCollisionArea());
        }
        _background.update(gameTime.getElapsedSeconds());
        _bord.update(gameTime);
    }

    @Override
    public void onDraw(Canvas canvas) {
        _background.render(canvas);
        _pipes.render(canvas);
        _bord.render(canvas);
        _scoreBoard.render(canvas);
    }

    @Override
    public void onFinished() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_left, R.anim.leave_left);
    }

    @Override
    public CollisionArea getCollisionArea() {
        return _screenSize;
    }

    @Override
    public void OnTouchMove(TouchState event) {

    }

    @Override
    public void OnTouchStart(TouchState event) {
        if (!_started) {
            _started = true;
            _bord.gamebegin();
            _bord.flap();
        }
        else if (!_gameover) {
            _bord.flap();
        }
    }

    @Override
    public void OnTouchOver(TouchState event) {

    }

    @Override
    public void OnTouchCancel() {

    }
}
