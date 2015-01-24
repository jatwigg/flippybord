package com.xazux._2dlib.sprites.components;

import android.graphics.ColorFilter;
import android.graphics.Paint;

/**
 * Created by josh on 24/01/15.
 */
public class PaintBuilder {
    private final Paint _paint;

    protected PaintBuilder(Paint paint) {
        _paint = paint;
    }

    public static PaintBuilder create() {
        return new PaintBuilder(new Paint());
    }

    public static PaintBuilder create(int flags) {
        return new PaintBuilder(new Paint(flags));
    }

    public static PaintBuilder create(Paint paint) {
        return new PaintBuilder(new Paint(paint));
    }

    public Paint getPaint() {
        return _paint;
    }

    public PaintBuilder setColor(int color) {
        _paint.setColor(color);
        return this;
    }

    public PaintBuilder setAntiAlias(boolean aa) {
        _paint.setAntiAlias(aa);
        return this;
    }

    public PaintBuilder setColorFilter(ColorFilter filter) {
        _paint.setColorFilter(filter);
        return this;
    }

    public PaintBuilder setTextSize(float textSize) {
        _paint.setTextSize(textSize);
        return this;
    }
}
