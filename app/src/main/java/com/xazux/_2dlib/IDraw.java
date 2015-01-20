package com.xazux._2dlib;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.xazux._2dlib.components.GameTime;

public interface IDraw
{
	void onDraw(Canvas canvas);
	void onUpdate(GameTime gameTime);
	void loadContent(Resources resources);
	void onGameOver();
}
