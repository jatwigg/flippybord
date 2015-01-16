package com.xazux.flippy_bord;

import android.util.Log;

import com.xazux._2dlib.JMath;
import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.sprites.Sprite;
import com.xazux._2dlib.sprites.components.Animation;
import com.xazux._2dlib.sprites.components.CollisionArea;

/**
 * Created by josh on 09/01/15.
 */
public class Bord extends Sprite {
    private float _rotateSpeed, _yFlapSpeedSeconds, _ySpeed = 0.0f, _rotation = 0.0f;
    private Animation _tx;
    private boolean _started = false;
    private float _screenHeight;
    private final float GRAVITY = 600.0f; //todo perhaps make this based on height?

    public Bord(Animation texture, CollisionArea collisionArea, float screenHeight) {
        super(texture, collisionArea);
        _tx = texture;
        _tx.setStretch(true);
        _tx.Play();
        _yFlapSpeedSeconds = screenHeight * -0.5f;
        _rotateSpeed = screenHeight * 0.1f;
        _screenHeight = screenHeight;
    }

    public void update(GameTime gameTime) {
        if (_started) {
            //totally wrong
            _ySpeed = JMath.Clamp(_ySpeed + (GRAVITY * gameTime.getElapsedSeconds()), _yFlapSpeedSeconds, GRAVITY);
            _ySpeed += (GRAVITY * gameTime.getElapsedSeconds());
            float distanceTraveled = _ySpeed * gameTime.getElapsedSeconds();

            float yPos = getCollisionArea().getBottom() + distanceTraveled;

            if (yPos > _screenHeight) {
                float f = _screenHeight - getCollisionArea().getBottom();
                getCollisionArea().offsetBy(0, f);
            }
            else if (yPos < getCollisionArea().getHeight()) {
                float f = getCollisionArea().getTop() * -1.0f;
                getCollisionArea().offsetBy(0, f);
            }
            else {
                getCollisionArea().offsetBy(0, distanceTraveled);
            }

            _rotation += _rotateSpeed * gameTime.getElapsedSeconds();
            if (_rotation > 0 && _rotation < 90)
                getCollisionArea().setRotationDegrees(_rotation);
        }

        _tx.update(gameTime);
    }

    public void flap() {
        _ySpeed = _yFlapSpeedSeconds;
        _rotation = 0.0f;
    }

    public void gamebegin() {
        _started = true;
    }
}
