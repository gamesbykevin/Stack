package com.gamesbykevin.stack.score;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.io.storage.Internal;
import com.gamesbykevin.stack.thread.MainThread;

import android.app.Activity;

public class Score extends Internal 
{
	/**
	 * The name of our internal storage file
	 */
	public static final String FILE_NAME = "scorecard";

    /**
     * New score separator string
     */
    private static final String NEW_SCORE = ";";
	
    /**
     * Split the number of colors and the level number
     */
    private static final String SCORE_DATA = "-";
    
    /**
     * The location for the difficulty
     */
    private static final int NUMBER_OF_COLORS_INDEX = 0;
    
    /**
     * The location for the level number
     */
    private static final int LEVEL_NUMBER_INDEX = 1;
    
	//list of all levels completed
	private ArrayList<LevelData> levels;
	
	/**
	 * Default Constructor
	 */
	public Score(final Activity activity) 
	{
		super(FILE_NAME, activity, MainThread.DEBUG);
		
		//create new list of levels
		this.levels = new ArrayList<LevelData>();
		
        //if content exists we will load it
        if (super.getContent().toString().trim().length() > 0)
        {
            //each index indicates a level completed
            final String[] scores = super.getContent().toString().split(NEW_SCORE);
            
            for (String score : scores)
            {
            	//split the score up
            	final String[] data = score.split(SCORE_DATA);
            	
            	//add this to our list of levels
            	this.levels.add(
            		new LevelData(
            			Integer.parseInt(data[NUMBER_OF_COLORS_INDEX]), 
            			Integer.parseInt(data[LEVEL_NUMBER_INDEX])
            		)
            	);
            }
        }
	}
	
	/**
	 * Have we completed this level
	 * @param numberColors The difficulty setting for the number of colors
	 * @param numberLevel The level index we want to check
	 * @return true if the specified parameters match our list, false otherwise
	 */
	public boolean hasCompleted(final int numberColors, final int numberLevel)
	{
		for (int i = 0; i < this.levels.size(); i++)
		{
			//if no match, skip to the next
			if (this.levels.get(i).numberColors != numberColors)
				continue;
			if (this.levels.get(i).numberLevel != numberLevel)
				continue;
			
			//we have a match
			return true;
		}
		
		//we do not have a match
		return false;
	}
	
	/**
	 * Add the level data to our saved list
	 * @param numberColors The difficulty setting for the number of colors
	 * @param numberLevel The level index we want to check
	 * @return true if the level data was added, false otherwise
	 */
	public boolean update(final int numberColors, final int numberLevel)
	{
		//check the entire list
		for (int i = 0; i < this.levels.size(); i++)
		{
			//if we already have, no need to add
			if (this.levels.get(i).numberColors == numberColors && 
				this.levels.get(i).numberLevel == numberLevel)
				return false;
		}
		
		//add the new level data to the list
		this.levels.add(new LevelData(numberColors, numberLevel));
		
		//save the data
		save();
		
		//we were successful updating
		return true;
	}
	
    /**
     * Save the levels to the internal storage
     */
    @Override
    public void save()
    {
        //remove all existing content
        super.getContent().delete(0, super.getContent().length());
        
        for (int i = 0; i < this.levels.size(); i++)
        {
            //if content exists, add delimiter to separate each level
            if (super.getContent().length() > 0)
                super.getContent().append(NEW_SCORE);
            
            //write colors, and level number
            super.getContent().append(this.levels.get(i).getData());
        }
        
        //save the content to physical internal storage location
        super.save();
    }
    
    @Override
    public void dispose()
    {
    	super.dispose();
    	
    	if (this.levels != null)
    	{
    		this.levels.clear();
    		this.levels = null;
    	}
    }
    
    private class LevelData
    {
    	private final int numberColors, numberLevel;
    	
    	private LevelData(final int numberColors, final int numberLevel)
    	{
    		this.numberColors = numberColors;
    		this.numberLevel = numberLevel;
    	}
    	
    	/**
    	 * Get the level data
    	 * @return The level data formatted to be saved to the hard drive
    	 */
    	private String getData()
    	{
    		return this.numberColors + SCORE_DATA + this.numberLevel;
    	}
    }
}