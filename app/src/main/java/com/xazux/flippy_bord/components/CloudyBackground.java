package com.xazux.flippy_bord.components;

import android.graphics.Canvas;
import android.graphics.Color;

import com.xazux._2dlib.I2DGameContext;
import com.xazux._2dlib.JMath;
import com.xazux._2dlib._2DGameActivity;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.Texture;
import com.xazux.flippy_bord.R;

import java.util.ArrayList;

/**
 * Created by josh on 17/01/15.
 */
public class CloudyBackground {
    private final CRect SCREENSIZE;
    private final float DISTANCE_BETWEEN_CLOUDS;
    private final Texture TX_CLOUD;
    private final int _backColour = Color.rgb(81, 172, 230);
    private final ArrayList<Cloud> _clouds = new ArrayList<>();

    public CloudyBackground(I2DGameContext context) {
        SCREENSIZE = context.getScreenDimensions();
        TX_CLOUD = new Texture(context.loadBitmap(R.drawable.cloud));
        DISTANCE_BETWEEN_CLOUDS = SCREENSIZE.getWidth() * 0.25f;
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
        if (_clouds.size() == 0 || (SCREENSIZE.getRight() - lastCloud._area.getRight() ) > DISTANCE_BETWEEN_CLOUDS)
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
            return (_area.getRight() < 0);
        }
    }
}
