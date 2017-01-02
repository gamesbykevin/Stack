package com.gamesbykevin.stack.score;

import com.gamesbykevin.androidframework.io.storage.Internal;
import com.gamesbykevin.stack.thread.MainThread;

import android.app.Activity;

public class Score extends Internal 
{
	/**
	 * The name of our internal storage file
	 */
	public static final String FILE_NAME = "scorecard";

    //the best score result
    private int scoreResult;
    
	/**
	 * Default Constructor
	 */
	public Score(final Activity activity) 
	{
		//call parent constructor
		super(FILE_NAME, activity, MainThread.DEBUG);
		
    	try 
    	{
    		//try to parse the result 
    		this.scoreResult = Integer.parseInt(super.getContent().toString());
		} 
    	catch (Exception e)
    	{
    		//any issue we will assign a default value
    		this.scoreResult = 0;
    	}
	}
	
	/**
	 * Update the score record if it is greater
	 * @param scoreResult The score result we want to check
	 * @return true if the scoreResult is a new record, false otherwise
	 */
	public boolean update(final int scoreResult)
	{
		if (this.scoreResult < scoreResult)
		{
			//update result
			this.scoreResult = scoreResult;
			
			//save the new record
			save();
			
			//return true because we set a record
			return true;
		}
		else
		{
			//no record has been set
			return false;
		}
	}
	
	/**
	 * Get the score result of the best record
	 * @return The current record set
	 */
	public int getScoreResult()
	{
		return this.scoreResult;
	}
	
    /**
     * Save the record to the android internal storage
     */
    @Override
    public void save()
    {
        //remove all existing content
        super.getContent().delete(0, super.getContent().length());
        
        //write the new score
        super.getContent().append(this.scoreResult);
        
        //save the content to physical internal storage location
        super.save();
    }
    
    @Override
    public void dispose()
    {
    	super.dispose();
    }
}