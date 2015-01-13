package com.xazux.flippy_bord;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

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
    private Texture pipeTx;
    private ArrayList<CollisionArea> _pipes;
    private CRect _screenSize;
    private float _distanceBetweenPipes;
    private float _flightSpeed;
    private Paint debugPaint;
    private float _pipeWidth;
    private float _pipeSpace;
    private CRect _pipeStart;
    private Random _random;

    public PipeGenerator(Resources resources, CRect screenSize) {
        pipeTx = new Texture(BitmapFactory.decodeResource(resources, R.drawable.background));
        _screenSize = screenSize;
        _pipes = new ArrayList<CollisionArea>();
        _distanceBetweenPipes = _screenSize.getWidth() * 0.5f;
        _flightSpeed = _screenSize.getWidth() * -0.4f;
        _pipeWidth = _screenSize.getWidth() * 0.2f;
        _pipeSpace = _screenSize.getHeight() * 0.3f;
        _pipeStart = new CRect(_screenSize.getRight(), _screenSize.getTop(), _screenSize.getRight() + _pipeWidth, _screenSize.getBottom());
        _random = new Random();

        debugPaint = new Paint();
        debugPaint.setColor(Color.BLACK);
        debugPaint.setAntiAlias(true);
    }

    public void render(Canvas canvas) {
        for (CollisionArea p : _pipes) {
            //p.render(canvas);
            p.render(canvas, debugPaint);
        }
    }

    public void update(GameTime gameTime) {
        float movement = _flightSpeed * gameTime.getElapsedSeconds();
        for (int i = 0; i < _pipes.size(); ++i) {
            _pipes.get(i).offsetBy(movement, 0);
            if (_pipes.get(i).getRight() < 0) {
                _pipes.remove(i);
                --i;
            }
        }
        if (_pipes.size() == 0 || (_screenSize.getRight() -  _pipes.get(_pipes.size() - 1).getRight()) > _distanceBetweenPipes) {

            float b1 = 100 + _random.nextInt((int)(_screenSize.getHeight() * 0.68f));
            float t2 = b1 + _pipeSpace;

            CRect p1 = new CRect(_pipeStart.getLeft(), _pipeStart.getTop(), _pipeStart.getRight(), b1);
            CRect p2 = new CRect(_pipeStart.getLeft(), t2, _pipeStart.getRight(), _pipeStart.getBottom());
            _pipes.add(p1);
            _pipes.add(p2);
        }
    }
}
