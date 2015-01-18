package com.xazux.flippy_bord;

import com.xazux._2dlib.JMath;
import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.sprites.Sprite;
import com.xazux._2dlib.sprites.components.Animation;
import com.xazux._2dlib.sprites.components.CCircle;
import com.xazux._2dlib.sprites.components.CollisionArea;

/**
 * Created by josh on 09/01/15.
 */
public class Bord extends Sprite {
    private float _yFlapSpeedSeconds, _ySpeed = 0.0f, _rotation = 0.0f;
    private Animation _tx;
    private boolean _started = false;
    private float _screenHeight;
    private final float GRAVITY = 600.0f; //todo perhaps make this based on height?
    private final float ROTATION_SPEED = 100.0f;
    private CCircle _collisionArea;

    public Bord(Animation texture, CCircle drawArea, float screenHeight) {
        super(texture, drawArea);
        _tx = texture;
        _tx.setStretch(false);
        _tx.Play();
        _yFlapSpeedSeconds = screenHeight * -0.5f;
        _screenHeight = screenHeight;
        _collisionArea = new CCircle(drawArea.getCenterX(), drawArea.getCenterY(), drawArea.getRadius() * 0.7f);
    }

    public void update(GameTime gameTime) {
        if (_started) {

            CollisionArea positionArea = super.getCollisionArea();

            _ySpeed = JMath.Clamp(_ySpeed + (GRAVITY * gameTime.getElapsedSeconds()), _yFlapSpeedSeconds, GRAVITY);
            _ySpeed += (GRAVITY * gameTime.getElapsedSeconds());
            float distanceTraveled = _ySpeed * gameTime.getElapsedSeconds();

            float yPos = positionArea.getBottom() + distanceTraveled;

            if (yPos > _screenHeight) {
                float f = _screenHeight - positionArea.getBottom();
                positionArea.offsetBy(0, f);
            }
            else if (yPos < positionArea.getHeight()) {
                float f = positionArea.getTop() * -1.0f;
                positionArea.offsetBy(0, f);
            }
            else {
                positionArea.offsetBy(0, distanceTraveled);
            }

            _rotation += ROTATION_SPEED * gameTime.getElapsedSeconds();
            if (_rotation > -45 && _rotation < 90)
                positionArea.setRotationDegrees(_rotation);

            // update collision area
            _collisionArea.offsetSoCenterIs(positionArea.getCenterX(), positionArea.getCenterY());
        }

        _tx.update(gameTime);
    }

    @Override
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
}
