package com.gamesbykevin.stack.number;

import java.util.ArrayList;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.stack.assets.Assets;

import android.graphics.Canvas;

public class Number extends Entity implements Disposable
{
	//the dimensions of each number animation
	private static final int NUMBER_ANIMATION_WIDTH = 145;
	private static final int NUMBER_ANIMATION_HEIGHT = 166;
	
	//the dimensions of each number render
	public static final int NUMBER_RENDER_WIDTH = 24;
	public static final int NUMBER_RENDER_HEIGHT = 28;
	
	/**
	 * The key for each number animation
	 */
	private enum Key
	{
		Zero(0), One(1), Two(2), Three(3), Four(4), 
		Five(5), Six(6), Seven(7), Eight(8), Nine(9), 
		Colon(10);
		
		private int x, y = 0;
		
		private Key(final int col)
		{
			x = col * NUMBER_ANIMATION_WIDTH;
		}
	}
	
	//our array object for each digit in our score
	private ArrayList<Digit> numbers;

	//the number
	private int number = 0;
	
	/**
	 * Default constructor
	 */
	public Number()
	{
		//create a new numbers list
		this.numbers = new ArrayList<Digit>();
		
		//set the dimensions
		super.setWidth(NUMBER_RENDER_WIDTH);
		super.setHeight(NUMBER_RENDER_HEIGHT);
		
		//add all number animations
		for (Key key : Key.values())
		{
			getSpritesheet().add(
				key, 
				new Animation(
					Images.getImage(Assets.ImageGameKey.Numbers), 
					key.x, 
					key.y, 
					NUMBER_ANIMATION_WIDTH, 
					NUMBER_ANIMATION_HEIGHT
				)
			);
		}
		
		//set default animation
		getSpritesheet().setKey(Key.Zero);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		if (this.numbers != null)
		{
			this.numbers.clear();
			this.numbers = null;
		}
	}
	
	/**
	 * Get the number
	 * @return The number that is rendered in this class
	 */
	public int getNumber()
	{
		return this.number;
	}
	
	/**
	 * Update the number animation
	 * @param number  The new number
	 * @throws Exception
	 */
	public void setNumber(final int number) throws Exception
	{
		//we will re-use the x-coordinate of the first digit as our starting x, also re-use y-coordinate
		if (numbers != null && !numbers.isEmpty())
		{
			setNumber(number, numbers.get(0).x, (int)getY());
		}
		else
		{
			setNumber(number, 0, (int)getY());
		}
		
	}
	
	/**
	 * Assign the number animation
	 * @param number The desired number
	 * @param x The starting x-coordinate
	 * @param y The y-coordinate
	 * @throws Exception If one of the characters in the number String are not mapped
	 */
	public void setNumber(final int number, int x, final int y) throws Exception
	{
		//assign the value
		this.number = number;
		
    	//assign the y-coordinate
    	setY(y);
    	
    	//convert number to string
    	String numberDesc = Integer.toString(number);
    	
    	//disable any unnecessary digits
    	for (int i = numberDesc.length(); i < numbers.size(); i++)
    	{
    		numbers.get(i).setEnabled(false);
    	}
    	
    	//convert string to single characters
    	char[] characters = numberDesc.toCharArray();
    	
    	//check each character so we can map the animations
    	for (int i = 0; i < characters.length; i++)
    	{
    		//the key for the identified character
    		Key tmp = null;
    		
    		//identify which animation
    		switch (characters[i])
    		{
	    		case ':':
	    			tmp = Key.Colon;
	    			break;
			
	    		case '0':
					tmp = Key.Zero;
	    			break;
	    			
	    		case '1':
	    			tmp = Key.One;
	    			break;
	    			
	    		case '2':
	    			tmp = Key.Two;
	    			break;
	    			
	    		case '3':
	    			tmp = Key.Three;
	    			break;
	    			
	    		case '4':
	    			tmp = Key.Four;
	    			break;
	    			
	    		case '5':
	    			tmp = Key.Five;
	    			break;
	    			
	    		case '6':
	    			tmp = Key.Six;
	    			break;
	    			
	    		case '7':
	    			tmp = Key.Seven;
	    			break;
	    			
	    		case '8':
	    			tmp = Key.Eight;
	    			break;
	    			
	    		case '9':
	    			tmp = Key.Nine;
	    			break;
	    			
				default:
					throw new Exception("Character not found '" + characters[i] + "'");
    		}
    		
    		//if we are within the array we can reuse
    		if (i < numbers.size())
    		{
    			numbers.get(i).setX(x);
    			numbers.get(i).setKey(tmp);
    			numbers.get(i).setEnabled(true);
    		}
    		else
    		{
    			//else we add a new object to the array
    			numbers.add(new Digit(x, tmp));
    		}
    		
    		//adjust x-coordinate
    		x += super.getWidth();
    	}
	}
	
    /**
     * Render the assigned number
     * @param canvas
     */
    public void render(final Canvas canvas) throws Exception
    {
    	//check every digit in the list
		for (int i = 0; i < numbers.size(); i++)
		{
			//get the current digit object
			final Digit digit = numbers.get(i);
			
			//if this is not enabled no need to continue
			if (!digit.isEnabled())
				return;
			
    		//assign x-coordinate location
    		setX(digit.x);
    		
    		//assign animation
    		getSpritesheet().setKey(digit.key);
    		
    		//render animation
    		super.render(canvas);
		}
    }
    
    /**
     * This class will keep track of each digit in our number
     */
    private class Digit
    {
    	//location
    	private int x;
    	
    	//animation to render
    	private Key key;
    	
    	//are we rendering this digit
    	private boolean enabled = true;
    	
    	private Digit(final int x, final Key key)
    	{
    		setX(x);
    		setKey(key);
    		setEnabled(true);
    	}
    	
    	private final void setX(final int x)
    	{
    		this.x = x;
    	}
    	
    	private final void setKey(final Key key)
    	{
    		this.key = key;
    	}
    	
    	private final void setEnabled(final boolean enabled)
    	{
    		this.enabled = enabled;
    	}
    	
    	private final boolean isEnabled()
    	{
    		return this.enabled;
    	}
    }
}