package game.ui.window;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

public class Quest extends Window
{

	public Quest(Point pos, Dimension s)
	{
		super(pos, s);
	}

	@Override
	protected void mouse()
	{
		//TODO 
	}

	@Override
	int getKey()
	{
		return Keyboard.KEY_Q;
	}

}
