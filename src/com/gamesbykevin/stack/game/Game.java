package com.gamesbykevin.stack.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.view.MotionEvent;

import com.gamesbykevin.stack.board.Block;
import com.gamesbykevin.stack.board.Board;
import com.gamesbykevin.stack.board.Piece;
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
    private static final long VIBRATION_DURATION = 750L;
    
    //the board we are placing pieces on
    private Board board;
    
    //our current piece
    private Piece piece;
    
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
        
        //create our score card
        this.score = new Score(screen.getPanel().getActivity());
        
        //create a new board
        this.board = new Board();
        
        //create a new piece
        createPiece();
    }
    
    /**
     * Create a new piece for us to interact with
     */
    public final void createPiece()
    {
    	if (this.piece == null)
    	{
    		this.piece = new Piece();
    	}
    	else
    	{
    		double x = 0;
    		double y = 0;
    		
    		//calculate the north-west corner coordinates
    		for (int col = 0; col < getBoard().getTop().getBlocks()[0].length; col++)
    		{
    			for (int row = 0; row < getBoard().getTop().getBlocks().length; row++)
    			{
    				//if this is not dead we found our north-west location
    				if (!getBoard().getTop().getBlocks()[row][col].isDead())
    				{
    					//set the coordinates for the next piece
    					x = getBoard().getTop().getBlocks()[row][col].getX();
    					y = getBoard().getTop().getBlocks()[row][col].getY();
    					
    					y = y - (Block.ROW_HEIGHT / 2);
    					
    					//assign at end
    					col = getBoard().getTop().getBlocks()[0].length;
    					row = getBoard().getTop().getBlocks().length;
    					
    					//no need to continue loop
    					break;
    				}
    			}
    		}
    		
    		//create a new piece
    		this.piece = new Piece(getBoard().getTop());
    		
    		//now place the piece over the previous
    		this.piece.setX(x);
    		this.piece.setY(y);
    	}
    }
    
    /**
     * Get the score object
     * @return Our object used for tracking the high score
     */
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
     * Get the board
     * @return The board to which we are placing pieces on
     */
    public Board getBoard()
    {
    	return this.board;
    }
    
    /**
     * Get the piece
     * @return The piece currently in play
     */
    public Piece getPiece()
    {
    	return this.piece;
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
			//flag the piece to stop moving
			getPiece().stop();
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