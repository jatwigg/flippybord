package com.xazux._2dlib;

import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Typeface;

public class FontStore
{
	protected Typeface m_typeFaceSoviet1;
	public final static String SOVIET2_LOCATION = "fonts/soviet2.ttf";

	public FontStore(AssetManager assetmanager)
	{
		m_typeFaceSoviet1 = Typeface.createFromAsset(assetmanager, SOVIET2_LOCATION);  
	}
	
	public Typeface getSoviet1()
	{
		return m_typeFaceSoviet1;
	}
	
	public Paint getSoviet1Paint(int r, int g, int b, int a, float size)
	{
		Paint paint = new Paint();
		paint.setARGB(r, g, b, a);
		paint.setTypeface(m_typeFaceSoviet1);
		paint.setTextSize(size);
		return paint;
	}
}
