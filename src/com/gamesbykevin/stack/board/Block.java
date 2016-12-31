package com.gamesbykevin.stack.board;

import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.stack.panel.GamePanel;
import com.gamesbykevin.stack.thread.MainThread;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Each block makes up a piece 
 * @author GOD
 *
 */
public class Block extends Entity implements Disposable 
{
	/**
	 * The pixel dimension of a single block that makes up this piece
	 */
	public static final int COLUMN_WIDTH = 25;

	/**
	 * The dimensions of a single block that makes up this piece
	 */
	public static final int ROW_HEIGHT = COLUMN_WIDTH;
	
	//is this block dead?
	private boolean dead = false;

	//how many frames have we been dead
	private float frames = 0;
	
	/**
	 * The number of frames a dead block is displayed
	 */
	private static final float FRAME_LIMIT = (MainThread.FPS / 3);
	
	public Block() 
	{
		//set the dimensions
		super.setHeight(ROW_HEIGHT);
		super.setWidth(COLUMN_WIDTH);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
	}
	
	public void update()
	{
		if (isDead())
		{
			//keep track of how many frames we are dead
			if (frames < FRAME_LIMIT)
				frames++;
		}
	}
	
	public boolean isDead()
	{
		return this.dead;
	}
	
	public void flagDead()
	{
		this.dead = true;
	}
	
	/**
	 * Is dead completed?
	 * @return true if the block is flagged dead and the frames count has met the limit for the animation, false otherwise
	 */
	public boolean deadCompleted()
	{
		return (isDead() && frames >= FRAME_LIMIT);
	}
	
	@Override
	public void render(final Canvas canvas, final Bitmap image, final Paint paint) throws Exception
	{
		//if dead and past the frames limit, no need to continue
		if (isDead() && frames >= FRAME_LIMIT)
			return;
		
		//skip if it won't even be rendered on screen
		if (getY() > GamePanel.HEIGHT)
			return;
		if (getX() + getWidth() < 0)
			return;
		if (getX() > GamePanel.WIDTH)
			return;
		
		if (paint != null && isDead())
		{
			//calculate the progress
			float progress = 1.0f - (frames / FRAME_LIMIT);
			
			//set transparency 255 opaque, 0 invisible
			paint.setAlpha((int)(progress * 255));
			
			//render the graphic
			super.render(canvas, image, paint);
		}
		else
		{
			//render the graphic
			super.render(canvas, image, null);
		}
	}
}