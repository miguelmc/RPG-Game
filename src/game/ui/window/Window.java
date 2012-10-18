package game.ui.window;

import static org.lwjgl.opengl.GL11.*;

import game.Main;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

import game.util.Util;

// TODO give priorities to window by last opened and draw them on top, give
// "focus" to windows and only close the most recent opened with esc
/**
 * Abstract type for windows and statically manages all other windows
 */
public abstract class Window
{

	protected Point position = new Point(100, 100);
	protected Dimension size = new Dimension(174, 256);
	protected String file;
	protected Texture texture;
	protected boolean active = false;
	protected boolean pressed = false;
	protected int key;

	private static Window[] windows = { new Inventory(Keyboard.KEY_I) };

	// TODO? create a stack for active windows, add on open and remove on close.
	// Pop on esc pressed.

	public Window(Point pos, Dimension s)
	{
		position = pos;
		size = s;
	}

	public static void init()
	{
		for (Window w : windows)
		{
			w.setTexture(w.getClass().getSimpleName().toLowerCase());
		}
	}

	public static void renderAll()
	{
		for (Window w : windows)
		{
			if (w.isActive())
				w.render();
		}
	}

	// TODO adjust with Util.render
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
					if (w.getX() > Main.DIM.getWidth() || w.getX() + w.getWidth() < 0
							|| w.getY() > Main.DIM.getHeight() || w.getY() + w.getHeight() < 0) // if
																								// for
																								// some
																								// reason
																								// the
																								// window
																								// goes
																								// out
																								// of
																								// the
																								// screen.
																								// (this
																								// might
																								// happen
																								// because
																								// mouse
																								// events
																								// and
																								// the
																								// game
																								// run
																								// on
																								// different
																								// threads)
						w.setPosition(100, 100);
				} else if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
				{ // close all
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

	// TODO make a textureManager to have all textures
	public void setTexture(String str)
	{
		texture = Util.getTexture("UI/window/" + str + ".png");
	}

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

	public int getKey()
	{
		return key;
	}

}