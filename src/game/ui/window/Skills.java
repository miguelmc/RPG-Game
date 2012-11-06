package game.ui.window;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

public class Skills extends Window{
	
	public Skills ()
	{
		super(new Point(350, 450), new Dimension(174, 256));
	}
	
	public void render() 
	{
		super.render();
	}
	
	int getKey()
	{
		return Keyboard.KEY_K;
	}

}
