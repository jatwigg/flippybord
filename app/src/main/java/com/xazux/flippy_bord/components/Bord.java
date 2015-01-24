package com.xazux.flippy_bord.components;

import android.graphics.Canvas;

import com.xazux._2dlib.I2DGameContext;
import com.xazux._2dlib.JMath;
import com.xazux._2dlib._2DGameActivity;
import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.sprites.components.Animation;
import com.xazux._2dlib.sprites.components.CCircle;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.CollisionArea;
import com.xazux.flippy_bord.R;

/**
 * Created by josh on 09/01/15.
 */
public class Bord {
    private float _yFlapSpeedSeconds, _ySpeed = 0.0f, _rotation = 0.0f;
    private Animation _tx;
    private boolean _started = false;
    private float _screenHeight;
    private final float GRAVITY = 600.0f; //todo perhaps make this based on height?
    private final float ROTATION_SPEED = 100.0f;
    private CCircle _collisionArea, _drawArea;

    public Bord(I2DGameContext context) {
        CRect screenSize = context.getScreenDimensions();
        _drawArea = new CCircle(screenSize.getWidth() * 0.375f, screenSize.getCenterY(), screenSize.getWidth() * 0.075f);

        _tx = new Animation(context.loadBitmap(R.drawable.bord), 10, Animation.AnimationType.FORWARD_LOOP, 0.05f);
        _tx.getPaint().setAntiAlias(true);
        _tx.setStretch(false);
        _tx.Play();

        _yFlapSpeedSeconds = screenSize.getHeight() * -0.5f;
        _screenHeight = screenSize.getHeight();
        _collisionArea = new CCircle(_drawArea.getCenterX(), _drawArea.getCenterY(), _drawArea.getRadius() * 0.7f);
    }

    public void update(GameTime gameTime) {
        if (_started) {

            _ySpeed = JMath.Clamp(_ySpeed + (GRAVITY * gameTime.getElapsedSeconds()), _yFlapSpeedSeconds, GRAVITY);
            _ySpeed += (GRAVITY * gameTime.getElapsedSeconds());
            float distanceTraveled = _ySpeed * gameTime.getElapsedSeconds();

            float yPos = _drawArea.getBottom() + distanceTraveled;

            if (yPos > _screenHeight) {
                float f = _screenHeight - _drawArea.getBottom();
                _drawArea.offsetBy(0, f);
            }
            else if (yPos < _drawArea.getHeight()) {
                float f = _drawArea.getTop() * -1.0f;
                _drawArea.offsetBy(0, f);
            }
            else {
                _drawArea.offsetBy(0, distanceTraveled);
            }

            _rotation += ROTATION_SPEED * gameTime.getElapsedSeconds();
            if (_rotation > -45 && _rotation < 90)
                _drawArea.setRotationDegrees(_rotation);

            // update collision area
            _collisionArea.offsetSoCenterIs(_drawArea.getCenterX(), _drawArea.getCenterY());
        }

        _tx.update(gameTime);
    }

    public CollisionArea getCollisionArea() {
        return _collisionArea;
    }

    public void flap() {
        _ySpeed = _yFlapSpeedSeconds;
        _rotation = -45.0f;
        _tx.Stop();
        _tx.Play();
    }

    public void gamebegin() {
        _started = true;
        _tx.Stop();
        _tx.ChangeType(Animation.AnimationType.FORWARD_ONCE);
        _tx.Play();
    }

    public void render(Canvas canvas) {
        _tx.render(canvas, _drawArea);
    }
}
