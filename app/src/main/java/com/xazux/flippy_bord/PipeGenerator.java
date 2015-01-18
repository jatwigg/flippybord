package com.xazux.flippy_bord;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.xazux._2dlib.JMath;
import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.CollisionArea;
import com.xazux._2dlib.sprites.components.Texture;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by josh on 12/01/15.
 */
public class PipeGenerator {
    private final ScoreBoard SCOREBOARD;
    private final Texture TX_BODY, TX_OUTLET;
    private final ArrayList<Pipe> _pipes = new ArrayList<>();
    private final float X_PIPE_SPACE, PIPE_WIDTH, SCROLL_SPEED, Y_PIPE_SPACE, BORD_POSITION_X;
    private final CRect BODY_RECT, OUTLET_RECT, SCREENSIZE;
    private final Random RANDOM = new Random();

    public PipeGenerator(Resources resources, CRect screenSize, float bordPositionX, ScoreBoard scoreBoard) {
        BORD_POSITION_X = bordPositionX;
        SCOREBOARD = scoreBoard;

        TX_BODY = new Texture(BitmapFactory.decodeResource(resources, R.drawable.pipe));
        TX_BODY.setStretch(true);

        TX_OUTLET = new Texture(BitmapFactory.decodeResource(resources, R.drawable.pipe_top));
        TX_OUTLET.setStretch(true);

        SCREENSIZE = screenSize;
        X_PIPE_SPACE = SCREENSIZE.getWidth() * 0.5f;
        SCROLL_SPEED = SCREENSIZE.getWidth() * -0.4f;
        PIPE_WIDTH = SCREENSIZE.getWidth() * 0.2f;
        Y_PIPE_SPACE = SCREENSIZE.getHeight() * 0.4f;
        BODY_RECT = new CRect(SCREENSIZE.getRight(), SCREENSIZE.getTop(), SCREENSIZE.getRight() + PIPE_WIDTH, SCREENSIZE.getBottom());
        OUTLET_RECT = new CRect(0, 0, PIPE_WIDTH, TX_OUTLET.getHeightIfWidthIs(JMath.Round(PIPE_WIDTH)));
    }

    public void render(Canvas canvas) {
        for (Pipe p : _pipes)
            p.render(canvas);
    }

    public void update(GameTime gameTime) {
        float movement = SCROLL_SPEED * gameTime.getElapsedSeconds();
        for (int i = 0; i < _pipes.size(); ++i)
           if (_pipes.get(i).update(movement)) {
                _pipes.remove(i--);
           }

        if (_pipes.size() == 0 || (SCREENSIZE.getRight() -  _pipes.get(_pipes.size() - 1)._areaOutlet.getRight()) > X_PIPE_SPACE) {

            float b1 = 100 + RANDOM.nextInt((int)(SCREENSIZE.getHeight() * 0.5f));
            float t2 = b1 + Y_PIPE_SPACE;

            CRect p1 = new CRect(BODY_RECT.getLeft(), BODY_RECT.getTop(), BODY_RECT.getRight(), b1);
            CRect p2 = new CRect(BODY_RECT.getLeft(), t2, BODY_RECT.getRight(), BODY_RECT.getBottom());

            _pipes.add(new Pipe(p1, true));
            _pipes.add(new Pipe(p2, false));
        }
    }

    public boolean intersects(CollisionArea cArea)
    {
        for (Pipe pipe : _pipes)
            if (pipe.intersects(cArea))
                return true;
        return false;
    }

    private class Pipe
    {
        private boolean _hasPassedBord = false;
        private CRect _areaOutlet, _areaBody;

        public Pipe(CRect area, boolean isTop)
        {
            _areaBody = area;
            _areaOutlet = OUTLET_RECT.clone();
            if (isTop) {
                _areaOutlet.offsetTo(_areaBody.getLeft(), _areaBody.getBottom() - _areaOutlet.getHeight());
                _areaOutlet.setRotationDegrees(180);
                _areaBody.offsetBy(0, _areaOutlet.getHeight() * -0.5f);
                _areaBody.setRotationDegrees(180);
            }
            else {
                _areaOutlet.offsetTo(_areaBody.getLeft(), _areaBody.getTop());
                _areaBody.offsetBy(0, _areaOutlet.getHeight() * 0.5f);
            }
        }

        public boolean update(float movement)
        {
            _areaOutlet.offsetBy(movement, 0);
            _areaBody.offsetBy(movement, 0);
            if (_areaOutlet.getRight() < 0)
                return true; //offscreen
            else if (!_hasPassedBord && _areaOutlet.getRight() < BORD_POSITION_X) {
                SCOREBOARD.scorePipePass();
                _hasPassedBord = true;
            }

            return false;
        }

        public void render(Canvas canvas)
        {
            TX_BODY.render(canvas, _areaBody);
            TX_OUTLET.render(canvas, _areaOutlet);
        }

        public boolean intersects(final CollisionArea cArea) {
            return cArea.intersects(_areaBody) || cArea.intersects(_areaOutlet);
        }
    }
}
