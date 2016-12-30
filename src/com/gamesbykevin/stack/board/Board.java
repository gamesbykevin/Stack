package com.gamesbykevin.stack.board;

import java.util.ArrayList;
import java.util.List;

import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.stack.assets.Assets;
import com.gamesbykevin.stack.panel.GamePanel;

import android.graphics.Canvas;

/**
 * Here lies the board that we are placing the pieces on
 * @author GOD
 *
 */
public class Board 
{
	//the list of pieces on the board
	private List<Piece> pieces;
	
	//the starting coordinate of the first piece
	public static final int START_X = (GamePanel.WIDTH / 2) - (Piece.COLUMN_WIDTH / 2);
	
	//the starting coordinate of the first piece
	public static final int START_Y = (GamePanel.HEIGHT / 2);
	
	/**
	 * Create a new board
	 */
	public Board() 
	{
		//create a new list
		this.pieces = new ArrayList<Piece>();
	}

	public void render(final Canvas canvas) throws Exception
	{
		//render each piece on the board
		for (int i = 0; i < pieces.size(); i++)
		{
			//render the current piece
			pieces.get(i).render(canvas, Images.getImage(Assets.ImageGameKey.Block));
		}
	}
}