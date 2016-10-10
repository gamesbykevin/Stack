package com.gamesbykevin.stack.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.view.MotionEvent;

import com.gamesbykevin.stack.number.Number;
import com.gamesbykevin.stack.score.Score;
import com.gamesbykevin.stack.screen.OptionsScreen;
import com.gamesbykevin.stack.screen.ScreenManager;

/**
 * The main game logic will happen here
 * @author ABRAHAM
 */
public final class Game implements IGame
{
    //our main screen object reference
    private final ScreenManager screen;
    
    //paint object to draw text
    private Paint paint;
    
    //the duration we want to vibrate the phone for
    private static final long VIBRATION_DURATION = 500L;
    
    /**
     * Our value to identify if vibrate is enabled
     */
	public static final int VIBRATE_ENABLED = 0;
	
    //object for rendering a number
    private Number number;
    
    //the game score card
    private Score score;
    
    /**
     * Create our game object
     * @param screen The main screen
     * @throws Exception
     */
    public Game(final ScreenManager screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create new number object and position it
        this.number = new Number();
        //this.number.setNumber(0, GameHelper.ATTEMPTS_X + Images.getImage(Assets.ImageGameKey.Attempts).getWidth() + 5, GameHelper.ATTEMPTS_Y);
        
        //check the options to set the size of our board
        final int size;
        
        //set the number of colors index
        final int numberColorsIndex = getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Difficulty); 
        
        //create our score card
        this.score = new Score(screen.getPanel().getActivity());
    }
    
    public Score getScore()
    {
    	return this.score;
    }
    
    /**
     * Get the number object
     * @return Our number object reference for rendering lives
     */
    public Number getNumber()
    {
    	return this.number;
    }
    
    /**
     * Get the main screen object reference
     * @return The main screen object reference
     */
    public ScreenManager getScreen()
    {
        return this.screen;
    }
    
    /**
     * Get the paint object
     * @return The paint object used to draw text in the game
     */
    public Paint getPaint()
    {
    	//if the object has not been created yet
    	if (this.paint == null)
    	{
            //create new paint object
            this.paint = new Paint();
            //this.paint.setTypeface(Font.getFont(Assets.FontGameKey.Default));
            this.paint.setTextSize(36f);
            this.paint.setColor(Color.WHITE);
            this.paint.setLinearText(false);
    	}
    	
        return this.paint;
    }
    
    @Override
    public void update(final int action, final float x, final float y) throws Exception
    {
    	//if we can't play, don't continue
    	if (!GameHelper.canPlay())
    		return;
    	
		if (action == MotionEvent.ACTION_UP)
    	{
    	}
    	else if (action == MotionEvent.ACTION_DOWN)
		{
		}
		else if (action == MotionEvent.ACTION_MOVE)
    	{
    	}
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
    	//update our game objects
		GameHelper.update(this);
    }
    
    /**
     * Vibrate the phone for the default duration
     */
    public void vibrate()
    {
    	this.vibrate(VIBRATION_DURATION);
    }
    
    /**
     * Vibrate the phone if the vibrate feature is enabled
     * @param duration The duration to vibrate for milliseconds
     */
    public void vibrate(final long duration)
    {
		//make sure vibrate option is enabled
		if (getScreen().getScreenOptions().getIndex(OptionsScreen.Key.Vibrate) == VIBRATE_ENABLED)
		{
    		//get our vibrate object
    		Vibrator v = (Vibrator) getScreen().getPanel().getActivity().getSystemService(Context.VIBRATOR_SERVICE);
    		 
			//vibrate for a specified amount of milliseconds
			v.vibrate(duration);
		}
    }
    
    /**
     * Render game elements
     * @param canvas Where to write the pixel data
     * @throws Exception 
     */
    @Override
    public void render(final Canvas canvas) throws Exception
    {
    	GameHelper.render(canvas, this);
    }
    
    @Override
    public void dispose()
    {
        this.paint = null;
        
    	if (this.number != null)
    	{
    		this.number.dispose();
    		this.number = null;
    	}
    	
    	if (this.score != null)
    	{
    		this.score.dispose();
    		this.score = null;
    	}
    }
}