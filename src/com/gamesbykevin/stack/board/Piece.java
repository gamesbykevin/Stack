package com.gamesbykevin.stack.board;

import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.stack.panel.GamePanel;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * A single piece that we want to place on the board
 * @author GOD
 *
 */
public class Piece extends Entity
{
	//the size of the piece
	private int cols, rows;

	/**
	 * The dimensions of a single cell of this piece
	 */
	public static final int COLUMN_WIDTH = 50;
	
	/**
	 * The dimensions of a single cell of this piece
	 */
	public static final int ROW_HEIGHT = COLUMN_WIDTH;
	
	/**
	 * Create a new piece
	 * @param cols The number of columns that make up the piece
	 * @param rows The number of rows that make up the piece
	 */
	public Piece(final int cols, final int rows) 
	{
		//store the size of the piece
		this.cols = cols;
		this.rows = rows;
		
		//assign default location
		super.setCol(0);
		super.setRow(0);
	}

	@Override
	public void render(final Canvas canvas, final Bitmap image) throws Exception
	{
		//set the dimensions
		super.setHeight(ROW_HEIGHT);
		super.setWidth(COLUMN_WIDTH);
		
		//render each cell in this piece
		for (int col = 0; col < cols; col++)
		{
			for (int row = 0; row < rows; row++)
			{
				final int adjustCol = (int) (col + getCol());
				final int adjustRow = (int) (row + getRow());
				
				//assign coordinates
				setX(((adjustCol - adjustRow) * (COLUMN_WIDTH / 2)) + Board.START_X);
				setY(((adjustCol + adjustRow) * (ROW_HEIGHT / 4)) + Board.START_Y);

				//skip if it won't even be rendered on screen
				if (getY() > GamePanel.HEIGHT)
					continue;
				if (getX() < 0 && getX() + getWidth() < 0)
					continue;
				if (getX() > GamePanel.WIDTH)
					continue;
				
				//render the image at the current location
				super.render(canvas, image);
			}
		}
	}
}