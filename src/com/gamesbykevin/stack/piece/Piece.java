package com.gamesbykevin.stack.piece;

import java.util.ArrayList;
import java.util.List;

import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.stack.board.Board;
import com.gamesbykevin.stack.panel.GamePanel;
import com.gamesbykevin.stack.piece.Side.Type;
import com.gamesbykevin.stack.thread.MainThread;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;

/**
 * A single piece that we want to place on the board
 * @author GOD
 */
public class Piece extends Entity implements Disposable
{
	/**
	 * The default total columns & rows size of the starting piece
	 */
	public static final int DEFAULT_SIZE = 20;
	
	/**
	 * Which column or row should we start at?
	 */
	public static final int DEFAULT_START_POSITION = -(DEFAULT_SIZE * 2);
	
	/**
	 * The default velocity of a moving piece
	 */
	public static final float DEFAULT_VELOCITY = .5f; 
	
	/**
	 * The velocity of a non moving piece
	 */
	public static final float DEFAULT_VELOCITY_NONE = 0.0f;
	
	//do we stop moving the piece
	private boolean stop = false;
	
	//our paint object used to add transparency to blocks
	private Paint paint;
	
	//have we done the comparison yet
	private boolean compare = false;
	
	//was or is the piece moving vertical?
	private final boolean vertical;
	
	//the total number of columns and rows that make up this piece
	private final float cols, rows;
	
	//once we compare pieces, what is the final size
	private float colsFinal, rowsFinal;
	
	//the sides that make up the piece
	private List<Side> sides;
	
	//when we separate the piece, where is the new x,y coordinates
	private float cutX, cutY;
	
	/**
	 * Create piece of default size, moving in a random direction
	 */
	public Piece()
	{
		this(DEFAULT_SIZE, DEFAULT_SIZE, GamePanel.RANDOM.nextBoolean());
	}
	
	/**
	 * Create a copy of the specified piece size
	 * @param piece
	 */
	public Piece(final Piece piece)
	{
		this(piece.getColsFinal(), piece.getRowsFinal(), !piece.hasVerticalVelocity());
	}
	
	/**
	 * Create a new piece
	 * @param cols The number of columns that make up the piece
	 * @param rows The number of rows that make up the piece
	 */
	public Piece(final float cols, final float rows, final boolean vertical) 
	{
		//store the columns and rows
		this.cols = cols;
		this.rows = rows;
		
		//create a new paint object
		this.paint = new Paint();
		
		//assign default location
		super.setCol(0);
		super.setRow(0);
		
		//assign default velocity
		super.setDX(DEFAULT_VELOCITY_NONE);
		super.setDY(DEFAULT_VELOCITY_NONE);
		
		//set the starting x,y coordinates
		super.setX(Board.START_X);
		super.setY(Board.START_Y);
		
		//determine if the piece
		this.vertical = vertical;
		
		//pick a random starting location
		if (this.hasVerticalVelocity())
		{
			super.setRow(DEFAULT_START_POSITION);
			super.setDY(DEFAULT_VELOCITY);
		}
		else
		{
			super.setCol(DEFAULT_START_POSITION);
			super.setDX(DEFAULT_VELOCITY);
		}
		
		//create a new list containing the sides
		this.sides = new ArrayList<Side>();
		
		//create the sides
		PieceHelper.createSides(this, 0, getCols(), 0, getRows(), false, false);
	}
	
	/**
	 * 
	 * @return
	 */
	protected List<Side> getSides()
	{
		return this.sides;
	}
	
	/**
	 * Get the total starting columns
	 * @return The starting total number of columns that makes up the piece
	 */
	public float getCols()
	{
		return this.cols;
	}
	
	/**
	 * Get the total starting rows
	 * @return The starting total number of rows that makes up the piece
	 */
	public float getRows()
	{
		return this.rows;
	}
	
	/**
	 * 
	 * @param cutX
	 */
	public void setCutX(final float cutX)
	{
		this.cutX = cutX;
	}
	
	/**
	 * 
	 * @param cutY
	 */
	public void setCutY(final float cutY)
	{
		this.cutY = cutY;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getCutX()
	{
		return this.cutX;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getCutY()
	{
		return this.cutY;
	}
	
	/**
	 * 
	 * @param colsFinal
	 */
	public void setColsFinal(final float colsFinal)
	{
		this.colsFinal = colsFinal;
	}
	
	/**
	 * 
	 * @param rowsFinal
	 */
	public void setRowsFinal(final float rowsFinal)
	{
		this.rowsFinal = rowsFinal;
	}
	
	/**
	 * Get the final column count
	 * @return The final number of columns after comparing this piece to another
	 */
	public float getColsFinal()
	{
		return this.colsFinal;
	}
	
	/**
	 * Get the final row count
	 * @return The final number of rows after comparing this piece to another
	 */
	public float getRowsFinal()
	{
		return this.rowsFinal;
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		this.paint = null;
		
		if (this.sides != null)
		{
			for (int i = 0; i < this.sides.size(); i++)
			{
				if (this.sides.get(i) != null)
				{
					this.sides.get(i).dispose();
					this.sides.set(i, null);
				}
			}
			
			this.sides.clear();
			this.sides = null;
		}
	}
	
	/**
	 * Flag the piece to stop moving
	 */
	public void stop()
	{
		//flag that we want to stop moving
		this.stop = true;
		
        if (MainThread.DEBUG)
        	System.out.println("Col = " + getCol() + ", Row = " + getRow());
	}
	
	/**
	 * Has the piece been flagged to stop moving
	 * @return true = yes, false otherwise
	 */
	public boolean hasStop()
	{
		return this.stop;
	}
	
	/**
	 * Does this piece have vertical velocity?
	 * @return true if it was/is moving vertical, false means it was/is moving horizontal
	 */
	public final boolean hasVerticalVelocity()
	{
		return this.vertical;
	}
	
	/**
	 * Have we done the piece comparison yet?
	 * @return true yes, false otherwise
	 */
	public boolean hasComparison()
	{
		return this.compare;
	}
	
	/**
	 * Update the piece.<br>
	 * Here we update the location and adjust velocity if needed etc...
	 * @param piece The piece on the board we want to place on top of
	 */
	public void update(final Piece piece)
	{
		//if the piece has not stopped update the location
		if (!hasStop())
		{
			//update the location of the piece
			setCol(getCol() + getDX());
			setRow(getRow() + getDY());
			
			//make sure we stay in bounds
			if (getCol() < DEFAULT_START_POSITION || getCol() > -DEFAULT_START_POSITION)
				setDX(-getDX());
			if (getRow() < DEFAULT_START_POSITION || getRow() > -DEFAULT_START_POSITION)
				setDY(-getDY());
		}
		else
		{
			if (!hasComparison())
			{
				//compare the pieces if we have not done the comparison
				//do the comparison
				PieceHelper.compare(this, piece);
				
				//flag that we have done the comparison
				this.compare = true;
			}
			else
			{
				//if we have stopped and have made a comparison, lets update the piece visibility
				for (int i = 0; i < getSides().size(); i++)
				{
					//update any necessary etc...
					getSides().get(i).update();
					
					if (getSides().get(i).hasDeadCompleted())
					{
						//remove the object
						getSides().remove(i);
						
						//move the index back
						i--;
					}
				}
			}
		}
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		//skip if it won't even be rendered on screen
		if (getY() > GamePanel.HEIGHT)
			return;
		
		//render all sides of the piece
		for (int i = 0; i < this.sides.size(); i++)
		{
			this.sides.get(i).render(canvas, paint, this);
		}
	}
}