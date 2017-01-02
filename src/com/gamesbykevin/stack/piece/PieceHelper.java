package com.gamesbykevin.stack.piece;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.stack.assets.Assets;
import com.gamesbykevin.stack.game.GameHelper;
import com.gamesbykevin.stack.piece.Side.Type;

/**
 * Helper methods for our piece class
 * @author GOD
 */
public class PieceHelper 
{
	/**
	 * The pixel dimension of a single block that makes up this piece
	 */
	public static final int COLUMN_WIDTH = 20;

	/**
	 * The dimensions of a single block that makes up this piece
	 */
	public static final int ROW_HEIGHT = (int) (COLUMN_WIDTH * 1.0);
	
	/**
	 * The pixel height we want to render on screen
	 */
	public static final int ROW_HEIGHT_RENDER = (int) (ROW_HEIGHT * 2.0);
	
	/**
	 * Compare the two pieces to determine how we are to separate
	 * @param piece1 Our piece currently in play
	 * @param piece2 The piece placed on the board
	 */
	protected static void compare(final Piece piece1, final Piece piece2)
	{
		//calculations to assign with comparing the piece
		float rowsFinal;
		float colsFinal;
		float difference = 0;
		float surviveColW = 0;
		float surviveColE = 0;
		float surviveRowN = 0;
		float surviveRowS = 0;
		float deadColW = 0;
		float deadColE = 0;
		float deadRowN = 0;
		float deadRowS = 0;
		boolean first = false;
		
		//if the piece is not directly on top of the other piece we need to separate
		if (piece2.getCol() != piece1.getCol() || piece2.getRow() != piece1.getRow())
		{
			//moving north and south
			if (piece1.hasVerticalVelocity())
			{
				if (piece2.getRow() > piece1.getRow())
				{
					//calculate the difference
					difference = (float) (piece2.getRow() - piece1.getRow());
					surviveColW = 0;
					surviveColE = piece1.getCols();
					surviveRowN = difference;
					surviveRowS = piece1.getRows();
					deadColW = 0;
					deadColE = piece1.getCols();
					deadRowN = 0;
					deadRowS = difference;
					first = true;
				}
				else
				{
					//calculate the difference
					difference = (float) (piece1.getRow() - piece2.getRow());
					surviveColW = 0;
					surviveColE = piece1.getCols();
					surviveRowN = 0;
					surviveRowS = piece1.getRows() - difference;
					deadColW = 0;
					deadColE = piece1.getCols();
					deadRowN = piece1.getRows() - difference;
					deadRowS = piece1.getRows();
					first = false;
				}
				
				//store the final piece size after comparison
				colsFinal = piece1.getCols();
				rowsFinal = piece1.getRows() - difference;
				
				//if the difference is greater or equal to the number of rows, we lost
				if (difference >= piece1.getRows())
					GameHelper.GAMEOVER = true;
			}
			else
			{
				if (piece2.getCol() > piece1.getCol())
				{
					//calculate the difference
					difference = (float) (piece2.getCol() - piece1.getCol());
					surviveColW = difference;
					surviveColE = piece1.getCols();
					surviveRowN = 0;
					surviveRowS = piece1.getRows();
					deadColW = 0;
					deadColE = difference;
					deadRowN = 0;
					deadRowS = piece1.getRows();
					first = true;
				}
				else
				{
					//calculate the difference
					difference = (float) (piece1.getCol() - piece2.getCol());
					surviveColW = 0;
					surviveColE = piece1.getCols() - difference;
					surviveRowN = 0;
					surviveRowS = piece1.getRows();
					deadColW = piece1.getCols() - difference;
					deadColE = piece1.getCols();
					deadRowN = 0;
					deadRowS = piece1.getRows();
				}
				
				//store the final piece size after comparison
				colsFinal = piece1.getCols() - difference;
				rowsFinal = piece1.getRows();
				
				//if the difference is greater or equal to the number of rows, we lost
				if (difference >= piece1.getCols())
					GameHelper.GAMEOVER = true;
			}
			
			if (!GameHelper.GAMEOVER)
			{
				//update the boundary for the existing surviving side
				for (int i = 0; i < piece1.getSides().size(); i++)
				{
					//update the boundary
					piece1.getSides().get(i).setBoundary(surviveColW, surviveColE, surviveRowN, surviveRowS);
				}
				
				//create the sides that are dead
				createSides(piece1, deadColW, deadColE, deadRowN, deadRowS, true, first);
				
				//set the cut location so we know where to spawn the next piece
			}
			else
			{
				//make all sides non-existent
				for (int i = 0; i < piece1.getSides().size(); i++)
				{
					piece1.getSides().get(i).setBoundary(0, 0, 0, 0);
				}
				
				//mark the whole piece as dead
				createSides(piece1, 0, piece1.getCols(), 0, piece1.getRows(), true, first);
			}
			
			//play appropriate sound effect
			Audio.play((int)difference != 0 ? Assets.AudioGameKey.PlaceWrong : Assets.AudioGameKey.PlaceCorrect);
		}
		else
		{
			//play sound effect that we got it correct
			Audio.play(Assets.AudioGameKey.PlaceCorrect);
			
			//rows and columns will stay the same
			colsFinal = piece1.getCols();
			rowsFinal = piece1.getRows();
			
			//we will also not need to cut the piece
			surviveColW = 0;
			surviveRowN = 0;
		}
		
		//assign the total surviving rows/columns
		piece1.setColsFinal(colsFinal);
		piece1.setRowsFinal(rowsFinal);
		
		//set the spawn location of the piece, so we know where to spawn the next piece
		piece1.setSpawnX(Side.getLocationX(surviveColW, surviveRowN, piece1));
		piece1.setSpawnY(Side.getLocationY(surviveColW, surviveRowN, piece1));
	}
	
	/**
	 * Create the sides with the specified boundary
	 * @param piece The piece we want to create sides for
	 * @param colW West most column
	 * @param colE East most column
	 * @param rowN North most column
	 * @param rowS South most column
	 * @param dead Are these sides dead?
	 * @param first Do we put these sides first in line in the array?
	 */
	protected static void createSides(final Piece piece, final float colW, final float colE, final float rowN, final float rowS, final boolean dead, final boolean first)
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
		if (first)
		{
			piece.getSides().add(0, top);
			piece.getSides().add(0, east);
			piece.getSides().add(0, south);
		}
		else
		{
			piece.getSides().add(top);
			piece.getSides().add(east);
			piece.getSides().add(south);
		}
	}
	
	/**
	 * Have any dead sides completed the animation sequence?<br>
	 * @param piece
	 * @return true if yes or if there are no dead sides, false otherwise
	 */
	public static boolean hasDeadCompleted(final Piece piece)
	{
		//if we have stopped and have made a comparison, lets update the piece visibility
		for (int i = 0; i < piece.getSides().size(); i++)
		{
			if (piece.getSides().get(i).isDead() && !piece.getSides().get(i).hasDeadCompleted())
				return false;
		}
		
		//we have success
		return true;
	}
	
	/**
	 * Align the piece so the piece in play is directly above the placed piece
	 * @param piece1 The piece currently in play
	 * @param piece2 The piece that is already placed on the board
	 */
	public static void alignPiece(final Piece piece1, final Piece piece2)
	{
		piece1.setX(piece2.getSpawnX());
		piece1.setY(piece2.getSpawnY());
	}
}