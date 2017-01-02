package com.gamesbykevin.stack.game;

import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.stack.assets.Assets;
import com.gamesbykevin.stack.number.Number;
import com.gamesbykevin.stack.piece.PieceHelper;
import com.gamesbykevin.stack.screen.ScreenManager;

import android.graphics.Canvas;

/**
 * Game helper methods
 * @author GOD
 */
public final class GameHelper 
{
	/**
	 * Do we notify that we are reseting the game?
	 */
	public static boolean NOTIFY_RESET = true;
	
	/**
	 * Do we reset the game?
	 */
	public static boolean RESET = true;
	
	/**
	 * Is the game over?
	 */
	public static boolean GAMEOVER = false;
	
    /**
     * Size of in-game buttons
     */
	public static final int BUTTON_DIMENSION = 60;
	
	/**
	 * The amount of pixels between each button
	 */
	public static final int Y_INCREMENT = (int)(BUTTON_DIMENSION * 1.175);
	
	/**
	 * Location of the in game logo
	 */
	public static final int LOGO_X = 0;
	
	/**
	 * Location of the in game logo
	 */
	public static final int LOGO_Y = 10;
    
    /**
     * Update game
     * @throws Exception 
     */
    public static final void update(final Game game) throws Exception
    {
    	if (RESET)
    	{
    		//reset turns count
    		game.getCurrent().setNumber(0);
    		
    		//make sure record is up to date
    		game.getRecord().setX(Number.BEST_RECORD_X);
    		game.getRecord().setNumber(game.getScore().getScoreResult());
    		
    		//reset board
    		game.getBoard().reset();
    		
    		//create a new piece
    		game.createPiece();
    		
    		//flag reset false now that we have finished
    		RESET = false;
    		
			//flag false now that we have finished
			NOTIFY_RESET = false;
    	}
    	else
    	{
    		//if we want to exit the current game
    		if (GAMEOVER)
    		{
    			if (PieceHelper.hasDeadCompleted(game.getPiece()))
    			{
					//vibrate phone
					game.vibrate();

	    			//flag false
	    			GAMEOVER = false;
	    			
	    			//set game over screen state
	    			game.getScreen().setState(ScreenManager.State.GameOver);
	    			
	    			//no need to continue
	    			return;
    			}
    			else
    			{
    				//update the piece
    				game.getPiece().update(game.getBoard());
    			}
    		}
    		else
    		{
    			if (game.getBoard().hasDestination())
    			{
					//update the game piece location/velocity etc...
					game.getPiece().update(game.getBoard());
    			}
    			else
    			{
					//update the board
					game.getBoard().update();
					
					//if the pieces are correctly in place, we can spawn a new piece
					if (game.getBoard().hasDestination())
					{
    					//create a new piece
    					game.createPiece();
    					
    					//add 1 to our total number of attempts
    					game.getCurrent().setNumber(game.getCurrent().getNumber() + 1);
					}
    			}
    		}
    	}
    }
    
    /**
     * Render the game accordingly
     * @param canvas Place to write pixel data
     * @param game Our game reference object
     * @throws Exception
     */
    public static final void render(final Canvas canvas, final Game game) throws Exception
    {
    	if (!canPlay())
    	{
			//render splash loading screen
			canvas.drawBitmap(Images.getImage(Assets.ImageMenuKey.Splash), 0, 0, null);
    	}
    	else
    	{
    		//draw all existing pieces on the board
    		game.getBoard().render(canvas);
    		
    		//render our number of successful attempts
    		game.getCurrent().render(canvas);

    		//render the current record
    		game.getRecord().render(canvas);
    		
    		//render the best record text
    		canvas.drawBitmap(Images.getImage(Assets.ImageGameKey.BestText), Number.BEST_RECORD_X_TEXT, Number.BEST_RECORD_Y, null);
    		
    		//render the current piece
    		game.getPiece().render(canvas);
    	}
    }
    
    /**
     * Can the player play the game?
     * @return true if we are not resetting the game, false otherwise
     */
    public static final boolean canPlay()
    {
    	return (!NOTIFY_RESET && !RESET);
    }
}