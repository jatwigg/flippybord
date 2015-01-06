package com.xazux._2dlib;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)    {
            w.getDefaultDisplay().getSize(size);
            Measuredwidth = size.x;
            Measuredheight = size.y;
        }else{
            Display d = w.getDefaultDisplay();
            Measuredwidth = d.getWidth();
            Measuredheight = d.getHeight();
        }
        return new CRect(0, 0, Measuredwidth, Measuredheight);
    }
}
