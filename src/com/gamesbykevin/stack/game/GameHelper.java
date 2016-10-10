package com.gamesbykevin.stack.game;

import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.stack.assets.Assets;
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
	 * Do we display the screenshot instructions
	 */
	public static boolean IN_GAME_INSTRUCTIONS = false;
	
	/**
	 * Do we go to the exit game screen
	 */
	public static boolean EXIT_GAME = false;
	
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
    		//reset attempts count
    		game.getNumber().setNumber(1);
    		
    		//flag reset false now that we have finished
    		RESET = false;
    		
			//flag false now that we have finished
			NOTIFY_RESET = false;
    	}
    	else
    	{
    		//don't continue if showing in game instructions
    		if (IN_GAME_INSTRUCTIONS)
    			return;
    		
    		//if we want to exit the current game
    		if (EXIT_GAME)
    		{
    			//flag false
    			EXIT_GAME = false;
    			
    			//set exit screen state
    			game.getScreen().setState(ScreenManager.State.Exit);
    			
    			//no need to continue
    			return;
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
			//render loading screen
			canvas.drawBitmap(Images.getImage(Assets.ImageMenuKey.Splash), 0, 0, null);
    	}
    	else
    	{
    		
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