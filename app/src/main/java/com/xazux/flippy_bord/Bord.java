package com.xazux.flippy_bord;

import com.xazux._2dlib.JMath;
import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.sprites.Sprite;
import com.xazux._2dlib.sprites.components.Animation;
import com.xazux._2dlib.sprites.components.CollisionArea;

/**
 * Created by josh on 09/01/15.
 */
public class Bord extends Sprite {
    private float _yDecreaseSpeedSeconds, _yFlapSpeedSeconds, _ySpeed = 0.0f;
    private Animation _tx;
    private boolean _started = false;
    private float _rotation = 0.0f;

    public Bord(Animation texture, CollisionArea collisionArea, float screenHeight) {
        super(texture, collisionArea);
        _tx = texture;
        _tx.setStretch(true);
        _tx.Play();
        _yDecreaseSpeedSeconds = screenHeight * 0.05f;
        _yFlapSpeedSeconds = screenHeight * -0.02f;
    }

    public void update(GameTime gameTime) {
        if (_started) {
            _ySpeed += JMath.Clamp(_yDecreaseSpeedSeconds * gameTime.getElapsedSeconds(), -_yDecreaseSpeedSeconds, _yDecreaseSpeedSeconds);
            getCollisionArea().offsetBy(0, _ySpeed);
            _rotation += JMath.Clamp(_yDecreaseSpeedSeconds * gameTime.getElapsedSeconds(), 0, 90);
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
