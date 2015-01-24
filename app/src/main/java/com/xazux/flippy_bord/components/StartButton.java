package com.xazux.flippy_bord.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

import com.xazux._2dlib.sprites.TouchableSprite;
import com.xazux._2dlib.sprites.components.CollisionArea;
import com.xazux._2dlib.sprites.components.PaintBuilder;
import com.xazux._2dlib.sprites.components.Texture;
import com.xazux._2dlib.touch.TouchState;

/**
 * Created by josh on 24/01/15.
 */
public abstract class StartButton extends TouchableSprite {
    private final Paint _paintPressed;

    public StartButton(Texture texture, CollisionArea collisionArea) {
        super(texture, collisionArea);
        _paintPressed = PaintBuilder.create().setAntiAlias(true)
                .setColor(Color.WHITE).setColorFilter(new LightingColorFilter(Color.RED, 0)).getPaint();
    }

    private boolean startTouched = false;

    @Override
    public void OnTouchMove(TouchState event) {
        startTouched = getCollisionArea().containsPoint(event.X, event.Y);
    }

    @Override
    public void OnTouchStart(TouchState event) {
        startTouched = true;
    }

    @Override
    public void OnTouchOver(TouchState event) {
        startTouched = false;
        if (getCollisionArea().containsPoint(event.X, event.Y)) {
            onPress();
        }
    }

    @Override
    public void OnTouchCancel() {
        startTouched = false;
    }

    @Override
    public void render(Canvas canvas) {
        if (startTouched)
            getTexture().render(canvas, getCollisionArea(), _paintPressed);
        else
            super.render(canvas);
    }

    public abstract void onPress();
}
