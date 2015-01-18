package com.xazux.flippy_bord;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import com.xazux._2dlib.JMath;
import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.Texture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josh on 17/01/15.
 */
public class CloudyBackground {
    private final CRect SCREENSIZE;
    private final float DISTANCE_BETWEEN_CLOUDS;
    private final Texture TX_CLOUD;
    private final int _backColour = Color.rgb(81, 172, 230);
    private final ArrayList<Cloud> _clouds = new ArrayList<>();

    public CloudyBackground(Resources resources, CRect screenSize) {
        SCREENSIZE = screenSize;
        TX_CLOUD = new Texture(BitmapFactory.decodeResource(resources, R.drawable.cloud));
        DISTANCE_BETWEEN_CLOUDS = screenSize.getWidth() * 0.25f;
    }

    public void update(float elapsedSeconds)
    {
        Cloud lastCloud = null, cloud;
        for(int i = 0; i < _clouds.size(); ++i) {
            cloud = _clouds.get(i);
            if (cloud.update(elapsedSeconds)) {
                _clouds.remove(i--);
            }
            else if (lastCloud == null || lastCloud._area.getRight() < cloud._area.getRight()) {
                lastCloud = _clouds.get(i);
            }
        }
        if (_clouds.size() == 0 ||
                (SCREENSIZE.getRight() - lastCloud._area.getRight() ) > DISTANCE_BETWEEN_CLOUDS)
        {
            _clouds.add(new Cloud());
        }
    }

    public void render(Canvas canvas)
    {
        canvas.drawColor(_backColour);
        for (Cloud cloud : _clouds)
            cloud.render(canvas);
    }

    private class Cloud
    {
        private float _speed;
        private CRect _area;

        public Cloud() {
            float w;
            int r = JMath.GetRandom().nextInt(3);
            switch (r)
            {
                case 0:
                    w = SCREENSIZE.getWidth() * 0.1f;
                    _speed = SCREENSIZE.getWidth() * -0.05f;
                    break;
                case 1:
                    w = SCREENSIZE.getWidth() * 0.2f;
                    _speed = SCREENSIZE.getWidth() * -0.1f;
                    break;
                default:
                    w = SCREENSIZE.getWidth() * 0.25f;
                    _speed = SCREENSIZE.getWidth() * -0.125f;
                    break;

            }
            _area = CRect.CreateUsingWidthAndHeight(SCREENSIZE.getRight(), JMath.GetRandom().nextInt((int)(SCREENSIZE.getHeight() * 0.7f)), w, TX_CLOUD.getHeightIfWidthIs(JMath.Round(w)));
        }

        public void render(Canvas canvas) {
            TX_CLOUD.render(canvas, _area);
        }

        public boolean update(float elapsedSeconds) {
            _area.offsetBy(_speed * elapsedSeconds, 0);
            if (_area.getRight() < 0)
                return true;
            return false;
        }
    }
}
