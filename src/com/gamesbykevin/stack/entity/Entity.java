package com.gamesbykevin.stack.entity;

import android.graphics.Canvas;

public abstract class Entity extends com.gamesbykevin.androidframework.base.Entity
{
	public Entity() 
	{
		super();
	}

	/**
	 * Is the coordinate contained within this entity?
	 * @param x
	 * @param y
	 * @return true if the specified (x,y) coordinate is within the entity, false otherwise
	 */
	public boolean contains(final double x, final double y)
	{
		//what is our result
		boolean result = false;
		
		//store coordinates before we offset
		final double ex = super.getX();
		final double ey = super.getY();
		
		//offset coordinates
		super.setX(ex - (getWidth() / 2));
		super.setY(ey - (getHeight() / 2));

		//if the coordinate is within, our result is true
		if (x >= getX() && x <= getX() + getWidth() && 
			y >= getY() && y <= getY() + getHeight())
			result = true;
			
		//restore location
		super.setX(ex);
		super.setY(ey);
		
		//return our result
		return result;
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		//store coordinates before we offset
		final double x = super.getX();
		final double y = super.getY();
		
		//offset coordinates
		super.setX(x - (getWidth() / 2));
		super.setY(y - (getHeight() / 2));
		
		//now we can render
		super.render(canvas);
		
		//restore location
		super.setX(x);
		super.setY(y);
	}
}