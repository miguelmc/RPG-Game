package game.ui;

import game.Main;
import game.util.Renderer;
import game.util.Util;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;

/**
 * Serves as an interface for sending messages to the player in a textbox. The
 * player stops its actions while the message box is active. Can send an "Ok"
 * messagebox or a "Yes/No"
 */
public class MsgBoxManager
{
	
	public static final int OK = 0, YES_NO = 1;
	
	private static String message = "";
	private static boolean selection = true; // only for "Yes/No" messagebox
	private static int type = OK;
	private static boolean active;
	private static Runnable action;

	public static void render()
	{		
		Dimension size = new Dimension(Main.DIM.getWidth() - 40, 140);
		Point position = new Point(20, Main.DIM.getHeight() - 25 - size.getHeight());

		GL11.glColor4f(0f, 0f, 0f, 0.6f);
		Renderer.renderQuad(position, size); // renders the background
		
		GL11.glColor4f(1f, 1f, 1f, 0.3f); // inner rectangle with different color
		Renderer.renderQuad(new Point(position.getX() + 5, position.getY() + 5), 
							new Dimension(size.getWidth() - 10, size.getHeight() - 10));
			
		//writes the text
		Util.useFont("Monaco", Font.PLAIN, 25, Color.white);
		String lines[] = Util.tokenizeText(message, Main.DIM.getWidth() - 50, 4);
		for (int i = 0; i < lines.length; i++)
			if (!lines[i].isEmpty())
				Util.write(lines[i], position.getX() + 15, position.getY() + 10 + Util.getFontHeight() * i);

		if (type == YES_NO)
		{
			Dimension optionBoxSize = new Dimension((int)(Main.DIM.getWidth()*.25), 45);
			Point optionBoxPos = new Point((int)(Main.DIM.getHeight()*.7), Main.DIM.getHeight() - 50);
			
			GL11.glColor4f(0f, 0f, 0f, .7f); //renders the optionBox
			Renderer.renderQuad(optionBoxPos, optionBoxSize);
			
			Point highlightPos = new Point(optionBoxPos.getX() + 5 + (selection ? 0 : (optionBoxSize.getWidth() - 10)/2), optionBoxPos.getY() + 5);
			GL11.glColor4f(1f, 1f, 0f, .55f); // renders the option selection highlight
			Renderer.renderQuad(highlightPos, new Dimension((optionBoxSize.getWidth() - 10)/2, optionBoxSize.getHeight() - 10));
			
			//Writes the yes/no option
			Util.write("Yes", optionBoxPos.getX() + 25, optionBoxPos.getY() + 5);
			Util.write("No", optionBoxPos.getX() + (optionBoxSize.getWidth() - 10)/2 + 35,  optionBoxPos.getY() + 5);
		}
		
		GL11.glColor4f(1f, 1f, 1f, 1f);
		
	}

	public static void sendMessage(String message, int type, Runnable runnable)
	{
		active = true;
		MsgBoxManager.message = message;
		MsgBoxManager.type = type;
		action = runnable;
		
		System.out.println("active");
		
	}
	
	public static void sendMessage(String message, int type)
	{
		sendMessage(message, type, null);
	}
	
	public static boolean isActive()
	{
		return active;
	}

	public static void input()
	{ // the keyboard events are only received if it is active
		if (Keyboard.getEventKeyState())
		{
			switch (Keyboard.getEventKey())
			{
			case Keyboard.KEY_SPACE:
			case Keyboard.KEY_RETURN:
				active = false;
				if(action != null)
					action.run();
				break;
			case Keyboard.KEY_ESCAPE:
				active = false;
				break;
			case Keyboard.KEY_RIGHT:
				selection = false;
				break;
			case Keyboard.KEY_LEFT:
				selection = true;
				break;
			}
		}

	}

	public static boolean getAnswer()
	{
		return selection;
	}

}
