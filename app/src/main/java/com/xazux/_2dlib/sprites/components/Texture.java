package com.xazux._2dlib.sprites.components;

import com.xazux._2dlib.components.GameTime;

import android.graphics.*;

public class Texture 
{
	protected final float DEAFULT_ORIGIN_X = 0.5f, DEAFULT_ORIGIN_Y = 0.5f;
	protected Bitmap m_bitmap;
	protected Paint m_paint;
	protected Matrix m_renderMatrix;
    private boolean _stretch = true;

    public Texture(Bitmap bitmap, Paint paint)
	{
		m_bitmap = bitmap;
		m_paint = paint;
		m_renderMatrix = new Matrix();
	}
	
	public Texture(Bitmap bitmap)
	{
		this(bitmap,new Paint());
	}

    public Texture(Bitmap bitmap, boolean stretch) {
        this(bitmap);
        setStretch(stretch);
    }

    public void update(GameTime gameTime)
	{
		// we don't update but derived classes do
	}
	
	public void render(Canvas canvas, Matrix matrix, Paint paint)
	{
		canvas.drawBitmap(m_bitmap, matrix, paint);
	}
	
	public void render(Canvas canvas, Matrix matrix)
	{
		this.render(canvas, matrix, m_paint);
	}
	
	public void render(Canvas canvas, CollisionArea collisionArea, Paint paint)
	{
		// work out origin point
		float originX = collisionArea.getWidth() * collisionArea.getOrigin().getX();
		float originY = collisionArea.getHeight() * collisionArea.getOrigin().getY();
		
		m_renderMatrix.reset();

        if (_stretch) {
            //cw/bw=scalar so that scaler*bw = cw
            m_renderMatrix.postScale((float) collisionArea.getWidth() / (float) m_bitmap.getWidth(),
                    (float) collisionArea.getHeight() / (float) m_bitmap.getHeight());
        }
        else {
            float x = collisionArea.getWidth() / (float) m_bitmap.getWidth();
            float y = collisionArea.getHeight() / (float) m_bitmap.getHeight();
            if (y > x)
                x = y;
            m_renderMatrix.postScale(x, x);
        }

		// translate so origin is at 0,0
		m_renderMatrix.postTranslate(-originX, -originY);
		// rotate around origin
		m_renderMatrix.postRotate(collisionArea.getRotationDegrees());
		// translate to position + origin we translated to zero
		m_renderMatrix.postTranslate(collisionArea.getLeft() + originX,
				collisionArea.getTop() + originY);
		// done
		render(canvas, m_renderMatrix, paint);
	}
	
	public void render(Canvas canvas, CollisionArea collisionArea)
	{
		this.render(canvas, collisionArea, m_paint);
	}
	
	public Paint getPaint()
	{
		return m_paint;
	}
	
	public void setPaint(Paint paint)
	{
		m_paint = paint;
	}

	public int getWidth() 
	{
		return m_bitmap.getWidth();
	}
	
	public int getHeight() 
	{
		return m_bitmap.getHeight();
	}
	
	public int getHeightIfWidthIs(int ifWidthIs)
	{
		float y = m_bitmap.getHeight();
		float x = m_bitmap.getWidth();
		
		float ratio = y / x;
		
		return (int)((ratio * ifWidthIs) + 0.5f);
	}
	
	public int getWidthIfHeightIs(int ifHeightIs)
	{
		float y = m_bitmap.getHeight();
		float x = m_bitmap.getWidth();
		
		float ratio = x / y;
		
		return (int)((ratio * ifHeightIs) + 0.5f);	
	}

    public void setStretch(boolean s)
    {
        _stretch = s;
    }

    public boolean getStrecth()
    {
        return _stretch;
    }
}
