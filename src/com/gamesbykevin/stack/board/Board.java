package com.gamesbykevin.stack.board;

import java.util.ArrayList;
import java.util.List;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.stack.common.ICommon;
import com.gamesbykevin.stack.panel.GamePanel;
import com.gamesbykevin.stack.piece.Piece;
import com.gamesbykevin.stack.piece.PieceHelper;
import android.graphics.Canvas;

/**
 * Here lies the board that we are placing the pieces on
 * @author GOD
 */
public class Board implements Disposable, ICommon
{
	//the list of pieces on the board
	private List<Piece> pieces;
	
	//the starting coordinate of the first piece
	public static final int START_X = (GamePanel.WIDTH / 2);
	
	//the starting coordinate of the first piece
	public static final int START_Y = (GamePanel.HEIGHT / 2) - (PieceHelper.ROW_HEIGHT_RENDER * 3);
	
	//the start and end coordinates when moving the pieces down
	private double startY = 0, endY = 0;
	
	/**
	 * How many pixels can we move the pieces down per update
	 */
	private static final double Y_VELOCITY = (PieceHelper.ROW_HEIGHT_RENDER / 5);
	
	/**
	 * Create a new board
	 */
	public Board() 
	{
		//create a new list
		this.pieces = new ArrayList<Piece>();
		
		//clear and add default piece to board
		reset();
	}
	
	@Override
	public void reset() 
	{
		//clear any existing pieces
		getPieces().clear();
		
		//create default piece for the bottom
		Piece piece = new Piece();
		
		//move the default piece below the current user piece
		piece.setCol(0);
		piece.setRow(0);
		piece.setY(Board.START_Y);
		
		//add default footer piece
		add(piece);
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
	 * Are the pieces placed on the board at their destination?
	 * @return true = yes, false = no
	 */
	public boolean hasDestination()
	{
		return (this.startY >= this.endY);
	}
	
	/**
	 * If the pieces are supposed to move down, lets do that
	 */
	public void update()
	{
		//move pieces if we aren't at our destination
		if (!hasDestination())
		{
			//if we aren't at our destination, we need to get there
			this.startY += Y_VELOCITY;
			
			//if we exceed the destination we made it
			if (this.startY > this.endY)
				this.startY = this.endY;
			
			//now move all pieces down to make room for the next piece
			for (int i = 0; i < getPieces().size(); i++)
			{
				//move the piece down
				getPieces().get(i).setY(getPieces().get(i).getY() + Y_VELOCITY);
			}
		}
	}
	
	/**
	 * Get the pieces
	 * @return The pieces placed on the board
	 */
	private List<Piece> getPieces()
	{
		return this.pieces;
	}
	
	/**
	 * Get the top piece
	 * @return The piece on the top that we are trying to place a piece on
	 */
	public Piece getTop()
	{
		return getPieces().get(getPieces().size() - 1);
	}
	
	/**
	 * Add piece to the board.<br>
	 * As we add this piece, we will move the other pieces down
	 * @param piece The piece we want to add
	 */
	public final void add(final Piece piece)
	{
		//stop the piece from moving
		piece.setDX(Piece.DEFAULT_VELOCITY_NONE);
		piece.setDY(Piece.DEFAULT_VELOCITY_NONE);
		
		//if adding the first piece, no need to move down
		if (getPieces().isEmpty())
		{
			//move down instantly
			piece.setY(piece.getY() + PieceHelper.ROW_HEIGHT_RENDER);
		}
		else
		{
			//set the start and finish coordinates so we know to move the pieces down
			startY = piece.getY();
			endY = startY + PieceHelper.ROW_HEIGHT_RENDER;
		}
		
		//add the piece to the array
		getPieces().add(piece);
	}
	
	/**
	 * Render all the pieces on the board 
	 * @param canvas The canvas to which we render pixel data
	 * @throws Exception
	 */
	public void render(final Canvas canvas) throws Exception
	{
		//render each piece on the board
		for (int i = 0; i < getPieces().size(); i++)
		{
			//render the current piece
			getPieces().get(i).render(canvas);
		}
	}
}