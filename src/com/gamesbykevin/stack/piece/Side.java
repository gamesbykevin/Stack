package com.gamesbykevin.stack.piece;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.stack.thread.MainThread;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;

/**
 * The side is part of a piece that makes up a piece
 * @author GOD
 */
public class Side extends Path implements Disposable
{
	/**
	 * The different types of sides
	 * @author GOD
	 */
	public enum Type
	{
		Top,
		East,
		South
	}
	
	//which type of side is this
	private final Type type;
	
	//is this side flagged as dead?
	private boolean dead = false;
	
	//how many frames has this side been dead
	private float frames = 0;
	
	/**
	 * How many frames is this side allowed to be dead (visible)
	 */
	private static final float FRAMES_LIMIT = (MainThread.FPS / 4);
	
	/**
	 * 100% visible
	 */
	private static final int OPAQUE = 255;

	//the boundaries of this side
	private float colW, colE, rowN, rowS;	
	
	//is this side complete?
	private boolean complete = false;
	
	/**
	 * Default Constructor
	 */
	public Side(final Type type) 
	{
		//call parent constructor
		super();
		
		this.type = type;
	}
	
	/**
	 * Assign the boundary of this side so we know how to render
	 * @param colW the west most column
	 * @param colE the east most column
	 * @param rowN the north most row
	 * @param rowS the south most row
	 */
	public void setBoundary(final float colW, final float colE, final float rowN, final float rowS)
	{
		this.colW = colW;
		this.colE = colE;
		this.rowN = rowN;
		this.rowS = rowS;
	}
	
	/**
	 * Flag the side completed
	 */
	public void flagComplete()
	{
		this.complete = true;
	}
	
	/**
	 * Is this side complete?
	 * @return true if this side is done moving, false otherwise
	 */
	public boolean isComplete()
	{
		return this.complete;
	}
	
	/**
	 * Flag the side as dead
	 */
	public void flagDead()
	{
		this.dead = true;
	}
	
	/**
	 * Is the side dead?
	 * @return true = yes, false = otherwise
	 */
	public boolean isDead()
	{
		return this.dead;
	}
	
	@Override
	public void dispose() 
	{
		//do any cleanup here
	}
	
	/**
	 * Get the type of side
	 * @return The type of side this is
	 */
	protected Type getType()
	{
		return this.type;
	}
	
	/**
	 * Calculate the coordinates of the side
	 * @param piece The piece object used for reference when calculating coordinates
	 * @throws Exception
	 */
	private void calculate(final Piece piece) throws Exception
	{
		//reset all existing points
		super.reset();
		
		//assign the coordinates accordingly
		switch (getType())
		{
			case Top:
				super.moveTo(getLocationX(colW, rowN, piece), getLocationY(colW, rowN, piece));
				super.lineTo(getLocationX(colW, rowN, piece), getLocationY(colW, rowN, piece));
				super.lineTo(getLocationX(colE, rowN, piece), getLocationY(colE, rowN, piece));
				super.lineTo(getLocationX(colE, rowS, piece), getLocationY(colE, rowS, piece));
				super.lineTo(getLocationX(colW, rowS, piece), getLocationY(colW, rowS, piece));
				super.lineTo(getLocationX(colW, rowN, piece), getLocationY(colW, rowN, piece));
				break;
				
			case East:
				super.moveTo(getLocationX(colE, rowN, piece), getLocationY(colE, rowN, piece));
				super.lineTo(getLocationX(colE, rowN, piece), getLocationY(colE, rowN, piece));
				super.lineTo(getLocationX(colE, rowN, piece), getLocationY(colE, rowN, piece) + (PieceHelper.ROW_HEIGHT / 2));
				super.lineTo(getLocationX(colE, rowS, piece), getLocationY(colE, rowS, piece) + (PieceHelper.ROW_HEIGHT / 2));
				super.lineTo(getLocationX(colE, rowS, piece), getLocationY(colE, rowS, piece));
				super.lineTo(getLocationX(colE, rowN, piece), getLocationY(colE, rowN, piece));
				break;
				
			case South:
				super.moveTo(getLocationX(colW, rowS, piece), getLocationY(colW, rowS, piece));
				super.lineTo(getLocationX(colW, rowS, piece), getLocationY(colW, rowS, piece));
				super.lineTo(getLocationX(colE, rowS, piece), getLocationY(colE, rowS, piece));
				super.lineTo(getLocationX(colE, rowS, piece), getLocationY(colE, rowS, piece) + (PieceHelper.ROW_HEIGHT / 2));
				super.lineTo(getLocationX(colW, rowS, piece), getLocationY(colW, rowS, piece) + (PieceHelper.ROW_HEIGHT / 2));
				super.lineTo(getLocationX(colW, rowS, piece), getLocationY(colW, rowS, piece));
				break;
				
			default:
				throw new Exception("Type not handled here: " + getType().toString());
		}
	}
	
	/**
	 * Get the x-coordinate location
	 * @param col
	 * @param row
	 * @param piece
	 * @return the x-coordinate
	 */
	protected static float getLocationX(final double col, final double row, final Piece piece)
	{
		final float adjustCol = (float) (col + piece.getCol());
		final float adjustRow = (float) (row + piece.getRow());
		
		return (float) (((adjustCol - adjustRow) * (PieceHelper.COLUMN_WIDTH / 2)) + piece.getX());
	}
	
	/**
	 * Get the y-coordinate location
	 * @param col
	 * @param row
	 * @param piece
	 * @return the y-coordinate
	 */
	protected static float getLocationY(final double col, final double row, final Piece piece)
	{
		final float adjustCol = (float) (col + piece.getCol());
		final float adjustRow = (float) (row + piece.getRow());
		
		return (float) (((adjustCol + adjustRow) * (PieceHelper.ROW_HEIGHT / 4)) + piece.getY());
	}	
	
	/**
	 * Update the frame progression, if dead
	 */
	public void update()
	{
		if (isDead() && this.frames < FRAMES_LIMIT)
			this.frames++;
	}
	
	/**
	 * Has the dead side finished its animation
	 * @return true if flagged dead and frames meet/exceed frame limit, false otherwise
	 */
	protected boolean hasDeadCompleted()
	{
		return (isDead() && this.frames >= FRAMES_LIMIT);
	}
	
	/**
	 * Render the side
	 * @param canvas
	 * @param paint
	 * @throws Exception 
	 */
	public void render(final Canvas canvas, final Paint paint, final Piece piece) throws Exception
	{
		//if we are not to be displayed don't continue
		if (hasDeadCompleted())
			return;
		
		//if there are no dimensions we won't render
		if (this.colW - this.colE == 0 || this.rowN - this.rowS == 0)
			return;
		
		//calculate the coordinates for rendering if we are still moving
		if (!isComplete())
			calculate(piece);
		
		//the alpha transparency
		int alpha = OPAQUE;
		
		//calculate transparency accordingly
		if (isDead())
		{
			//calculate our progress
			final float progress = (1.0f - (frames / FRAMES_LIMIT));
			
			//set the transparency accordingly
			alpha = (int)(progress * OPAQUE);
		}
		
		//render the shape
		paint.setARGB(alpha, 0, 255, 0);
		paint.setStyle(Style.FILL);
		canvas.drawPath(this, paint);
		
		//render the outline of the shape
		paint.setARGB(alpha, 0, 0, 0);
		paint.setStyle(Style.STROKE);
		canvas.drawPath(this, paint);
	}
}