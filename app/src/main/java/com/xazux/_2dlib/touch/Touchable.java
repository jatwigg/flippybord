package com.xazux._2dlib.touch;

import com.xazux._2dlib.sprites.components.CollisionArea;

public interface Touchable {
    public CollisionArea getCollisionArea();

    public void OnTouchMove(TouchState event);

    public void OnTouchStart(TouchState event);

    public void OnTouchOver(TouchState event);

    public void OnTouchCancel();
}
