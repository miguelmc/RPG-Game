package game.ui.window;

import game.util.MouseManager;
import game.util.Renderer;
import game.util.Renderer.Builder;
import game.util.Util;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

/**
 * Abstract type for windows and statically manages all other windows
 */
public abstract class Window
{

	//TODO make shop a window
	
	private Point position;
	private final Point initialPos;
	protected final Dimension SIZE;
	private Texture texture;
	private boolean grabbed = false;
	protected static final int DOUBLE_CLICK_DELAY = 250;
	
	public Window(Point pos, Dimension size)
	{
		position = pos;
		initialPos = new Point(pos);
		SIZE = size;
		texture = Util.getTexture("UI/window/" + getClass().getSimpleName().toLowerCase() + ".png");
	}
	
	public void render()
	{
		Renderer.render(new Builder(
				texture,
				getPosition(),
				SIZE)
				.imageSize(174, 256));
	}
	
	public void setPosition(int x, int y)
	{
		position.setLocation(x, y);
	}

	public Point getPosition()
	{
		return position;
	}

	public void mouse() {
		if (Util.inRange(MouseManager.getPosition(), getPosition(), SIZE))
		{
			if (MouseManager.mousePressed())
				grabbed = true;
			else if (MouseManager.mouseReleased())
				grabbed = false;
			else if (MouseManager.mouseMoved() && MouseManager.isButtonDown(MouseManager.LEFT)) {
				if(grabbed)
					setPosition(getX() + MouseManager.getDX(), getY() + MouseManager.getDY());
			}
		}
	}

	public int getX()
	{
		return position.getX();
	}

	public void setX(int x)
	{
		position.setX(x);
	}

	public int getY()
	{
		return position.getY();
	}

	public void setY(int y)
	{
		position.setY(y);
	}
	
	public void resetPosition()
	{
		setPosition(initialPos.getX(), initialPos.getY());
	}
	
	public void onClose() {}
}