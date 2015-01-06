package com.xazux._2dlib;

import com.xazux._2dlib.components.GameTime;

import android.content.res.Resources;
import android.graphics.Canvas;

public interface IDraw
{
	void onDraw(Canvas canvas);
	void onUpdate(GameTime gameTime);
	void loadContent(Resources resources);
	void onGameOver();
}
