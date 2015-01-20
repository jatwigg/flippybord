package com.xazux._2dlib.sprites;

import com.xazux._2dlib.sprites.components.CollisionArea;
import com.xazux._2dlib.sprites.components.Texture;
import com.xazux._2dlib.touch.TouchState;
import com.xazux._2dlib.touch.Touchable;

/**
 * Created by josh on 19/01/15.
 */
public class TouchableSprite extends Sprite implements Touchable {
    private boolean _startTouched = false;

    public TouchableSprite(Texture texture, CollisionArea collisionArea) {
        super(texture, collisionArea);
    }

    @Override
    public void OnTouchMove(TouchState event) {

    }

    @Override
    public void OnTouchStart(TouchState event) {
        _startTouched = true;
    }

    @Override
    public void OnTouchOver(TouchState event) {
        _startTouched = false;
    }

    @Override
    public void OnTouchCancel() {
        _startTouched = false;
    }

    public boolean isTouched() {
        return _startTouched;
    }
}
