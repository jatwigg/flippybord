package com.xazux.flippy_bord.components;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.xazux._2dlib.sprites.Sprite;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.CollisionArea;
import com.xazux._2dlib.sprites.components.Texture;
import com.xazux._2dlib.touch.TouchState;
import com.xazux._2dlib.touch.Touchable;

/**
 * Created by josh on 27/01/15.
 */
public abstract class TextButton extends Sprite implements Touchable {
    private CRect _area;
    private String _buttonText;
    private Paint _paintFore;

    public TextButton(String text, Texture backgroundTextOrNull, CRect area, Paint paintTextFore) {
        super(backgroundTextOrNull, area);
        _area = area; //TODO: this saves the cast, must be a better way of doing this though, maybe make Sprite use generics?
        _buttonText = text;
        _paintFore = paintTextFore;
    }

    @Override
    public void render(Canvas canvas) {
        if (getTexture() != null)
            super.render(canvas);
        _area.renderTextCenterCRect(canvas, _paintFore, _buttonText);
    }

    @Override
    public void OnTouchMove(TouchState event) {

    }

    @Override
    public void OnTouchStart(TouchState event) {

    }

    @Override
    public void OnTouchOver(TouchState event) {
        if (getCollisionArea().containsPoint(event.X, event.Y))
            onPress();
    }

    @Override
    public void OnTouchCancel() {

    }

    public abstract void onPress();
}
