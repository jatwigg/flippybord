package com.xazux._2dlib.sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.CollisionArea;
import com.xazux._2dlib.sprites.components.Texture;

import org.w3c.dom.Text;

/**
 * Created by josh on 09/01/15.
 */
public class Sprite {
    private Texture _texture;
    private CollisionArea _collisionArea;

    public Sprite(Texture texture, CollisionArea collisionArea) {
        _texture = texture;
        _collisionArea = collisionArea;
    }

    public void render(Canvas canvas) {
        _texture.render(canvas, _collisionArea);
    }

    public Texture getTexture() {
        return _texture;
    }

    public CollisionArea getCollisionArea() {
        return _collisionArea;
    }
}
