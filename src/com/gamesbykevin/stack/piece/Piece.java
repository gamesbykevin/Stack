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
	public static final float DEFAULT_VELOCITY = 1.0f; 
	
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
	
	//the sides that make up the piece
	private List<Side> sides;
	
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
		this(piece.getCols(), piece.getRows(), !piece.hasVerticalVelocity());
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
		createSides(0, getCols(), 0, getRows(), false);
	}
	
	/**
	 * Create the sides with the specified boundary
	 * @param colW West most column
	 * @param colE East most column
	 * @param rowN North most column
	 * @param rowS South most column
	 * @param dead Are these sides dead?
	 */
	private void createSides(final float colW, final float colE, final float rowN, final float rowS, final boolean dead)
	{
		Side top = new Side(Type.Top);
		Side east = new Side(Type.East);
		Side south = new Side(Type.South);
		
		//set the boundaries of the current side
		top.setBoundary(colW, colE, rowN, rowS);
		east.setBoundary(colW, colE, rowN, rowS);
		south.setBoundary(colW, colE, rowN, rowS);
		
		//if dead, flag it
		if (dead)
		{
			top.flagDead();
			east.flagDead();
			south.flagDead();
		}
		
		//add the sides to our list
		this.sides.add(top);
		this.sides.add(east);
		this.sides.add(south);
	}
	
	public float getCols()
	{
		return this.cols;
	}
	
	public float getRows()
	{
		return this.rows;
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
	 * Compare the current piece with the piece specified<br>
	 * The reason is to identify how to cut the current piece
	 * @param piece The piece previously placed on the board
	 */
	public void compare(final Piece piece)
	{
		
		//if the piece is not directly on top of the other piece we need to separate
		if (piece.getCol() != getCol() || piece.getRow() != getRow())
		{
			float difference = 0;
			float surviveColW = 0;
			float surviveColE = 0;
			float surviveRowN = 0;
			float surviveRowS = 0;
			float deadColW = 0;
			float deadColE = 0;
			float deadRowN = 0;
			float deadRowS = 0;
			
			//moving north and south
			if (hasVerticalVelocity())
			{
				if (piece.getRow() > getRow())
				{
					//calculate the difference
					difference = (float) (piece.getRow() - getRow());
					surviveColW = 0;
					surviveColE = getCols();
					surviveRowN = (float)piece.getRow();
					surviveRowS = getRows() - difference;
					deadColW = 0;
					deadColE = getCols();
					deadRowN = 0;
					deadRowS = (float)piece.getRow();
				}
				else
				{
					//calculate the difference
					difference = (float) (getRow() - piece.getRow());
					surviveColW = 0;
					surviveColE = getCols();
					surviveRowN = 0;
					surviveRowS = piece.getRows();
					deadColW = 0;
					deadColE = getCols();
					deadRowN = piece.getRows();
					deadRowS = getRows();
				}
			}
			else
			{
				if (piece.getCol() > getCol())
				{
					//calculate the difference
					difference = (float) (piece.getCol() - getCol());
					surviveColW = getCols() - difference;
					surviveColE = getCols();
					surviveRowN = 0;
					surviveRowS = getRows();
					deadColW = 0;
					deadColE = getCols() - difference;
					deadRowN = 0;
					deadRowS = getRows();
				}
				else
				{
					//calculate the difference
					difference = (float) (getCol() - piece.getCol());
					surviveColW = difference;
					surviveColE = piece.getCols();
					surviveRowN = 0;
					surviveRowS = getRows();
					deadColW = piece.getCols();
					deadColE = getCols();
					deadRowN = 0;
					deadRowS = getRows();
				}
			}
			
			//update the boundary for the existing surviving side
			for (int i = 0; i < this.sides.size(); i++)
			{
				//update the boundary
				this.sides.get(i).setBoundary(surviveColW, surviveColE, surviveRowN, surviveRowS);
			}
			
			//create the sides that are dead
			createSides(deadColW, deadColE, deadRowN, deadRowS, true);
		}
		
		//flag that we have done the comparison
		this.compare = true;
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
				compare(piece);
			}
			else
			{
				//if we have stopped and have made a comparison, lets update the piece visibility
				for (int i = 0; i < this.sides.size(); i++)
				{
					this.sides.get(i).update();
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