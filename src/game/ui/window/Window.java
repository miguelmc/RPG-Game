package game.ui.window;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import game.Main;
import game.util.Util;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

// TODO give priorities to window by last opened and draw them on top, give
// "focus" to windows and only close the most recent opened with esc
/**
 * Abstract type for windows and statically manages all other windows
 */
public abstract class Window
{

	private Point position = new Point(100, 100);
	private Dimension size = new Dimension(174, 256);
	private Texture texture;
	private boolean active = false, pressed = false;

	private static Window[] windows = { new Inventory() };
	
	// TODO? create a stack for active windows, add on open and remove on close.
	// Pop on esc pressed.
	
	public Window(Point pos, Dimension s)
	{
		position = pos;
		size = s;
		texture = Util.getTexture("UI/window/" + getClass().getSimpleName().toLowerCase() + ".png");
	}

	public static void renderAll()
	{
		for (Window w : windows)
		{
			if (w.isActive())
				w.render();
		}
	}

	public void render()
	{
		glEnable(GL_TEXTURE_2D);
		texture.bind();
		glColor4f(1, 1, 1, .6f);
		glLoadIdentity();
		glTranslatef(getPosition().getX(), getPosition().getY(), 0);
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			glTexCoord2f(174f / 256f, 0);
			glVertex2f(size.getWidth(), 0);
			glTexCoord2f(174f / 256f, 1);
			glVertex2f(size.getWidth(), size.getHeight());
			glTexCoord2f(0, 1);
			glVertex2f(0, size.getHeight());
		glEnd();
		glLoadIdentity();
		glColor4f(1, 1, 1, 1);
		glDisable(GL_TEXTURE_2D);
	}

	public static void keyboardInput()
	{
		if (Keyboard.getEventKeyState())
		{ // key released
			for (Window w : windows)
			{
				if (Keyboard.getEventKey() == w.getKey())
				{ // each window has a key assigned to open it
					w.toggleActive(); // open/close window
					// if for some reason the window goes out of the screen. (this might happen because mouse events and the game run on different threads)
					if (w.getX() > Main.DIM.getWidth() || w.getX() + w.getWidth() < 0
							|| w.getY() > Main.DIM.getHeight() || w.getY() + w.getHeight() < 0) 
						w.setPosition(100, 100);
				} else if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) // close all
				{ 
					w.close();
				}
			}
		}
	}

	public static void mouseInput()
	{
		for (Window w : windows)
		{
			if (Mouse.getX() >= w.getX() && Mouse.getX() <= w.getX() + w.getWidth()
					&& Main.DIM.getHeight() - Mouse.getY() + 1 >= w.getY()
					&& Main.DIM.getHeight() - Mouse.getY() + 1 <= w.getY() + w.getHeight())
			{
				if (w.isActive())
				{
					w.mouse(); // pass the event to the window
					break; // dont allow 2 windows to respond to the same event
				}
			}
		}
	}

	abstract int getKey();
	
	// TODO make a textureManager to have all textures

	public void setPosition(int x, int y)
	{
		position.setLocation(x, y);
	}

	public Point getPosition()
	{
		return position;
	}

	public boolean isActive()
	{
		return active; // active means open
	}

	protected void close()
	{
		active = false;
	}

	public void toggleActive()
	{
		active = !active;
	}

	abstract protected void mouse();

	public boolean isPressed()
	{
		return pressed;
	}

	public void setPressed(boolean pressed)
	{
		this.pressed = pressed;
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

	public int getWidth()
	{
		return size.getWidth();
	}

	public void setWidth(int width)
	{
		size.setWidth(width);
	}

	public int getHeight()
	{
		return size.getHeight();
	}

	public void setHeight(int height)
	{
		size.setHeight(height);
	}
	
	public Texture getTexture()
	{
		return texture;
	}
	
	public Dimension getSize()
	{
		return new Dimension(size);
	}
}