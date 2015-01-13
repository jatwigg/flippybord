package com.xazux.flippy_bord;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.xazux._2dlib.Helper;
import com.xazux._2dlib._2DGameActivity;
import com.xazux._2dlib.components.GameTime;
import com.xazux._2dlib.sprites.Sprite;
import com.xazux._2dlib.sprites.components.Animation;
import com.xazux._2dlib.sprites.components.CRect;
import com.xazux._2dlib.sprites.components.CollisionArea;
import com.xazux._2dlib.sprites.components.Texture;
import com.xazux._2dlib.touch.Touchable;

/**
 * Created by josh on 08/01/15.
 */
public class Game extends _2DGameActivity implements Touchable {
    private Sprite _background;
    private Bord _bord;
    private Paint debugPaint;
    private PipeGenerator _pipes;
    private boolean _started = false;

    @Override
    public void onInit() {
        //background
        CRect screenSize = Helper.GetScreenDimensions(this);
        Texture tx = new Texture(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        _background = new Sprite(tx, screenSize);
        _background.getTexture().setStretch(false); //so background isn 't squashed
        //bord
        float hs = screenSize.getWidth() * 0.075f;
        CRect crect = CRect.CreateUsingWidthAndHeight(screenSize.getWidth() * 0.3f, screenSize.getCenterY() - hs, hs+hs, hs+hs);

        Animation anim = new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.bordclear), 5, Animation.AnimationType.FORWARD_BACKWARD_LOOP, 0.1f);
        _bord = new Bord(anim, crect, screenSize.getHeight());
        debugPaint = new Paint();
        debugPaint.setColor(Color.GREEN);
        //pipe generator
        _pipes = new PipeGenerator(getResources(), screenSize);

        //screen
        getTouchHandler().RegisterTouchable(this);
    }

    @Override
    public void onUpdate(GameTime gameTime) {
        if (_started)
            _pipes.update(gameTime);
        _bord.update(gameTime);
    }

    @Override
    public void onDraw(Canvas canvas) {
        _background.render(canvas);
        _pipes.render(canvas);
        _bord.getCollisionArea().render(canvas, debugPaint);
        _bord.render(canvas);
    }

    @Override
    public void onFinished() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_left, R.anim.leave_left);
    }

    @Override
    public CollisionArea getCollisionArea() {
        return _background.getCollisionArea();
    }

    @Override
    public void OnTouchMove(MotionEvent event, float x, float y) {

    }

    @Override
    public void OnTouchStart(MotionEvent event, float x, float y) {
        if (!_started) {
            _started = true;
            _bord.gamebegin();
        }
        _bord.flap();
    }

    @Override
    public void OnTouchOver(MotionEvent event, float x, float y) {

    }

    @Override
    public void OnTouchCancel(MotionEvent event) {

    }
}
