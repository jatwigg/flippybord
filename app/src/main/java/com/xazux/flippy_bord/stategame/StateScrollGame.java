package com.xazux.flippy_bord.stategame;

import android.graphics.Canvas;

import com.xazux._2dlib._2DGameActivityWithStates;
import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.sprites.components.CollisionArea;
import com.xazux._2dlib.states.GameState;
import com.xazux._2dlib.touch.TouchState;
import com.xazux._2dlib.touch.Touchable;
import com.xazux.flippy_bord.components.Bord;
import com.xazux.flippy_bord.components.CloudyBackground;
import com.xazux.flippy_bord.components.PipeGenerator;
import com.xazux.flippy_bord.components.ScoreBoard;

/**
 * Created by josh on 23/01/15.
 */
public class StateScrollGame extends GameState implements Touchable {
    private CloudyBackground _background;
    private Bord _bord;
    private PipeGenerator _pipes;
    private ScoreBoard _scoreBoard;
    private boolean _started = false;
    private boolean _gameover = false;

    public StateScrollGame(_2DGameActivityWithStates context) {
        super(context);
    }

    @Override
    public void initialize() {
        _background = new CloudyBackground(this);
        _bord = new Bord(this);
        _scoreBoard = new ScoreBoard(this);
        _pipes = new PipeGenerator(this, _bord.getCollisionArea().getRight(), _scoreBoard);
        // we want to flap when touched
        getTouchHandler().RegisterTouchable(this);
    }

    @Override
    public void render(Canvas canvas) {
        _background.render(canvas);
        _pipes.render(canvas);
        _bord.render(canvas);
        _scoreBoard.render(canvas);
    }

    @Override
    public void update(GameTime gameTime) {
        if (_started && !_gameover) {
            _pipes.update(gameTime);
            _gameover = _pipes.intersects(_bord.getCollisionArea());
        }
        _background.update(gameTime.getElapsedSeconds());
        _bord.update(gameTime);
    }

    @Override
    public void destroy() {

    }

    @Override
    public CollisionArea getCollisionArea() {
        return getScreenDimensions();
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
