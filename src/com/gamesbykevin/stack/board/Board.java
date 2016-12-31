package com.gamesbykevin.stack.board;

import java.util.ArrayList;
import java.util.List;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.stack.assets.Assets;
import com.gamesbykevin.stack.panel.GamePanel;

import android.graphics.Canvas;

/**
 * Here lies the board that we are placing the pieces on
 * @author GOD
 */
public class Board implements Disposable 
{
	//the list of pieces on the board
	private List<Piece> pieces;
	
	//the starting coordinate of the first piece
	public static final int START_X = (GamePanel.WIDTH / 2);
	
	//the starting coordinate of the first piece
	public static final int START_Y = (GamePanel.HEIGHT / 2);
	
	/**
	 * Create a new board
	 */
	public Board() 
	{
		//create a new list
		this.pieces = new ArrayList<Piece>();
		
		//create default piece for the bottom
		Piece piece = new Piece();
		
		//move the default piece below the current user piece
		piece.setCol(0);
		piece.setRow(0);
		piece.setY(Board.START_Y);
		
		//make sure we calculate where the coordinates are
		piece.calculate();
		
		//add default footer piece
		this.add(piece);
	}
	
	@Override
	public void dispose() 
	{
		if (this.pieces != null)
		{
			for (int i = 0; i < this.pieces.size(); i++)
			{
				this.pieces.get(i).dispose();
				this.pieces.set(i, null);
			}
			
			this.pieces.clear();
			this.pieces = null;
		}
	}

	/**
	 * Get the top piece
	 * @return The piece on the top that we are trying to place a piece on
	 */
	public Piece getTop()
	{
		return this.pieces.get(this.pieces.size() - 1);
	}
	
	/**
	 * Add piece to the board.<br>
	 * As we add this piece, we will move the other pieces down
	 * @param piece The piece we want to add
	 */
	public void add(final Piece piece)
	{
		//stop the piece from moving
		piece.setDX(Piece.DEFAULT_VELOCITY_NONE);
		piece.setDY(Piece.DEFAULT_VELOCITY_NONE);
		
		//add the piece to the array
		this.pieces.add(piece);
		
		//now move all pieces down to make room for the next piece
		for (int i = 0; i < this.pieces.size(); i++)
		{
			//move the piece down
			this.pieces.get(i).setY(this.pieces.get(i).getY() + (Block.ROW_HEIGHT / 2));
			
			//recalculate the block coordinates
			this.pieces.get(i).calculate();
		}
	}
	
	/**
	 * Render all the pieces on the board 
	 * @param canvas The canvas to which we render pixel data
	 * @throws Exception
	 */
	public void render(final Canvas canvas) throws Exception
	{
		//render each piece on the board
		for (int i = 0; i < pieces.size(); i++)
		{
			//render the current piece
			this.pieces.get(i).render(canvas, Images.getImage(Assets.ImageGameKey.Block));
		}
	}
}