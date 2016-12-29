package com.gamesbykevin.stack;

import com.gamesbykevin.stack.panel.GamePanel;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public final class MainActivity extends Activity 
{
    //our game panel
    private GamePanel panel;
    
    /**
     * Our web site address where more games can be found
     */
    public static final String WEBPAGE_MORE_GAMES_URL = "http://gamesbykevin.com";

    /**
     * The web address where this game can be rated
     */
    public static final String WEBPAGE_RATE_URL = "https://play.google.com/store/apps/details?id=com.gamesbykevin.mastermind";

    /**
     * The url that contains the instructions for the game
     */
    public static final String WEBPAGE_GAME_INSTRUCTIONS_URL = "http://gamesbykevin.com/2016/07/04/mastermind";
    
    /**
     * The face book url
     */
    public static final String WEBPAGE_FACEBOOK_URL = "https://facebook.com/gamesbykevin";
    
    /**
     * The twitter url
     */
    public static final String WEBPAGE_TWITTER_URL = "https://twitter.com/gamesbykevin";
    
    /**
     * The youtube url
     */
    public static final String WEBPAGE_YOUTUBE_URL = "https://youtube.com/gamesbykevin";
    
    /**
     * Keep track of when the activity is finished so we don't call multiple times
     */
    private static boolean FINISH_ACTIVITY = false;
    
    /**
     * Called when the activity is first created
     * @param savedInstanceState 
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	//we have not yet finished the activity
    	FINISH_ACTIVITY = false;
    	
        //turn the title off
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set the screen to full screen
        super.getWindow().setFlags(
        	WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        	WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        
        //call parent create
        super.onCreate(savedInstanceState);
        
        //if the panel has not been created
        if (getGamePanel() == null)
        {
            //create the game panel
            setGamePanel(new GamePanel(this));

            //add callback to the game panel to intercept events
            getGamePanel().getHolder().addCallback(getGamePanel());
        }

        //set the content view to our game
        setContentView(getGamePanel());
    }
    
    /**
     * Override the finish call
     */
    @Override
    public void finish()
    {
    	//if we already finished the activity don't do it again
    	if (FINISH_ACTIVITY)
    		return;
    	
    	//flag true
    	FINISH_ACTIVITY = true;
    	
        //cleanup game panel if it exists
        if (getGamePanel() != null)
        {
            getGamePanel().dispose();
            setGamePanel(null);
        }
        
        //call parent
        super.finish();
        
        //return control to android
        return;
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onStart()
    {
        //call parent
        super.onStart();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onStop()
    {
        //call parent
        super.onStop();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onDestroy()
    {
        //finish the activity
        this.finish();
        
        //perform final cleanup
        super.onDestroy();
        
        //return control to android
        return;
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onPause()
    {
    	//call parent
        super.onPause();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onWindowFocusChanged(final boolean hasFocus)
    {
    	//call parent
    	super.onWindowFocusChanged(true);
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onResume()
    {
    	//call parent
    	super.onResume();
    }
    
    /**
     * Navigate to the desired web page
     * @param url The desired url
     */
    public void openWebpage(final String url)
    {
        //create action view intent
        Intent intent = new Intent(Intent.ACTION_VIEW);
        
        //the content will be the web page
        intent.setData(Uri.parse(url.trim()));
        
        //start this new activity
        startActivity(intent);        
    }
    
    /**
     * Get the game panel.
     * @return The object containing our game logic, assets, threads, etc...
     */
    private GamePanel getGamePanel()
    {
        return this.panel;
    }
    
    /**
     * Assign the game panel
     * @param panel The game panel
     */
    private void setGamePanel(final GamePanel panel)
    {
        this.panel = panel;
    }
}