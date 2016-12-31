package com.gamesbykevin.stack.board;

import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.stack.panel.GamePanel;
import com.gamesbykevin.stack.thread.MainThread;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

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
	
	//an array list of blocks that make up this piece
	private Block[][] blocks;
	
	//our paint object used to add transparency to blocks
	private Paint paint;
	
	//have we done the comparison yet
	private boolean compare = false;
	
	//was/is the piece moving vertical
	private final boolean vertical;
	
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
		this(piece.getCountCols(), piece.getCountRows(), !piece.hasVerticalVelocity());
	}
	
	/**
	 * Create a new piece
	 * @param cols The number of columns that make up the piece
	 * @param rows The number of rows that make up the piece
	 */
	public Piece(final int cols, final int rows, final boolean vertical) 
	{
		//create a new paint object
		this.paint = new Paint();
		
		//create the array of blocks
		this.blocks = new Block[rows][cols];
		
		//create a new instance of each block
		for (int col = 0; col < blocks[0].length; col++)
		{
			for (int row = 0; row < blocks.length; row++)
			{
				this.blocks[row][col] = new Block();
			}
		}
		
		//assign default location
		super.setCol(0);
		super.setRow(0);
		
		//assign default velocity
		super.setDX(DEFAULT_VELOCITY_NONE);
		super.setDY(DEFAULT_VELOCITY_NONE);
		
		//set the starting x,y coordinates
		super.setX(Board.START_X - (Block.COLUMN_WIDTH / 2));
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
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		if (blocks != null)
		{
			for (int col = 0; col < blocks[0].length; col++)
			{
				for (int row = 0; row < blocks.length; row++)
				{
					blocks[row][col].dispose();
				}
			}
		}
		
		this.blocks = null;
		this.paint = null;
	}
	
	/**
	 * Get the blocks
	 * @return The array of blocks that make up the current piece
	 */
	public final Block[][] getBlocks()
	{
		return this.blocks;
	}
	
	/**
	 * Count the number of columns
	 * @return The total number of columns that aren't dead
	 */
	private int getCountCols()
	{
		//keep track of our count
		int count = 0;
		
		//check each row one by one until we are able to retrieve a count
		for (int row = 0; row < getBlocks().length; row++)
		{
			for (int col = 0; col < getBlocks()[0].length; col++)
			{
				//if the block is not dead count it
				if (!getBlocks()[row][col].isDead())
					count++;
			}
			
			//if we have a count we have found a row that we can count
			if (count > 0)
				break;
		}
		
		//return our result
		return count;
	}
	
	/**
	 * Count the number of rows
	 * @return The total number of rows that aren't dead
	 */
	private int getCountRows()
	{
		//keep track of our count
		int count = 0;
		
		//check each column one by one until we are able to retrieve a count
		for (int col = 0; col < getBlocks()[0].length; col++)
		{
			for (int row = 0; row < getBlocks().length; row++)
			{
				//if the block is not dead count it
				if (!getBlocks()[row][col].isDead())
					count++;
			}
			
			//if we have a count we have found a column that we can count
			if (count > 0)
				break;
		}
		
		//return our result
		return count;
	}
	
	/**
	 * Flag the piece to stop moving
	 */
	public void stop()
	{
		this.stop = true;
		
        if (MainThread.DEBUG)
        	System.out.println("Col = " + getCol() + ", Row = " + getRow());
	}
	
	/**
	 * Has the piece been flagged to stop moving
	 * @return
	 */
	public boolean hasStopped()
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
	 * Are the dead blocks finished?<br>
	 * @return true if any blocks marked as dead have completed their animation or if there are no dead blocks, false otherwise
	 */
	public boolean deadCompleted()
	{
		for (int col = 0; col < getBlocks()[0].length; col++)
		{
			for (int row = 0; row < getBlocks().length; row++)
			{
				//if the block is dead, but not completed we return false
				if (getBlocks()[row][col].isDead() && !getBlocks()[row][col].deadCompleted())
					return false;
			}
		}
		
		//return true as no blocks were found to be dead and not completed
		return true;
	}
	
	/**
	 * Get the block count
	 * @return The total number of blocks that aren't flagged dead
	 */
	public int getBlockCount()
	{
		//count the blocks
		int count = 0;
		
		for (int col = 0; col < getBlocks()[0].length; col++)
		{
			for (int row = 0; row < getBlocks().length; row++)
			{
				//if the block is not dead, count it
				if (!getBlocks()[row][col].isDead())
					count++;
			}
		}
		
		//return our result
		return count;
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
			//calculate the impact
			int difference;
			int rowStart = 0;
			int rowEnd = getBlocks().length;
			int colStart = 0;
			int colEnd = getBlocks()[0].length;
			
			if (this.hasVerticalVelocity())
			{
				//if moving vertical check the rows
				if (getRow() < piece.getRow())
				{
					//calculate the row difference
					difference = (int) (piece.getRow() - getRow());
					
					//define where we start/end
					rowStart = 0;
					rowEnd = difference;
				}
				else
				{
					//calculate the row difference
					difference = (int) (getRow() - piece.getRow());
					
					//define where we start/end
					rowStart = getBlocks().length - difference;
					rowEnd = getBlocks().length;
				}
			}
			else 
			{
				//if moving horizontal check the columns
				if (getCol() < piece.getCol())
				{
					//calculate the column difference
					difference = (int) (piece.getCol() - getCol());
					
					//define where we start/end
					colStart = 0;
					colEnd = difference;
				}
				else
				{
					//calculate the column difference
					difference = (int) (getCol() - piece.getCol());
					
					//define where we start/end
					colStart = getBlocks()[0].length - difference;
					colEnd = getBlocks()[0].length;
				}
			}
			
			//now flag every block dead here
			for (int col = colStart; col < colEnd; col++)
			{
				for (int row = rowStart; row < rowEnd; row++)
				{
					//make sure we stay in bounds
					if (col < 0 || row < 0)
						continue;
					if (col >= getBlocks()[0].length || row >= getBlocks().length)
						continue;
					
					//flag the block dead
					getBlocks()[row][col].flagDead();
				}
			}
		}
		
		//flag that we have done the comparison
		this.compare = true;
	}
	
	/**
	 * Update the piece.<br>
	 * Here we update the location and adjust velocity if needed
	 */
	public void update()
	{
		//if the piece has not stopped update the location
		if (!hasStopped())
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
			//update each block
			for (int col = 0; col < getBlocks()[0].length; col++)
			{
				for (int row = 0; row < getBlocks().length; row++)
				{
					//update the blocks
					getBlocks()[row][col].update();
				}
			}
		}
	}
	
	/**
	 * Calculate the coordinates of all the blocks for rendering
	 */
	public void calculate()
	{
		//calculate the render coordinates
		for (int col = 0; col < getBlocks()[0].length; col++)
		{
			for (int row = 0; row < getBlocks().length; row++)
			{
				final float adjustCol = (int) (col + getCol());
				final float adjustRow = (int) (row + getRow());
				
				//assign coordinates
				getBlocks()[row][col].setX(((adjustCol - adjustRow) * (Block.COLUMN_WIDTH / 2)) + getX());
				getBlocks()[row][col].setY(((adjustCol + adjustRow) * (Block.ROW_HEIGHT   / 4)) + getY());
			}
		}
	}
	
	@Override
	public void render(final Canvas canvas, final Bitmap image) throws Exception
	{
		//skip if it won't even be rendered on screen
		if (getY() > GamePanel.HEIGHT)
			return;
		
		//if we haven't stopped the piece, figure out where we need to render the piece
		if (!hasStopped())
			calculate();
		
		//render each cell in this piece
		for (int col = 0; col < getBlocks()[0].length; col++)
		{
			for (int row = 0; row < getBlocks().length; row++)
			{
				getBlocks()[row][col].render(canvas, image, this.paint);
			}
		}
	}
}