package com.gamesbykevin.stack.screen;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.stack.assets.Assets;

import java.util.HashMap;

/**
 * The exit screen, when the player wants to go back to the menu
 * @author GOD
 */
public class ExitScreen implements Screen, Disposable
{
    //our main screen reference
    private final ScreenManager screen;
    
    //all of the buttons for the player to control
    private HashMap<Assets.ImageMenuKey, Button> buttons;
    
    /**
     * The dimensions of the buttons
     */
    private static final int BUTTON_DIMENSION = 128;
    
    /**
     * The location of our image text
     */
    private static final int TEXT_X = 40;
    
    /**
     * The location of our image text
     */
    private static final int TEXT_Y = 240;
    
    public ExitScreen(final ScreenManager screen)
    {
        //store our parent reference
        this.screen = screen;
        
        //create buttons
        this.buttons = new HashMap<Assets.ImageMenuKey, Button>();
        this.buttons.put(Assets.ImageMenuKey.Cancel, new Button(Images.getImage(Assets.ImageMenuKey.Cancel)));
        this.buttons.put(Assets.ImageMenuKey.Confirm, new Button(Images.getImage(Assets.ImageMenuKey.Confirm)));
        
        //dimensions of our text image
        final int w = Images.getImage(Assets.ImageMenuKey.ExitText).getWidth();
        final int h = Images.getImage(Assets.ImageMenuKey.ExitText).getHeight();
        
        //position the buttons below the message
        final int y = TEXT_Y + (h * 2);
        
        //position buttons
        this.buttons.get(Assets.ImageMenuKey.Confirm).setX(TEXT_X);
        this.buttons.get(Assets.ImageMenuKey.Confirm).setY(y);
        this.buttons.get(Assets.ImageMenuKey.Cancel).setX(TEXT_X + w - BUTTON_DIMENSION);
        this.buttons.get(Assets.ImageMenuKey.Cancel).setY(y);
        
        //set the bounds of each button
        for (Button button : buttons.values())
        {
            button.setWidth(BUTTON_DIMENSION);
            button.setHeight(BUTTON_DIMENSION);
            button.updateBounds();
        }
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        //do we need anything here
    }
    
    @Override
    public boolean update(final int action, final float x, final float y) throws Exception
    {
        if (action == MotionEvent.ACTION_UP)
        {
        	for (Assets.ImageMenuKey key : buttons.keySet())
        	{
        		Button button = buttons.get(key);
        		
        		if (button == null)
        			continue;
        		
        		if (!button.contains(x, y))
        			continue;
        		
        		switch (key)
        		{
	        		case Cancel:
	                    //if cancel, go back to game
	                    screen.setState(ScreenManager.State.Running);
	                    
	                    //play sound effect
	                    Assets.playMenuSelection();
	                    
	                    //return true;
	                    return false;
	        			
	        		case Confirm:
	                    //if confirm, go back to menu
	                    screen.setState(ScreenManager.State.Ready);
	                    
	                    //play sound effect
	                    Assets.playMenuSelection();
	                    
	                    //return false;
	                    return false;
	                    
                    default:
                    	throw new Exception("Key not handled here " + key.toString());
        		}
        	}
        }
        
        //yes we want additional motion events
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        //nothing needed to update here
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw text
        canvas.drawBitmap(Images.getImage(Assets.ImageMenuKey.ExitText), TEXT_X, TEXT_Y, null);
        
        //render buttons
        buttons.get(Assets.ImageMenuKey.Cancel).render(canvas);
        buttons.get(Assets.ImageMenuKey.Confirm).render(canvas);
    }
    
    @Override
    public void dispose()
    {
        if (buttons != null)
        {
            for (Button button : buttons.values())
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            buttons.clear();
            buttons = null;
        }
    }
}