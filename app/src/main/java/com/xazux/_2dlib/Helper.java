package com.xazux._2dlib;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import com.xazux._2dlib.sprites.components.CRect;

/**
 * Created by josh on 06/01/15.
 */
public class Helper {
    public static CRect GetScreenDimensions(Activity activity) {
        int Measuredwidth = 0;
        int Measuredheight = 0;
        Point size = new Point();
        WindowManager w = activity.getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            w.getDefaultDisplay().getSize(size);
            Measuredwidth = size.x;
            Measuredheight = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }
        return new CRect(0, 0, Measuredwidth, Measuredheight);
    }

    private static RectF bounds = new RectF();

    public static void RenderTextCenterCRect(Canvas canvas, Paint paint, String text, CRect areaRect) {

        bounds = new RectF(areaRect.getLeft(), areaRect.getTop(), areaRect.getRight(), areaRect.getBottom());
        // measure text width

        bounds.right = paint.measureText(text, 0, text.length());

        // measure text height
        bounds.bottom = paint.descent() - paint.ascent();

        bounds.left += (areaRect.getWidth() - bounds.right) / 2.0f;
        bounds.top += (areaRect.getHeight() - bounds.bottom) / 2.0f;

        canvas.drawText(text, bounds.left, bounds.top - paint.ascent(), paint);
    }

    public static void RenderTextCenterCRectShadow(Canvas canvas, Paint paint, Paint paintFore, String text, CRect areaRect) {

        bounds = new RectF(areaRect.getLeft(), areaRect.getTop(), areaRect.getRight(), areaRect.getBottom());
        // measure text width

        bounds.right = paint.measureText(text, 0, text.length());

        // measure text height
        bounds.bottom = paint.descent() - paint.ascent();

        bounds.left += (areaRect.getWidth() - bounds.right) / 2.0f;
        bounds.top += (areaRect.getHeight() - bounds.bottom) / 2.0f;

        for (int i = -5; i < 5; ++i)
            canvas.drawText(text, bounds.left + i, (bounds.top - paint.ascent()) + i, paint);
        canvas.drawText(text, bounds.left + 5, (bounds.top - paint.ascent()) + 5, paintFore);
    }
}
